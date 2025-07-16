package gift.E2ETest.wishlist;

import gift.E2ETest.AbstractControllerTest;
import gift.dto.auth.SignupRequest;
import gift.dto.auth.TokenResponse;
import gift.dto.product.ProductCreateRequest;
import gift.dto.product.ProductDefaultResponse;
import gift.dto.wishlist.CreateWishedProductRequest;
import gift.dto.wishlist.WishedProductResponse;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractWishlistTest extends AbstractControllerTest {
    protected List<ProductDefaultResponse> testProducts;
    protected String testToken;


    private TokenResponse signup(String email, String password) {
        // 테스트용 사용자 생성 메서드
        return RestAssured.given()
                .contentType("application/json")
                .body(new SignupRequest(email, password, password))
                .post(getBaseUrl() + "/api/auth/signup")
                .then()
                .statusCode(201)
                .extract()
                .as(TokenResponse.class);
    }

    private ProductDefaultResponse createProduct(ProductCreateRequest request) {
        return RestAssured.given()
                .contentType("application/json")
                .header(AUTH_HEADER_KEY, this.adminToken) // 관리자 토큰 사용
                .body(request)
                .post(getBaseUrl() + "/api/products")
                .then()
                .statusCode(201)
                .extract()
                .as(ProductDefaultResponse.class);
    }

    protected WishedProductResponse addProductToWishlist(Long productId, Integer quantity) {
        // 위시리스트에 제품을 추가하는 메서드
        CreateWishedProductRequest request = new CreateWishedProductRequest(productId, quantity);
        return RestAssured.given()
                .contentType("application/json")
                .body(request)
                .header(AUTH_HEADER_KEY, this.testToken)
                .post(getRequestUrl())
                .then()
                .statusCode(201)
                .extract()
                .as(WishedProductResponse.class);
    }

    @BeforeEach
    public void setUp(RestDocumentationContextProvider provider) {
        super.setUp(provider);
        this.testProducts = new ArrayList<>();
        for (int i= 0; i < 5; i++) {
            ProductCreateRequest request = new ProductCreateRequest(
                    "테스트 제품 " + i, 1000L + i, "이미지 URL " + i
            );
            ProductDefaultResponse response = createProduct(request);
            this.testProducts.add(response);
        }

        UUID radomUUID = UUID.randomUUID();
        var tokenResponse = signup(radomUUID + "@test.com", "qwerty1234@");
        this.testToken = "Bearer " + tokenResponse.token();
    }

    @AfterEach
    public void tearDown() {
        this.testProducts.forEach(product ->
            RestAssured.given()
                    .header(AUTH_HEADER_KEY, this.adminToken)
                    .delete(getBaseUrl() + "/api/products/{id}", product.id())
                    .then()
                    .statusCode(204));
        this.testProducts.clear();

        RestAssured.given()
                .header(AUTH_HEADER_KEY, this.testToken)
                .delete(getRequestUrl())
                .then()
                .statusCode(204);

        RestAssured.given()
                .header(AUTH_HEADER_KEY, this.testToken) // 테스트 사용자 토큰으로 요청
                .delete(getBaseUrl() + "/api/users/me")
                .then()
                .statusCode(204);
    }

    protected String getRequestUrl() {
        return getBaseUrl() + "/api/wishes";
    }

}

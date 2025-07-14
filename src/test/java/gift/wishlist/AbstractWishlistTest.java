package gift.wishlist;

import gift.AbstractControllerTest;
import gift.dto.product.ProductCreateRequest;
import gift.dto.product.ProductDefaultResponse;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractWishlistTest extends AbstractControllerTest {
    protected List<ProductDefaultResponse> testProducts;

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
    }

    @AfterEach
    public void tearDown() {
        this.testProducts.forEach(product -> {
            RestAssured.given()
                    .header(AUTH_HEADER_KEY, this.adminToken)
                    .delete(getBaseUrl() + "/api/products/" + product.id())
                    .then()
                    .statusCode(204);
        });
        this.testProducts.clear();
    }

    protected String getRequestUrl() {
        return getBaseUrl() + "/api/wishes";
    }

}

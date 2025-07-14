package gift.product;

import gift.AbstractControllerTest;
import gift.dto.auth.LoginRequest;
import gift.dto.auth.TokenResponse;
import gift.dto.product.ProductCreateRequest;
import gift.dto.product.ProductDefaultResponse;
import gift.dto.user.UserAdminResponse;
import gift.dto.user.UserCreateRequest;
import gift.entity.UserRole;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractProductTest extends AbstractControllerTest {
    protected Map<UserRole, UserAdminResponse> testUsers;
    protected Map<UserRole, String> testUserTokens;
    protected List<ProductDefaultResponse> testProductIds;

    private UserAdminResponse getAdminResponse(UserCreateRequest request) {
        return RestAssured.given()
                .contentType("application/json")
                .header(AUTH_HEADER_KEY, this.adminToken) // 관리자 토큰 사용
                .body(request)
                .post(getBaseUrl() + "/api/users")
                .then()
                .statusCode(201)
                .extract()
                .as(UserAdminResponse.class);
    }

    private String getToken(UserCreateRequest request) {
        TokenResponse response =  RestAssured.given()
                .contentType("application/json")
                .body(new LoginRequest(request.email(), request.password()))
                .post(getBaseUrl() + "/api/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .as(TokenResponse.class);
        return "Bearer " + response.token();
    }

    @BeforeEach
    public void setUp(RestDocumentationContextProvider provider) {
        super.setUp(provider);
        ProductCreateRequest request =
                new ProductCreateRequest("테스트 제품", 1000L, "이미지 URL");

        Map<UserRole, UserCreateRequest> userRequests = Map.of(
                UserRole.ROLE_ADMIN,
                new UserCreateRequest("user1@example.com", "password123!", "ROLE_ADMIN"),
                UserRole.ROLE_MD,
                new UserCreateRequest("user2@example.com", "password123!", "ROLE_MD"),
                UserRole.ROLE_USER,
                new UserCreateRequest("user3@example.com", "password123!", "ROLE_USER")
        );
        this.testUsers = new HashMap<>();
        this.testUserTokens = new HashMap<>();

        for (Map.Entry<UserRole, UserCreateRequest> entry : userRequests.entrySet()) {
            UserRole role = entry.getKey();
            UserCreateRequest userRequest = entry.getValue();
            UserAdminResponse userResponse = getAdminResponse(userRequest);
            this.testUsers.put(role, userResponse);
            String token = getToken(userRequest);
            this.testUserTokens.put(role, token);
        }


        this.testProductIds = new ArrayList<>();
        ProductDefaultResponse response = RestAssured.given()
                .header(AUTH_HEADER_KEY, this.adminToken)
                .contentType("application/json")
                .body(request)
                .post(getRequestUrl())
                .then()
                .statusCode(201)
                .extract()
                .as(ProductDefaultResponse.class);
        this.testProductIds.add(response);
    }

    @AfterEach
    public void tearDown() {
        if (this.testProductIds == null || this.testProductIds.isEmpty()) {
            return;
        }
        for (ProductDefaultResponse response : this.testProductIds) {
            RestAssured.given()
                    .header(AUTH_HEADER_KEY, this.adminToken)
                    .when()
                    .delete(getBaseUrl() + "/api/products/" + response.id())
                    .then()
                    .statusCode(204);
        }
        this.testProductIds.clear();

        for (UserAdminResponse user : this.testUsers.values()) {
            RestAssured.given()
                    .header(AUTH_HEADER_KEY, this.adminToken)
                    .when()
                    .delete(getBaseUrl() + "/api/users/" + user.id())
                    .then()
                    .statusCode(204);
        }
    }

    public String getRequestUrl() {
        return getBaseUrl() + "/api/products";
    }
}

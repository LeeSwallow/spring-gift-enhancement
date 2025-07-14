package gift.user;


import gift.AbstractControllerTest;
import gift.dto.auth.LoginRequest;
import gift.dto.auth.TokenResponse;
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

public abstract class AbstractUserTest extends AbstractControllerTest {
    protected Map<UserRole, UserAdminResponse> testUsers;
    protected Map<UserRole, String> testUserTokens;
    protected List<Long> testedUserIds;

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
        Map<UserRole, UserCreateRequest> userRequests = Map.of(
                UserRole.ROLE_ADMIN,
                new UserCreateRequest("user1@test.com", "password123!", "ROLE_ADMIN"),
                UserRole.ROLE_MD,
                new UserCreateRequest("user2@test.com", "password123!", "ROLE_MD"),
                UserRole.ROLE_USER,
                new UserCreateRequest("user3@test.com", "password123!", "ROLE_USER")
        );

        this.testUsers = new HashMap<>();
        this.testUserTokens = new HashMap<>();

        for (Map.Entry<UserRole, UserCreateRequest> entry : userRequests.entrySet()) {
            UserRole role = entry.getKey();
            UserCreateRequest request = entry.getValue();
            UserAdminResponse response = getAdminResponse(request);
            this.testUsers.put(role, response);
            this.testUserTokens.put(role, getToken(request));
        }
        this.testedUserIds = new ArrayList<>();
    }

    @AfterEach
    public void tearDown() {
        if (this.testUsers == null || this.testUsers.isEmpty()) {
            return;
        }
        for (UserAdminResponse user : this.testUsers.values()) {
            RestAssured.given()
                    .header(AUTH_HEADER_KEY, this.adminToken)
                    .delete(getBaseUrl() + "/api/users/" + user.id())
                    .then()
                    .statusCode(204);
        }
        if (this.testedUserIds != null && !this.testedUserIds.isEmpty()) {
            for (Long userId : this.testedUserIds) {
                RestAssured.given()
                        .header(AUTH_HEADER_KEY, this.adminToken)
                        .delete(getBaseUrl() + "/api/users/" + userId)
                        .then()
                        .statusCode(204);
            }
        }
        this.testUsers.clear();
        this.testUserTokens.clear();
        this.testedUserIds.clear();
    }

    public String getRequestUrl() {
        return getBaseUrl() + "/api/users";
    }

}

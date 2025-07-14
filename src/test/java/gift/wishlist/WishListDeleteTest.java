package gift.wishlist;

import gift.dto.wishlist.CreateWishedProductRequest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

public class WishListDeleteTest extends  AbstractWishlistTest {

    private void addProductToWishlist(Long productId) {
        // 위시리스트에 제품을 추가하는 메서드
        CreateWishedProductRequest request = new CreateWishedProductRequest(productId, null);
        RestAssured.given()
                .contentType("application/json")
                .body(request)
                .header(AUTH_HEADER_KEY, this.adminToken) // 관리자 권한으로 요청
                .post(getRequestUrl())
                .then()
                .statusCode(201);
    }

    @Test
    @DisplayName("단건 위시리스트 삭제 성공 테스트")
    public void delete_Wishlist_Success() {
        // 단건 위시리스트 삭제 성공 테스트
        Long productId = this.testProducts.getFirst().id();

        // 먼저 위시리스트에 제품을 추가
        addProductToWishlist(productId);

        // 위시리스트에서 제품 삭제 요청
        RestAssured.given(this.spec)
                .filter(document("위시리스트 제품 삭제 성공",
                        pathParameters(
                                parameterWithName("productId").description("삭제할 제품 ID")
                        )))
                .header(AUTH_HEADER_KEY, this.adminToken) // 관리자 권한으로 요청
                .delete(getRequestUrl() + "/{productId}", productId)
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("위시리스트 전체 삭제 성공 테스트")
    public void delete_All_Wishlist_Success() {
        // 위시리스트 전체 삭제 성공 테스트
        this.testProducts.forEach(
                product -> addProductToWishlist(product.id())
        );

        RestAssured.given(this.spec)
                .filter(document("위시리스트 전체 삭제 성공"))
                .header(AUTH_HEADER_KEY, this.adminToken) // 관리자 권한으로 요청
                .delete(getRequestUrl())
                .then()
                .statusCode(204);

        // 위시리스트가 비어있는지 확인
        RestAssured.given()
                .header(AUTH_HEADER_KEY, this.adminToken) // 관리자 권한으로 요청
                .get(getRequestUrl())
                .then()
                .statusCode(200)
                .body("totalElements", equalTo(0))
                .body("contents", empty());
    }

    @Test
    @DisplayName("위시리스트 삭제 실패 테스트: 존재하지 않는 제품 ID(404 Not Found)")
    public void delete_Wishlist_Failure_NotFound() {
        // 존재하지 않는 제품 ID로 위시리스트 삭제 요청
        Long nonExistentProductId = 999L; // 예시로 존재하지 않는 ID 사용

        RestAssured.given(this.spec)
                .filter(document("위시리스트 제품 삭제 실패 - 제품 없음",
                        pathParameters(
                                parameterWithName("productId").description("삭제할 제품 ID")
                        )))
                .header(AUTH_HEADER_KEY, this.adminToken) // 관리자 권한으로 요청
                .delete(getRequestUrl() + "/{productId}", nonExistentProductId)
                .then()
                .statusCode(404);
    }
    @Test
    @DisplayName("위시리스트 삭제 실패 테스트: 권한이 없는 경우(403 Forbidden)")
    public void delete_Wishlist_Failure_Unauthorized() {
        // 위시리스트 삭제 요청 시 권한이 없는 경우
        Long productId = this.testProducts.getFirst().id();
        addProductToWishlist(productId);

        RestAssured.given(this.spec)
                .filter(document("위시리스트 제품 삭제 실패 - 권한 없음",
                        pathParameters(
                                parameterWithName("productId").description("삭제할 제품 ID")
                        )))
                .delete(getRequestUrl() + "/{productId}", productId)
                .then()
                .statusCode(403);
    }

    @Test
    @DisplayName("위시리스트 전체 삭제 실패 테스트: 권한이 없는 경우(403 Forbidden)")
    public void delete_All_Wishlist_Failure_Unauthorized() {
        // 위시리스트 전체 삭제 요청 시 권한이 없는 경우
        RestAssured.given(this.spec)
                .filter(document("위시리스트 전체 삭제 실패 - 권한 없음"))
                .delete(getRequestUrl())
                .then()
                .statusCode(403);
    }
}
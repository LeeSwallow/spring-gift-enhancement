package gift.wishlist;

import gift.dto.wishlist.CreateWishedProductRequest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

public class WishListReadTest extends AbstractWishlistTest {

    static final FieldDescriptor[] PRODUCT_READ_RESPONSE = {
            fieldWithPath("id").description("제품 ID").type(JsonFieldType.NUMBER),
            fieldWithPath("name").description("제품 이름").type(JsonFieldType.STRING),
            fieldWithPath("price").description("제품 가격").type(JsonFieldType.NUMBER),
            fieldWithPath("imageUrl").description("제품 이미지 URL").type(JsonFieldType.STRING),
            fieldWithPath("quantity").description("위시리스트에 추가된 제품의 수량").type(JsonFieldType.NUMBER).optional(),
            fieldWithPath("subtotal").description("위시리스트에 추가된 제품의 총액").type(JsonFieldType.NUMBER).optional(),
            fieldWithPath("createdAt").description("제품 생성 시간").type(JsonFieldType.STRING).optional(),
            fieldWithPath("updatedAt").description("제품 업데이트 시간").type(JsonFieldType.STRING).optional()
    };

    static final FieldDescriptor[] PRODUCT_READ_PAGE_RESPONSE = {
            fieldWithPath("page").description("현재 페이지 번호").type(JsonFieldType.NUMBER),
            fieldWithPath("size").description("페이지 크기").type(JsonFieldType.NUMBER),
            fieldWithPath("totalElements").description("전체 요소 수").type(JsonFieldType.NUMBER),
            fieldWithPath("totalPages").description("전체 페이지 수").type(JsonFieldType.NUMBER),
            fieldWithPath("totalQuantity").description("위시리스트에 추가된 제품의 총 수량").type(JsonFieldType.NUMBER),
            fieldWithPath("totalPrice").description("위시리스트에 추가된 제품의 총액").type(JsonFieldType.NUMBER),
            fieldWithPath("contents[]").description("제품 목록").type(JsonFieldType.ARRAY),
            fieldWithPath("contents[].id").description("제품 ID").type(JsonFieldType.NUMBER).optional(),
            fieldWithPath("contents[].name").description("제품 이름").type(JsonFieldType.STRING).optional(),
            fieldWithPath("contents[].price").description("제품 가격").type(JsonFieldType.NUMBER).optional(),
            fieldWithPath("contents[].imageUrl").description("제품 이미지 URL").type(JsonFieldType.STRING).optional(),
            fieldWithPath("contents[].quantity").description("위시리스트에 추가된 제품의 수량").type(JsonFieldType.NUMBER).optional(),
            fieldWithPath("contents[].subtotal").description("위시리스트에 추가된 제품의 총액").type(JsonFieldType.NUMBER).optional(),
            fieldWithPath("contents[].createdAt").description("제품 생성 시간").type(JsonFieldType.STRING).optional(),
            fieldWithPath("contents[].updatedAt").description("제품 업데이트 시간").type(JsonFieldType.STRING).optional()
    };

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
    @DisplayName("위시리스트 전체 조회 성공 테스트")
    public void find_All_Wishlist_Success() {
        // 위시리스트 전체 조회 성공 테스트
        RestAssured.given(this.spec)
                .filter(document("위시리스트 전체 조회 성공",
                        queryParameters(
                                parameterWithName("page").description("페이지 번호").optional(),
                                parameterWithName("size").description("페이지 크기").optional()
                        ),
                        responseFields(PRODUCT_READ_PAGE_RESPONSE)))
                .header(AUTH_HEADER_KEY, this.adminToken)
                .when()
                .get(getRequestUrl())
                .then()
                .statusCode(200)
                .body("page", notNullValue())
                .body("size", notNullValue())
                .body("totalElements", notNullValue())
                .body("totalPages", notNullValue())
                .body("totalQuantity", notNullValue())
                .body("totalPrice", notNullValue())
                .body("contents", notNullValue());
    }

    @Test
    @DisplayName("위시리스트 전체 조회 실패 테스트 : 잘못된 페이지 요청 시 400 반환")
    public void find_All_Wishlist_Failure_Negative_Page_Request_400_Returned() {
        // 위시리스트 전체 조회 실패 테스트 : 잘못된 페이지 요청 시 400 반환
        RestAssured.given(this.spec)
                .filter(document("위시리스트 전체 조회 실패 - 음수 페이지 요청",
                        queryParameters(
                                parameterWithName("page").description("페이지 번호").optional(),
                                parameterWithName("size").description("페이지 크기").optional()
                        ),
                        responseFields(ERROR_MESSAGE_FIELDS)))
                .header(AUTH_HEADER_KEY, this.adminToken)
                .when()
                .get(getRequestUrl() + "?page=-1&size=5")
                .then()
                .statusCode(400);

        // 위시리스트 전체 조회 실패 테스트 : 잘못된 크기 요청 시 400 반환
        RestAssured.given(this.spec)
                .filter(document("위시리스트 전체 조회 실패 - 음수 크기 요청",
                        queryParameters(
                                parameterWithName("page").description("페이지 번호").optional(),
                                parameterWithName("size").description("페이지 크기").optional()
                        ),
                        responseFields(ERROR_MESSAGE_FIELDS)))
                .header(AUTH_HEADER_KEY, this.adminToken)
                .when()
                .get(getRequestUrl() + "?page=0&size=-1")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("위시리스트 전체 조회 실패 테스트 : 권한이 없는 경우(403 Forbidden)")
    public void find_All_Wishlist_Failure_Unauthorized() {
        // 위시리스트 전체 조회 실패 테스트 : 권한이 없는 경우(403 Forbidden)
        RestAssured.given(this.spec)
                .filter(document("위시리스트 전체 조회 실패 - 권한 없음",
                        queryParameters(
                                parameterWithName("page").description("페이지 번호").optional(),
                                parameterWithName("size").description("페이지 크기").optional()
                        ),
                        responseFields(ERROR_MESSAGE_FIELDS)))
                .when()
                .get(getRequestUrl())
                .then()
                .statusCode(403);
    }


    @Test
    @DisplayName("위시리스트 단건 제품 조회 성공 테스트")
    public void find_Wishlist_Product_Success() {
        // 위시리스트 단건 제품 조회 성공 테스트
        Long productId = this.testProducts.getFirst().id();
        addProductToWishlist(productId);
        RestAssured.given(this.spec)
                .filter(document("위시리스트 단건 제품 조회 성공",
                        responseFields(PRODUCT_READ_RESPONSE)))
                .header(AUTH_HEADER_KEY, this.adminToken)
                .when()
                .get(getRequestUrl() + "/" + productId)
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", notNullValue())
                .body("price", notNullValue())
                .body("imageUrl", notNullValue())
                .body("quantity", notNullValue())
                .body("subtotal", notNullValue())
                .body("id", equalTo(productId.intValue()))
                .body("name", equalTo(this.testProducts.getFirst().name()))
                .body("price", equalTo(this.testProducts.getFirst().price().intValue()))
                .body("imageUrl", equalTo(this.testProducts.getFirst().imageUrl()))
                .body("quantity", equalTo(1))
                .body("subtotal", equalTo(this.testProducts.getFirst().price().intValue()));
    }

    @Test
    @DisplayName("위시리스트 단건 제품 조회 실패 테스트 : 존재하지 않는 제품 ID 요청 시 404 반환")
    public void find_Wishlist_Product_Failure_NonExistentId_404_Returned() {
        // 위시리스트 단건 제품 조회 실패 테스트 : 존재하지 않는 제품 ID 요청 시 404 반환
        RestAssured.given(this.spec)
                .filter(document("위시리스트 단건 제품 조회 실패 - 존재하지 않는 제품 ID",
                        responseFields(ERROR_MESSAGE_FIELDS)))
                .header(AUTH_HEADER_KEY, this.adminToken)
                .when()
                .get(getRequestUrl() + "/999999") // 존재하지 않는 ID
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("위시리스트 단건 제품 조회 실패 테스트 : 권한이 없는 경우(403 Forbidden)")
    public void find_Wishlist_Product_Failure_Unauthorized() {
        // 위시리스트 단건 제품 조회 실패 테스트 : 권한이 없는 경우(403 Forbidden)
        Long productId = this.testProducts.getFirst().id();
        addProductToWishlist(productId);
        RestAssured.given(this.spec)
                .filter(document("위시리스트 단건 제품 조회 실패 - 권한 없음",
                        responseFields(ERROR_MESSAGE_FIELDS)))
                .when()
                .get(getRequestUrl() + "/" + productId)
                .then()
                .statusCode(403);
    }

}


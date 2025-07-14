package gift.product;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

public class ProductReadTest extends AbstractProductTest {

    static final FieldDescriptor [] PRODUCT_READ_RESPONSE = {
            fieldWithPath("id").description("제품 ID").type(JsonFieldType.NUMBER),
            fieldWithPath("name").description("제품 이름").type(JsonFieldType.STRING),
            fieldWithPath("price").description("제품 가격").type(JsonFieldType.NUMBER),
            fieldWithPath("imageUrl").description("제품 이미지 URL").type(JsonFieldType.STRING),
            fieldWithPath("createdAt").description("제품 생성 시간").type(JsonFieldType.STRING).optional(),
            fieldWithPath("updatedAt").description("제품 업데이트 시간").type(JsonFieldType.STRING).optional()
    };

    static final FieldDescriptor [] PRODUCT_READ_PAGE_RESPONSE = {
            fieldWithPath("page").description("현재 페이지 번호").type(JsonFieldType.NUMBER),
            fieldWithPath("size").description("페이지 크기").type(JsonFieldType.NUMBER),
            fieldWithPath("totalElements").description("전체 요소 수").type(JsonFieldType.NUMBER),
            fieldWithPath("totalPages").description("전체 페이지 수").type(JsonFieldType.NUMBER),
            fieldWithPath("contents[]").description("제품 목록").type(JsonFieldType.ARRAY),
            fieldWithPath("contents[].id").description("제품 ID").type(JsonFieldType.NUMBER).optional(),
            fieldWithPath("contents[].name").description("제품 이름").type(JsonFieldType.STRING).optional(),
            fieldWithPath("contents[].price").description("제품 가격").type(JsonFieldType.NUMBER).optional(),
            fieldWithPath("contents[].imageUrl").description("제품 이미지 URL").type(JsonFieldType.STRING).optional(),
            fieldWithPath("contents[].createdAt").description("제품 생성 시간").type(JsonFieldType.STRING).optional(),
            fieldWithPath("contents[].updatedAt").description("제품 업데이트 시간").type(JsonFieldType.STRING).optional()
    };


    @Test
    @DisplayName("전체 제품 조회 성공 테스트")
    public void find_All_Products_Success() {
        String url = getBaseUrl() + "/api/products";
        RestAssured.given(this.spec)
                .filter(document("상품 전체 조회 성공",
                        queryParameters(
                            parameterWithName("page").description("페이지 번호").optional(),
                            parameterWithName("size").description("페이지 크기").optional()
                        ),
                        responseFields(PRODUCT_READ_PAGE_RESPONSE)))
                .when()
                .get(url)
                .then()
                .statusCode(200)
                .body("page", notNullValue())
                .body("size", notNullValue())
                .body("totalElements", notNullValue())
                .body("totalPages", notNullValue())
                .body("contents", notNullValue())
                .body("contents[0].id", notNullValue())
                .body("contents[0].name", notNullValue())
                .body("contents[0].price", notNullValue())
                .body("contents[0].imageUrl", notNullValue());
    }

    @Test
    @DisplayName("전체 제품 조회 실패 테스트 : 음수 페이지 요청 시 400 반환")
    public void find_All_Products_Failure_Negative_Page_Request_400_Returned() {
        RestAssured.given(this.spec)
                .filter(document("상품 전체 조회 실패 - 음수 페이지 요청",
                        queryParameters(
                            parameterWithName("page").description("페이지 번호").optional(),
                            parameterWithName("size").description("페이지 크기").optional()
                        ),
                        responseFields(ERROR_MESSAGE_FIELDS))
                )
                .when()
                .get(getBaseUrl() + "/api/products?page=-1&size=5")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("전체 제품 조회 실패 테스트 : 음수 크기 요청 시 400 반환")
    public void find_All_Products_Failure_Negative_Size_Request_400_Returned() {
        String url = getBaseUrl() + "/api/products?page=0&size=-1";
        RestAssured.given(this.spec)
                .filter(document("상품 전체 조회 실패 - 음수 크기 요청",
                        queryParameters(
                            parameterWithName("page").description("페이지 번호").optional(),
                            parameterWithName("size").description("페이지 크기").optional()
                        ),
                        responseFields(ERROR_MESSAGE_FIELDS))
                )
                .when()
                .get(url)
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("특정 제품 조회 성공 테스트")
    public void find_Specific_Product_Success() {
        String url = getBaseUrl() + "/api/products/{id}"; // 존재하는 제품 ID
        Long testProductId = this.testProductIds.getFirst().id(); // 테스트용 제품 ID 가져오기
        RestAssured
                .given(this.spec)
                .filter(document("상품 특정 조회 성공",
                        pathParameters(
                                parameterWithName("id").description("조회할 제품 ID")
                        ),
                        responseFields(PRODUCT_READ_RESPONSE)))
                .when()
                .get(url, testProductId)
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", notNullValue())
                .body("price", notNullValue())
                .body("imageUrl", notNullValue());
    }

    @Test
    @DisplayName("특정 제품 조회 실패 테스트 : 존재하지 않는 제품 ID")
    public void find_Specific_Product_Failure_NonExistentId_404_Returned() {
        String url = getBaseUrl() + "/api/products/{id}"; // 존재하지 않는 제품 ID
        RestAssured.given(this.spec)
                .filter(document("상품 특정 조회 실패 - 존재하지 않는 제품 ID",
                        pathParameters(
                                parameterWithName("id").description("조회할 제품 ID")
                        ),
                        responseFields(ERROR_MESSAGE_FIELDS)))
                .when()
                .get(url,9999) // 존재하지 않는 제품 ID
                .then()
                .statusCode(404);
    }
}


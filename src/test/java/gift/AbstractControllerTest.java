package gift;

import gift.dto.auth.LoginRequest;
import gift.dto.auth.TokenResponse;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public abstract class AbstractControllerTest {

    @LocalServerPort
    protected int port;
    protected RequestSpecification spec;
    protected String adminToken;

    public static final String AUTH_HEADER_KEY = "authorization";
    public static final HeaderDescriptor[] AUTHENTICATE_HEADERS = {
            headerWithName("authorization").description("JWT 인증 토큰").optional()
    };

    public static  final FieldDescriptor[] ERROR_MESSAGE_FIELDS = {
        fieldWithPath("type").description("에러를 해결할 수 있는 문서 주소").type(JsonFieldType.STRING),
        fieldWithPath("title").description("에러 제목").type(JsonFieldType.STRING),
        fieldWithPath("status").description("HTTP 상태 코드").type(JsonFieldType.NUMBER),
        fieldWithPath("detail").description("에러 상세 메시지").type(JsonFieldType.STRING),
        fieldWithPath("instance").description("에러 인스턴스 ID").type(JsonFieldType.STRING).optional(),
        fieldWithPath("timestamp").description("에러 발생 시간").type(JsonFieldType.STRING),
        fieldWithPath("stackTrace").description("스택 트레이스 (개발 환경에서만 사용)").type(JsonFieldType.ARRAY).optional(),
        fieldWithPath("validationErrors").description("유효성 검사 오류 목록").type(JsonFieldType.ARRAY).optional(),
        fieldWithPath("validationErrors[].field").description("유효성 검사 오류 필드").type(JsonFieldType.STRING).optional(),
        fieldWithPath("validationErrors[].message").description("유효성 검사 오류 메시지").type(JsonFieldType.STRING).optional(),
    };

    @BeforeEach
    protected void setUp(RestDocumentationContextProvider provider) {
        RestAssured.port = port;
        this.spec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(provider))
                .build();

        LoginRequest request = new LoginRequest(
             "test@test.com",
                "qwerty1234@"
        );

        TokenResponse tokenResponse = RestAssured.given()
                .contentType("application/json")
                .body(request)
                .post(getBaseUrl() + "/api/auth/login")
                .as(TokenResponse.class);

        this.adminToken = "Bearer " + tokenResponse.token();
    }

    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }
}

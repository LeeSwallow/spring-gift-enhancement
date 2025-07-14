package gift.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import java.security.MessageDigest;

@Component("serverStartupVerifier")
public class ServerStartupVerifier {
    private static final Logger log = LoggerFactory.getLogger(ServerStartupVerifier.class);

    private final String jwtToken;
    private final Integer jwtExpirationTime;
    private final ConfigurableApplicationContext context;
    private String passwordEncoderAlgorithm;
    private final String adminEmail;
    private final String adminPassword;


   public ServerStartupVerifier(
                @Value("${gift.jwt.secret:#{null}}") String jwtToken,
                @Value("${gift.jwt.expiration:#{null}}") Integer jwtExpirationTime,
                @Value("${gift.password.encoder.algorithm:#{null}}") String passwordEncoderAlgorithm,
                @Value("${gift.admin.email:#{null}}") String adminEmail,
                @Value("${gift.admin.password:#{null}}") String adminPassword,
                ConfigurableApplicationContext context
   ) {
        this.jwtToken = jwtToken;
        this.jwtExpirationTime = jwtExpirationTime;
        this.passwordEncoderAlgorithm = passwordEncoderAlgorithm;
        this.context = context;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
   }

   @PostConstruct
    public void verifyServerStartup() {
        log.info("서버 시작 검증을 시작합니다.");
        JwtVerification();
        passwordEncoderVerification();
        adminCredentialsVerification();
        log.info("서버 시작 검증을 통과했습니다.");
    }

    private void haltServerStartup() {
        log.error("서버 시작 검증에 실패했습니다. 서버를 중단합니다.");
        context.close();
    }

    private void JwtVerification() {
        log.info("JWT 설정을 검증합니다.");
        if (jwtToken == null || jwtToken.isEmpty()) {
            log.error("JWT 토큰이 설정되지 않았습니다. application.properties 파일에서 gift.jwt.token 값을 확인하세요.");
            haltServerStartup();
        }else if (jwtToken.length() < 32) {
            log.error("JWT 토큰이 너무 짧습니다. 최소 32자 이상이어야 합니다. application.properties 파일에서 gift.jwt.token 값을 확인하세요.");
            haltServerStartup();
        }

        if (jwtExpirationTime == null) {
            log.error("JWT 만료 시간 설정이 누락되었습니다. application.properties 파일에서 gift.jwt.expiration 값을 확인하세요.");
            haltServerStartup();
        } else if (jwtExpirationTime <= 0) {
            log.error("JWT 만료 시간은 0보다 커야 합니다. 현재 설정된 값: {}", jwtExpirationTime);
            haltServerStartup();
        }

        log.info("JWT 설정 검증을 통과했습니다.");
    }

    private void passwordEncoderVerification() {
        log.info("비밀번호 인코딩 수행을 검증합니다.");
        if (passwordEncoderAlgorithm == null || passwordEncoderAlgorithm.isEmpty()) {
            log.warn("비밀번호 인코딩 알고리즘이 설정되지 않았습니다. 기본값인 SHA-256을 사용합니다.");
            passwordEncoderAlgorithm = "SHA-256"; // 기본값 설정
        }
        try {
            String testPassword = "testPassword";
            MessageDigest messageDigest = MessageDigest.getInstance(passwordEncoderAlgorithm);
            messageDigest.digest(testPassword.getBytes());
        } catch (Exception e) {
            log.error("비밀번호 인코딩 알고리즘이 유효하지 않습니다: {}. application.properties 파일에서 " +
                    "gift.password.encoder.algorithm 값을 확인하세요.", passwordEncoderAlgorithm);
            haltServerStartup();
        }
        log.info("비밀번호 인코딩 수행 검증을 통과했습니다.");
    }

    private void adminCredentialsVerification() {
        log.info("관리자 계정 설정을 검증합니다.");
        if (adminEmail == null || adminEmail.isEmpty()) {
            log.error("관리자 이메일이 설정되지 않았습니다. application.properties 파일에서 gift.admin.email 값을 확인하세요.");
            haltServerStartup();
        }
        if (adminPassword == null || adminPassword.isEmpty()) {
            log.error("관리자 비밀번호가 설정되지 않았습니다. application.properties 파일에서 gift.admin.password 값을 확인하세요.");
            haltServerStartup();
        }
        log.info("관리자 계정 설정 검증을 통과했습니다.");
    }
}

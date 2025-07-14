package gift.common.util;

import gift.common.exception.CriticalServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
@DependsOn("serverStartupVerifier")
public class PasswordEncoder {
    private final String passwordEncodingAlgorithm;

    public PasswordEncoder(
            @Value("${gift.password.encoder.algorithm:SHA-256}") String passwordEncodingAlgorithm
    ) {
        this.passwordEncodingAlgorithm = passwordEncodingAlgorithm;
    }

    public String encode(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 null이거나 비어있을 수 없습니다.");
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(passwordEncodingAlgorithm);
            byte[] hashedBytes = messageDigest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new CriticalServerException("비밀번호 인코딩에 실패했습니다. " + passwordEncodingAlgorithm +
                    "알고리즘을 사용할 수 없습니다.", e.getCause());
        }
    }

    public Boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            throw new IllegalArgumentException("비밀번호와 인코딩된 비밀번호는 null일 수 없습니다.");
        }
        String hashedRawPassword = encode(rawPassword);
        return hashedRawPassword.equals(encodedPassword);
    }
}

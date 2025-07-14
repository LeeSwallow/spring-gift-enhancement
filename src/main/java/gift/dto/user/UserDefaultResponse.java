package gift.dto.user;

import gift.entity.User;

public record UserDefaultResponse(
    Long id,
    String email
) {
    public static UserDefaultResponse from(User user) {
        return new UserDefaultResponse(user.getId(), user.getEmail());
    }
}
package gift.dto.wishlist;

import jakarta.validation.constraints.Min;

public record PatchWishedProductRequest (
    @Min(value = 1, message = "증감할 제품의 수량은 1 이상이어야 합니다.")
    Integer quantity,
    Boolean increment
) {
    public PatchWishedProductRequest {
        // 기본값 설정
        if (quantity == null) {
            quantity = 1;
        }
        if (increment == null) {
            increment = true;
        }
    }
}

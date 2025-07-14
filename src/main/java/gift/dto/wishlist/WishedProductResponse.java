package gift.dto.wishlist;

import gift.entity.WishedProduct;

import java.time.Instant;

public record WishedProductResponse(
        Long id,
        String name,
        Long price,
        String imageUrl,
        Integer quantity,
        Long subtotal,
        Instant createdAt,
        Instant updatedAt
) {
    public static WishedProductResponse from(WishedProduct wishedProduct) {
        return new WishedProductResponse(
                wishedProduct.getId(),
                wishedProduct.getName(),
                wishedProduct.getPrice(),
                wishedProduct.getImageUrl(),
                wishedProduct.getQuantity(),
                wishedProduct.getSubtotal(),
                wishedProduct.getCreatedAt(),
                wishedProduct.getUpdatedAt()
        );
    }
}

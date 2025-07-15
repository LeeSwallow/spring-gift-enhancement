package gift.dto.wishlist;

import gift.entity.Product;
import gift.entity.WishedProduct;

import java.time.Instant;

public record WishedProductResponse(
        Long id,
        Long productId,
        String name,
        Long price,
        String imageUrl,
        Integer quantity,
        Long subtotal,
        Instant createdAt,
        Instant updatedAt
) {
    public static WishedProductResponse from(WishedProduct wishedProduct) {
        Product product = wishedProduct.getProduct();
        Long subtotal = product.getPrice() * wishedProduct.getQuantity();

        return new WishedProductResponse(
                wishedProduct.getId(),
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                wishedProduct.getQuantity(),
                subtotal,
                wishedProduct.getCreatedAt(),
                wishedProduct.getUpdatedAt()
        );
    }
}

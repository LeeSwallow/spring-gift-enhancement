package gift.dto.product;

import gift.entity.Product;

import java.time.Instant;

public record ProductDefaultResponse(
    Long id,
    String name,
    Long price,
    String imageUrl,
    Instant createdAt,
    Instant updatedAt
) {

    public static ProductDefaultResponse from(Product product) {
        return new ProductDefaultResponse(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }
}

package gift.dto.product;

import java.time.Instant;

public record ProductDefaultResponse(
    Long id,
    String name,
    Long price,
    String imageUrl,
    Instant createdAt,
    Instant updatedAt
) {
}

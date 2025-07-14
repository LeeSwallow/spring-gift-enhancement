package gift.entity;

import java.time.Instant;

public abstract class AbstractEntity {
    Instant createdAt;
    Instant updatedAt;

    public AbstractEntity() {}

    public AbstractEntity(Instant createdAt, Instant updatedAt) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void update() {
        this.updatedAt = Instant.now();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}

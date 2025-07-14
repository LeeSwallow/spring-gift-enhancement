package gift.entity;

import java.time.Instant;
import java.util.Objects;

public class Product extends AbstractEntity {
    private Long id;
    private String name;
    private Long price;
    private String imageUrl;
    private Long ownerId;

    public Product(Long id, String name, Long price, String imageUrl, Long ownerId) {
        super();
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.ownerId = ownerId;
    }

    public Product(
            Long id,
            String name,
            Long price,
            String imageUrl,
            Long ownerId,
            Instant createdAt,
            Instant updatedAt
    ) {
        super(createdAt, updatedAt);
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.ownerId = ownerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", ownerId=" + ownerId +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Product objProd)) return false;
        return Objects.equals(id, objProd.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), id);
    }
}

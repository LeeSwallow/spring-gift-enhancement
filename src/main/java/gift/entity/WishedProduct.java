package gift.entity;

import java.time.Instant;

public class WishedProduct extends AbstractEntity {
    private Long id;
    private String name;
    private Long price;
    private String imageUrl;
    private Integer quantity;

    public WishedProduct(
            Long id,
            String name,
            Long price,
            String imageUrl,
            Integer quantity,
            Instant createdAt,
            Instant updatedAt
    ) {
        super(createdAt, updatedAt);
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getSubtotal() {
        return price * quantity;
    }
}

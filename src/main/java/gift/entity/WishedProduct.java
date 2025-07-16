package gift.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name="wished_products")
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "WishedProduct.withUser",
                attributeNodes = {@NamedAttributeNode("user")}
        ),
        @NamedEntityGraph(
                name = "WishedProduct.withProduct",
                attributeNodes = {@NamedAttributeNode("product")}
        ),
        @NamedEntityGraph(
                name = "WishedProduct.withUserAndProduct",
                attributeNodes = {
                        @NamedAttributeNode("user"),
                        @NamedAttributeNode("product")
                }
        )
})
public class WishedProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    private Integer quantity;


    public WishedProduct() {

    }

    public WishedProduct(Long id, User user, Product product, Integer quantity) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

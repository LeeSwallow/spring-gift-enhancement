package gift.UnitTest.repository;

import gift.entity.Product;
import gift.repository.product.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class ProductRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @Order(1)
    @DisplayName("상품 저장 테스트")
    public void save() {
        Product product = new Product(null, "Test Product", 1000L, "http://example.com/image.jpg",1L);
        Product saved = productRepository.save(product);
        assertAll (
                () -> assertNotNull(saved.getId()),
                () -> assertEquals(product.getName(), saved.getName()),
                () -> assertEquals(product.getPrice(), saved.getPrice()),
                () -> assertEquals(product.getImageUrl(), saved.getImageUrl()),
                () -> assertEquals(product.getOwnerId(), saved.getOwnerId())
        );
    }

    @Test
    @Order(2)
    @DisplayName("상품 조회 테스트")
    public void findById() {
        Product product = new Product(null, "Test Product", 1000L, "http://example.com/image.jpg",1L);
        Product saved = productRepository.save(product);

        Product found = productRepository.findById(saved.getId()).orElse(null);
        Assertions.assertNotNull(found);
        assertAll(
                () -> assertEquals(saved.getId(), found.getId()),
                () -> assertEquals(saved.getName(), found.getName()),
                () -> assertEquals(saved.getPrice(), found.getPrice()),
                () -> assertEquals(saved.getImageUrl(), found.getImageUrl()),
                () -> assertEquals(saved.getOwnerId(), found.getOwnerId())
        );
    }

    @Test
    @Order(3)
    @DisplayName("상품 수정 테스트")
    public void update() {
        Product product = new Product(null, "Test Product", 1000L, "http://example.com/image.jpg", 1L);
        Product saved = productRepository.save(product);

        saved.setName("Updated Product");
        saved.setPrice(2000L);
        saved.setImageUrl("http://example.com/updated_image.jpg");
        Product updated = productRepository.save(saved);

        assertAll(
                () -> assertNotNull(updated.getId()),
                () -> assertEquals("Updated Product", updated.getName()),
                () -> assertEquals(2000L, updated.getPrice()),
                () -> assertEquals("http://example.com/updated_image.jpg", updated.getImageUrl()),
                () -> assertEquals(saved.getOwnerId(), updated.getOwnerId())
        );
    }


    @Test
    @Order(4)
    @DisplayName("상품 삭제 테스트")
    public void delete() {
        Product product = new Product(null, "Test Product", 1000L, "http://example.com/image.jpg", 1L);
        Product saved = productRepository.save(product);

        productRepository.deleteById(saved.getId());
        Product found = productRepository.findById(saved.getId()).orElse(null);
        assertAll(
                () -> assertNull(found, "삭제된 상품은 조회되지 않아야 합니다.")
        );
    }
}

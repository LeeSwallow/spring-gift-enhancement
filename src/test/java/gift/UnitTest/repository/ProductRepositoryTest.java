package gift.UnitTest.repository;

import gift.entity.Product;
import gift.repository.product.ProductRepository;
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
                () -> assertNotNull(saved.getId(), "상품 ID는 null이 아니어야 합니다."),
                () -> assertEquals(product.getName(), saved.getName(), "상품 이름이 일치해야 합니다."),
                () -> assertEquals(product.getPrice(), saved.getPrice(), "상품 가격이 일치해야 합니다."),
                () -> assertEquals(product.getImageUrl(), saved.getImageUrl(), "상품 이미지 URL이 일치해야 합니다."),
                () -> assertEquals(product.getOwnerId(), saved.getOwnerId(), "상품 소유자 ID가 일치해야 합니다.")
        );
    }

    @Test
    @Order(2)
    @DisplayName("상품 조회 테스트")
    public void findById() {
        Product product = new Product(null, "Test Product", 1000L, "http://example.com/image.jpg",1L);
        Product saved = productRepository.save(product);

        Product found = productRepository.findById(saved.getId()).orElse(null);
        assertAll(
                () -> assertNotNull(found, "저장된 상품을 조회해야 합니다."),
                () -> assertEquals(saved.getId(), found.getId(), "조회된 상품 ID가 일치해야 합니다."),
                () -> assertEquals(saved.getName(), found.getName(), "조회된 상품 이름이 일치해야 합니다."),
                () -> assertEquals(saved.getPrice(), found.getPrice(), "조회된 상품 가격이 일치해야 합니다."),
                () -> assertEquals(saved.getImageUrl(), found.getImageUrl(), "조회된 상품 이미지 URL이 일치해야 합니다."),
                () -> assertEquals(saved.getOwnerId(), found.getOwnerId(), "조회된 상품 소유자 ID가 일치해야 합니다.")
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
                () -> assertNotNull(updated.getId(), "업데이트된 상품 ID는 null이 아니어야 합니다."),
                () -> assertEquals("Updated Product", updated.getName(), "업데이트된 상품 이름이 일치해야 합니다."),
                () -> assertEquals(2000L, updated.getPrice(), "업데이트된 상품 가격이 일치해야 합니다."),
                () -> assertEquals("http://example.com/updated_image.jpg", updated.getImageUrl(), "업데이트된 상품 이미지 URL이 일치해야 합니다."),
                () -> assertEquals(saved.getOwnerId(), updated.getOwnerId(), "업데이트된 상품 소유자 ID가 일치해야 합니다.")
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

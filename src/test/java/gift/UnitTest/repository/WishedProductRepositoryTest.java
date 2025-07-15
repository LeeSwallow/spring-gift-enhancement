package gift.UnitTest.repository;

import gift.entity.Product;
import gift.entity.Role;
import gift.entity.User;
import gift.entity.WishedProduct;
import gift.repository.product.ProductRepository;
import gift.repository.role.RoleRepository;
import gift.repository.user.UserRepository;
import gift.repository.wishlist.WishedProductRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

public class WishedProductRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WishedProductRepository wishedProductRepository;

    private User testUser;
    private List<Product> testProducts;

    @BeforeEach
    public void setUp() {
        roleRepository.saveAll(List.of(
                new Role("ROLE_USER"),
                new Role("ROLE_MD"),
                new Role("ROLE_ADMIN")
        ));

        if (this.testUser == null) {
            User user = new User();
            user.setEmail("testuser@test.com");
            user.setPassword("testuser123!");
            var roles = new ArrayList<>(List.of((roleRepository.findByName("ROLE_USER").orElseThrow())));
            user.setRoles(roles);
            this.testUser = userRepository.save(user);
        }
        if (this.testProducts == null) {
            this.testProducts = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Product product = new Product(null, "Test Product " + i, 1000L + (i * 100), "http://example.com/image" + i + ".jpg", this.testUser.getId());
                this.testProducts.add(productRepository.save(product));
            }
        }
    }

    @Test
    @Order(1)
    @DisplayName("위시리스트에 상품 추가 테스트")
    public void save_test() {
        WishedProduct wishedProduct = new WishedProduct();
        wishedProduct.setUser(this.testUser);
        wishedProduct.setProduct(this.testProducts.getFirst());
        wishedProduct.setQuantity(2);

        WishedProduct saved = wishedProductRepository.save(wishedProduct);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(saved.getId(), "저장된 위시리스트 상품의 ID는 null이 아니어야 합니다."),
                () -> Assertions.assertEquals(this.testUser.getId(), saved.getUser().getId(), "저장된 위시리스트 상품의 사용자 ID가 일치해야 합니다."),
                () -> Assertions.assertEquals(this.testProducts.getFirst().getId(), saved.getProduct().getId(), "저장된 위시리스트 상품의 제품 ID가 일치해야 합니다."),
                () -> Assertions.assertEquals(2, saved.getQuantity(), "저장된 위시리스트 상품의 수량이 일치해야 합니다.")
        );
    }

    @Test
    @Order(2)
    @DisplayName("위시리스트 상품 조회 테스트")
    public void findById_test() {
        WishedProduct wishedProduct = new WishedProduct();
        wishedProduct.setUser(this.testUser);
        wishedProduct.setProduct(this.testProducts.getFirst());
        wishedProduct.setQuantity(2);
        WishedProduct saved = wishedProductRepository.save(wishedProduct);

        WishedProduct found = wishedProductRepository.findById(saved.getId()).orElse(null);
        Assertions.assertNotNull(found, "저장된 위시리스트 상품을 조회해야 합니다.");
        Assertions.assertAll(
                () -> Assertions.assertEquals(saved.getId(), found.getId(), "조회된 위시리스트 상품의 ID가 일치해야 합니다."),
                () -> Assertions.assertEquals(saved.getUser().getId(), found.getUser().getId(), "조회된 위시리스트 상품의 사용자 ID가 일치해야 합니다."),
                () -> Assertions.assertEquals(saved.getProduct().getId(), found.getProduct().getId(), "조회된 위시리스트 상품의 제품 ID가 일치해야 합니다."),
                () -> Assertions.assertEquals(saved.getQuantity(), found.getQuantity(), "조회된 위시리스트 상품의 수량이 일치해야 합니다.")
        );
    }

    @Test
    @Order(3)
    @DisplayName("위시리스트 상품 수정 테스트")
    public void update_test() {
        WishedProduct wishedProduct = new WishedProduct();
        wishedProduct.setUser(this.testUser);
        wishedProduct.setProduct(this.testProducts.getFirst());
        wishedProduct.setQuantity(2);
        WishedProduct saved = wishedProductRepository.save(wishedProduct);

        saved.setQuantity(3);
        WishedProduct updated = wishedProductRepository.save(saved);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(updated.getId(), "업데이트된 위시리스트 상품의 ID는 null이 아니어야 합니다."),
                () -> Assertions.assertEquals(saved.getUser().getId(), updated.getUser().getId(), "업데이트된 위시리스트 상품의 사용자 ID가 일치해야 합니다."),
                () -> Assertions.assertEquals(saved.getProduct().getId(), updated.getProduct().getId(), "업데이트된 위시리스트 상품의 제품 ID가 일치해야 합니다."),
                () -> Assertions.assertEquals(3, updated.getQuantity(), "업데이트된 위시리스트 상품의 수량이 일치해야 합니다.")
        );
    }

    @Test
    @Order(4)
    @DisplayName("위시리스트 단건 삭제 테스트")
    public void delete_test() {
        WishedProduct wishedProduct = new WishedProduct();
        wishedProduct.setUser(this.testUser);
        wishedProduct.setProduct(this.testProducts.getFirst());
        wishedProduct.setQuantity(2);
        WishedProduct saved = wishedProductRepository.save(wishedProduct);

        wishedProductRepository.delete(saved);
        Assertions.assertFalse(wishedProductRepository.findById(saved.getId()).isPresent(), "삭제된 위시리스트 상품은 조회되지 않아야 합니다.");
    }

    @Test
    @Order(5)
    @DisplayName("위시리스트 전체 삭제 테스트")
    public void deleteAll_test() {
        testProducts.forEach(product -> {
            WishedProduct wishedProduct = new WishedProduct();
            wishedProduct.setUser(this.testUser);
            wishedProduct.setProduct(product);
            wishedProduct.setQuantity(1);
            wishedProductRepository.save(wishedProduct);
        });
        wishedProductRepository.deleteAll();
        Assertions.assertTrue(wishedProductRepository.findAll().isEmpty(), "모든 위시리스트 상품이 삭제되어야 합니다.");
    }

    @Test
    @Order(6)
    @DisplayName("사용자별 위시리스트 페이지네이션 테스트")
    public void findByUserIdWithPagination_test() {
        int quantity = 1;
        long totalPrice = 0;
        long totalQuantity = 0;
        for (Product product : testProducts) {
            WishedProduct wishedProduct = new WishedProduct();
            wishedProduct.setUser(this.testUser);
            wishedProduct.setProduct(product);
            wishedProduct.setQuantity(quantity++);
            wishedProductRepository.save(wishedProduct);
            totalPrice += product.getPrice() * wishedProduct.getQuantity();
            totalQuantity += wishedProduct.getQuantity();
        }
        var pagedProducts = wishedProductRepository.findAllByUserId(this.testUser.getId(), PageRequest.of(0, 5)).getContent();

        Assertions.assertFalse(pagedProducts.isEmpty(), "사용자별 위시리스트는 비어있지 않아야 합니다.");
        Assertions.assertAll(
                () -> Assertions.assertEquals(5, pagedProducts.size(), "페이지 크기는 5여야 합니다."),
                () -> Assertions.assertEquals(this.testUser.getId(), pagedProducts.getFirst().getUser().getId(), "모든 위시리스트 상품은 동일한 사용자 ID를 가져야 합니다.")
        );

        var stats = wishedProductRepository.calculateStatsByUserId(this.testUser.getId());
        long finalTotalPrice = totalPrice;
        long finalTotalQuantity = totalQuantity;
        Assertions.assertAll(
                () -> Assertions.assertEquals(finalTotalPrice, stats.getTotalPrice(), "총 가격이 일치해야 합니다."),
                () -> Assertions.assertEquals(finalTotalQuantity, stats.getTotalQuantity(), "총 수량이 일치해야 합니다.")
        );
    }
}

package gift.service.wishlist;

import gift.common.model.CustomPage;
import gift.entity.WishedProduct;
import gift.repository.product.ProductRepository;
import gift.repository.user.UserRepository;
import gift.repository.wishlist.WishedProductRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class WishedProductServiceImpl implements WishedProductService {
    private final WishedProductRepository wishedProductRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public WishedProductServiceImpl(
            WishedProductRepository wishedProductRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        this.wishedProductRepository = wishedProductRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    private void validateUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("존재하지 않는 사용자입니다. userId: " + userId);
        }
    }

    public void validateProductId(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new NoSuchElementException("존재하지 않는 제품입니다. productId: " + productId);
        }
    }

    @Override
    @Transactional
    public CustomPage<WishedProduct> findAllBy(Long userId, int page, int size) {
        validateUserId(userId);
        var pagedProducts = wishedProductRepository.findAllByUserId(userId, PageRequest.of(page, size));
        var customPage = CustomPage.from(pagedProducts);
        var stats = wishedProductRepository.calculateStatsByUserId(userId);
        customPage.setExtras(
                Map.of("totalQuantity", stats.getTotalQuantity(), "totalPrice", stats.getTotalPrice())
        );
        return customPage;
    }

    @Override
    @Transactional
    public WishedProduct findBy(Long userId, Long wishedProductId) {
        validateUserId(userId);
        var wishedProduct = wishedProductRepository.findById(wishedProductId)
                .orElseThrow(() -> new NoSuchElementException("장바구니에 해당 제품이 없습니다. wishedProductId: " + wishedProductId));

        if (!wishedProduct.getUser().getId().equals(userId)) {
            throw new NoSuchElementException("장바구니에 해당 제품이 없습니다. wishedProductId: " + wishedProductId);
        }
        return wishedProduct;
    }

    @Override
    @Transactional
    public WishedProduct create(Long userId, Long productId, Integer quantity) {
        validateUserId(userId);
        validateProductId(productId);
        if (wishedProductRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new DuplicateKeyException("이미 장바구니에 존재하는 제품입니다. productId: " + productId);
        }
        var productRef = productRepository.getReferenceById(productId);
        var userRef = userRepository.getReferenceById(userId);
        return wishedProductRepository.save(new WishedProduct(null, userRef, productRef, quantity));
    }

    @Override
    @Transactional
    public void deleteBy(Long userId, Long wishedProductId) {
        findBy(userId, wishedProductId); // 검증을 위해 호출
        wishedProductRepository.deleteById(wishedProductId);
    }

    @Override
    @Transactional
    public void deleteAll(Long userId) {
        validateUserId(userId);
        wishedProductRepository.deleteAllByUserId(userId);
    }

    @Override
    @Transactional
    public Optional<WishedProduct> updateQuantityBy(Long userId, Long wishedProductId, Integer quantity) {
        var existingProduct = findBy(userId, wishedProductId);

        if (quantity == null || quantity <= 0) {
            wishedProductRepository.deleteById(wishedProductId);
            return Optional.empty();
        }
        existingProduct.setQuantity(quantity);
        return Optional.of(wishedProductRepository.save(existingProduct));
    }

    @Override
    @Transactional
    public Optional<WishedProduct> increaseQuantityBy(Long userId, Long wishedProductId, Integer quantity) {
        var existingProduct =  findBy(userId, wishedProductId);
        existingProduct.setQuantity(existingProduct.getQuantity() + quantity);
        return Optional.of(wishedProductRepository.save(existingProduct));
    }

    @Override
    @Transactional
    public Optional<WishedProduct> decreaseQuantityBy(Long userId, Long wishedProductId, Integer quantity) {
        var wishedProduct = findBy(userId, wishedProductId);
        if (wishedProduct.getQuantity() <= quantity) {
            wishedProductRepository.deleteById(wishedProductId);
            return Optional.empty();
        }
        wishedProduct.setQuantity(wishedProduct.getQuantity() - quantity);
        return Optional.of(wishedProductRepository.save(wishedProduct));
    }
}

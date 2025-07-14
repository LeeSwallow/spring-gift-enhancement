package gift.service.wishlist;

import gift.common.model.CustomPage;
import gift.entity.WishedProduct;
import gift.repository.product.ProductRepository;
import gift.repository.user.UserRepository;
import gift.repository.wishlist.WishedProductRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 사용자 ID 입니다. userId: " + userId);
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 사용자입니다. userId: " + userId);
        }
    }

    private void validateProductId(Long productId) {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 제품 ID 입니다. productId: " + productId);
        }
        if (productRepository.findById(productId).isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 제품입니다. productId: " + productId);
        }
    }


    @Override
    @Transactional
    public CustomPage<WishedProduct> getAll(Long userId, int page, int size) {
        validateUserId(userId);

        return wishedProductRepository.findAll(userId, page, size);
    }

    @Override
    @Transactional
    public WishedProduct getByProductId(Long userId, Long productId) {
        validateUserId(userId);
        validateProductId(productId);
        return wishedProductRepository.findById(userId, productId)
                .orElseThrow(() -> new NoSuchElementException("장바구니에 해당 제품이 없습니다. productId: " + productId));
    }

    @Override
    @Transactional
    public WishedProduct addProduct(Long userId, Long productId, Integer quantity) {
        validateUserId(userId);
        validateProductId(productId);
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("새롭게 추가되는 제품의 수량은 1 이상이어야 합니다. productId: " + productId);
        }
        if (wishedProductRepository.findById(userId, productId).isPresent()) {
            throw new DuplicateKeyException("이미 장바구니에 존재하는 제품입니다. productId: " + productId);
        }
        return wishedProductRepository.addProduct(userId, productId, quantity);
    }

    @Override
    @Transactional
    public void removeProduct(Long userId, Long productId) {
        validateUserId(userId);
        validateProductId(productId);
        getByProductId(userId, productId); // 검증을 위해 호출
        if (!wishedProductRepository.removeProduct(userId, productId)) {
            throw new IllegalStateException("장바구니에서 제품을 제거하는데 실패했습니다. " +
                    "userId: " + userId + ", productId: " + productId);
        }
    }

    @Override
    @Transactional
    public void removeAllProducts(Long userId) {
        validateUserId(userId);
        wishedProductRepository.removeAllProducts(userId);
    }

    @Override
    @Transactional
    public Optional<WishedProduct> updateProduct(Long userId, Long productId, Integer quantity) {
        validateUserId(userId);
        validateProductId(productId);
        getByProductId(userId, productId); // 검증을 위해 호출
        if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("업데이트 되는 수량은 0 이상이어야 합니다. productId: " + productId);
        }
        if (quantity <= 0) {
            removeProduct(userId, productId);
            return Optional.empty();
        }
        return Optional.of(wishedProductRepository.updateProduct(userId, productId, quantity));
    }

    @Override
    @Transactional
    public Optional<WishedProduct> increaseProductQuantity(Long userId, Long productId, Integer quantity) {
        validateUserId(userId);
        validateProductId(productId);
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("증가할 수량은 1 이상이어야 합니다. productId: " + productId);
        }
        getByProductId(userId, productId);
        return Optional.of(wishedProductRepository.increaseProductQuantity(userId, productId, quantity));
    }

    @Override
    @Transactional
    public Optional<WishedProduct> decreaseProductQuantity(Long userId, Long productId, Integer quantity) {
        validateUserId(userId);
        validateProductId(productId);
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("감소할 수량은 1 이상이어야 합니다. productId: " + productId);
        }
        WishedProduct wishedProduct = getByProductId(userId, productId);
        if (wishedProduct.getQuantity() <= quantity) {
            removeProduct(userId, productId);
            return Optional.empty();
        }
        return Optional.of(wishedProductRepository.decreaseProductQuantity(userId, productId, quantity));
    }
}

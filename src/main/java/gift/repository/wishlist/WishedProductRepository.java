package gift.repository.wishlist;

import gift.common.model.CustomPage;
import gift.entity.WishedProduct;

import java.util.Optional;

public interface WishedProductRepository {
    CustomPage<WishedProduct> findAll(Long userId, int page, int size);
    Optional<WishedProduct> findById(Long userId, Long productId);
    WishedProduct addProduct(Long userId, Long productId, Integer quantity);
    Boolean removeProduct(Long userId, Long productId);
    void removeAllProducts(Long userId);
    WishedProduct updateProduct(Long userId, Long productId, Integer quantity);
    WishedProduct increaseProductQuantity(Long userId, Long productId, Integer quantity);
    WishedProduct decreaseProductQuantity(Long userId, Long productId, Integer quantity);
    Integer countBy(Long userId);
}

package gift.service.wishlist;

import gift.common.model.CustomPage;
import gift.entity.WishedProduct;

import java.util.Optional;

public interface WishedProductService {
    CustomPage<WishedProduct> getAll(Long userId, int page, int size);
    WishedProduct getByProductId(Long userId, Long productId);
    WishedProduct addProduct(Long userId, Long productId, Integer quantity);
    void removeProduct(Long userId, Long productId);
    void removeAllProducts(Long userId);
    Optional<WishedProduct> updateProduct(Long userId, Long productId, Integer quantity);
    Optional<WishedProduct> increaseProductQuantity(Long userId, Long productId, Integer quantity);
    Optional<WishedProduct> decreaseProductQuantity(Long userId, Long productId, Integer quantity);
}

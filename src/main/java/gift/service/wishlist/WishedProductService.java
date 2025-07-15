package gift.service.wishlist;

import gift.common.model.CustomPage;
import gift.entity.WishedProduct;

import java.util.Optional;

public interface WishedProductService {
    CustomPage<WishedProduct> getAll(Long userId, int page, int size);
    WishedProduct getById(Long userId, Long wishedProductId);
    WishedProduct create(Long userId, Long productId, Integer quantity);
    void delete(Long userId, Long wishedProductId);
    void deleteAll(Long userId);
    Optional<WishedProduct> update(Long userId, Long wishedProductId, Integer quantity);
    Optional<WishedProduct> increaseProductQuantity(Long userId, Long wishedProductId, Integer quantity);
    Optional<WishedProduct> decreaseProductQuantity(Long userId, Long wishedProductId, Integer quantity);
}

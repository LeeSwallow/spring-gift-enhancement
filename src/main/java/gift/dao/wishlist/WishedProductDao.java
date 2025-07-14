package gift.dao.wishlist;

import gift.entity.WishedProduct;
import gift.entity.WishedProductStats;

import java.util.List;
import java.util.Optional;

public interface WishedProductDao {
    @Deprecated
    List<WishedProduct> findAllProduct(Long userId);
    List<WishedProduct> findAllProduct(Long userId, int page, int size);
    Optional<WishedProduct> findById(Long userId, Long productId);
    Integer addProduct(Long userId, Long productId, Integer quantity);
    Integer removeProduct(Long userId, Long productId);
    Integer updateProduct(Long userId, Long productId, Integer quantity);
    Integer increaseProductQuantity(Long userId, Long productId, Integer quantity);
    Integer decreaseProductQuantity(Long userId, Long productId, Integer quantity);
    void clear(Long userId);
    Integer count(Long userId);
    WishedProductStats getWishedProductStats(Long userId);
}

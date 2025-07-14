package gift.dao.product;


import gift.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDao {
    @Deprecated
    List<Product> findAll();
    List<Product> findAll(int page, int size);
    Optional<Product> findById(Long productId);
    Long insertWithKey(Product product);
    Integer update(Product product);
    Integer updateFieldById(Long productId, String fieldName, Object value);
    Integer deleteById(Long productId);
    Integer count();
}

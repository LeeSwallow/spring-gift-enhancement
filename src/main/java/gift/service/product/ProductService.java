package gift.service.product;

import gift.common.model.CustomPage;
import gift.entity.Product;
import gift.entity.UserRole;

public interface ProductService {
    CustomPage<Product> findAllBy(int page, int size);
    Product findById(Long productId);
    Product create(Product product, UserRole role, Long userId);
    Product update(Product product, UserRole role, Long userId);
    void deleteById(Long productId, UserRole role, Long userId);
}

package gift.service.product;

import gift.common.model.CustomPage;
import gift.entity.Product;
import gift.entity.UserRole;

import java.util.List;

public interface ProductService {
    CustomPage<Product> findAllBy(int page, int size);
    CustomPage<Product> findAllBy(int page, int size, List<String> sortBy);
    Product findById(Long productId);
    Product create(Product product, UserRole role, Long userId);
    Product update(Product product, UserRole role, Long userId);
    void deleteById(Long productId, UserRole role, Long userId);
    Boolean existsById(Long productId);
    Product getReference(Long productId);
}

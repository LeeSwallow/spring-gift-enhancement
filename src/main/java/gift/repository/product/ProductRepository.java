package gift.repository.product;

import gift.common.model.CustomPage;
import gift.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    @Deprecated
    List<Product> findAll();
    CustomPage<Product> findAll(int page, int size);
    Optional<Product> findById(Long productId);
    Product save(Product product);
    Product updateFieldById(Long productId, String fieldName, Object value);
    Boolean deleteById(Long productId); // 삭제 메소드 추가
}

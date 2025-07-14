package gift.service.product;

import gift.common.model.CustomAuth;
import gift.common.model.CustomPage;
import gift.entity.Product;

import java.util.List;

public interface ProductService {
    @Deprecated
    List<Product> getAll();
    CustomPage<Product> getBy(int page, int size);
    Product getById(Long productId);
    Product create(Product product, CustomAuth auth);
    Product update(Product product, CustomAuth auth);
    Product patch(Product product, CustomAuth auth);
    void deleteById(Long productId, CustomAuth auth);
}

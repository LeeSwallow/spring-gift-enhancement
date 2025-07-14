package gift.repository.product;

import gift.dao.product.ProductDao;
import gift.common.model.CustomPage;
import gift.entity.Product;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Transactional
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductDao productDao;

    public ProductRepositoryImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    @Deprecated
    public List<Product> findAll() {
        return productDao.findAll();
    }

    public CustomPage<Product> findAll(int page, int size) {
        var builder = CustomPage.builder(
            productDao.findAll(page, size),
            productDao.count())
            .page(page)
            .size(size);

        return builder.build();
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return productDao.findById(productId);
    }

    @Override
    public Product save(Product product) {
        // 제품 ID가 null인 경우 새 제품을 추가
        if (product.getId() == null) {
            Long newId = productDao.insertWithKey(product);
            return productDao.findById(newId)
                    .orElseThrow(() ->
                        new DataRetrievalFailureException("새 제품을 추가하는데 실패했습니다."));
        }

        // 제품 ID가 있는 경우 기존 제품을 업데이트
        if (productDao.update(product) <= 0) {
            throw new DataIntegrityViolationException("제품을 업데이트하는데 실패했습니다.");
        }
        return productDao.findById(product.getId()).orElseThrow(() ->
            new DataRetrievalFailureException("업데이트된 제품을 찾을 수 없습니다."));
    }

    @Override
    public Product updateFieldById(Long productId, String fieldName, Object value) {
        if (fieldName == null || value == null) {
            throw new IllegalArgumentException("필드 이름과 값은 필수입니다.");
        }

        if (productDao.findById(productId).isEmpty()) {
            throw new EmptyResultDataAccessException("업데이트할 제품이 존재하지 않습니다.", 1);
        }

        if (productDao.updateFieldById(productId, fieldName, value) <= 0) {
            throw new DataRetrievalFailureException("제품 필드를 업데이트하는데 실패했습니다.");
        }

        return productDao.findById(productId).orElseThrow(() ->
            new DataRetrievalFailureException("업데이트된 제품을 찾을 수 없습니다."));
    }

    @Override
    public Boolean deleteById(Long productId) {
        if (productDao.findById(productId).isEmpty()) {
            throw new EmptyResultDataAccessException("삭제할 제품이 존재하지 않습니다.", 1);
        }
        if (productDao.deleteById(productId) <= 0) {
            throw new DataRetrievalFailureException("제품 삭제에 실패했습니다.");
        }
        return true;
    }
}

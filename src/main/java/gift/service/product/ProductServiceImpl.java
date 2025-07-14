package gift.service.product;

import gift.common.exception.AccessDeniedException;
import gift.common.model.CustomAuth;
import gift.common.model.CustomPage;
import gift.entity.Product;
import gift.entity.UserRole;
import gift.repository.product.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
public class ProductServiceImpl implements ProductService {
    private final static Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private void validateProduct(Product product, CustomAuth auth) {
        if (auth.role() == UserRole.ROLE_ADMIN) {
            log.info("관리자 권한으로 상품 검증을 건너뜁니다.");
            return;
        }

        if (product == null) {
            log.error("상품 정보가 null 입니다.");
            throw new IllegalArgumentException("상품 정보는 필수입니다.");
        }
        if (product.getOwnerId() == null) {
            log.error("상품 소유자 ID가 null 입니다.");
            throw new IllegalArgumentException("상품 소유자 ID는 필수입니다.");
        }

        if (!product.getOwnerId().equals(auth.userId())){
            log.error("상품 소유자 ID가 인증된 사용자 ID와 일치하지 않습니다. 소유자 ID: {}, 인증된 사용자 ID: {}",
                      product.getOwnerId(), auth.userId());
            throw new AccessDeniedException("상품 소유자 ID가 인증된 사용자 ID와 일치하지 않습니다.");
        }
    }


    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public CustomPage<Product> getBy(int page, int size) {
        return productRepository.findAll(page, size);
    }

    @Override
    public Product getById(Long productId) {
        if (productId == null) {
            log.error("상품 ID가 null 입니다.");
            throw new IllegalArgumentException("상품 ID는 필수입니다.");
        }
        Optional<Product> product = productRepository.findById(productId);

        return product.orElseThrow(() ->
        {
            log.error("Id {}에 해당하는 상품이 존재하지 않습니다.", productId);
            return new NoSuchElementException(
                    String.format("Id %d에 해당하는 상품이 존재하지 않습니다.", productId)
            );
        });
    }

    @Override
    public Product create(Product product, CustomAuth auth) {
        product.setOwnerId(auth.userId());
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product update(Product product, CustomAuth auth) {
        Product updated = getById(product.getId());
        validateProduct(updated, auth);
        if (product.getName() != null) {
            updated.setName(product.getName());
        }

        if (product.getPrice() != null) {
            updated.setPrice(product.getPrice());
        }

        if (product.getImageUrl() != null) {
            updated.setImageUrl(product.getImageUrl());
        }

        return productRepository.save(updated);
    }

    @Override
    @Transactional
    public Product patch(Product product, CustomAuth auth) {
        Product updated = getById(product.getId());
        validateProduct(updated, auth);
        if (product.getName() != null) {
            updated = productRepository.updateFieldById(product.getId(), "name", product.getName());
        }
        if (product.getPrice() != null) {
            updated = productRepository.updateFieldById(product.getId(), "price", product.getPrice());
        }
        if (product.getImageUrl() != null) {
            updated = productRepository.updateFieldById(product.getId(), "image_url", product.getImageUrl());
        }
        return updated;
    }

    @Override
    @Transactional
    public void deleteById(Long productId, CustomAuth auth) {
        Product deleted = getById(productId);
        validateProduct(deleted, auth);

        if (!productRepository.deleteById(productId)) {
            throw new IllegalStateException(
                    String.format("Id %d에 해당하는 상품을 삭제할 수 없습니다.", productId));
        }
    }
}

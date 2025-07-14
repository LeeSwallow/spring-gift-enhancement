package gift.repository.wishlist;

import gift.common.model.CustomPage;
import gift.dao.wishlist.WishedProductDao;
import gift.entity.WishedProduct;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class WishedProductRepositoryImpl implements WishedProductRepository {
    private final WishedProductDao wishedProductDao;

    public WishedProductRepositoryImpl(WishedProductDao wishedProductDao) {
        this.wishedProductDao = wishedProductDao;
    }

    private WishedProduct validateAndGetWishedProduct(Long userId, Long productId, String errorMessage) {
        Optional<WishedProduct> product = wishedProductDao.findById(userId, productId);
        if (product.isEmpty()) {
            throw new DataRetrievalFailureException(errorMessage + " userId: " + userId + ", productId: " + productId);
        }
        return product.get();
    }

    @Override
    @Transactional
    public CustomPage<WishedProduct> findAll(Long userId, int page, int size) {
        var pagedProducts = wishedProductDao.findAllProduct(userId, page, size);
        var stats = wishedProductDao.getWishedProductStats(userId);
        return CustomPage.builder(pagedProducts, stats.count())
                .page(page)
                .size(size)
                .extra("totalPrice",  stats.totalPrice())
                .extra("totalQuantity", stats.totalQuantity())
                .build();
    }

    @Override
    public Optional<WishedProduct> findById(Long userId, Long productId) {
        return wishedProductDao.findById(userId, productId);
    }

    @Override
    @Transactional
    public WishedProduct addProduct(Long userId, Long productId, Integer quantity) {
        var product = wishedProductDao.findById(userId, productId);
        if (product.isPresent()) {
            throw new DuplicateKeyException("이미 장바구니에 존재하는 제품입니다. productId: " + productId);
        }
        Integer result = wishedProductDao.addProduct(userId, productId, quantity);

        if (result <= 0) {
            throw new DataRetrievalFailureException("장바구니에 제품을 추가하는데 실패했습니다. " +
                    "userId: " + userId + ", productId: " + productId);
        }
        return validateAndGetWishedProduct(userId, productId, "추가 후 제품을 찾을 수 없습니다. ");
    }

    @Override
    public Boolean removeProduct(Long userId, Long productId) {
        return wishedProductDao.removeProduct(userId, productId) > 0;
    }

    @Override
    public void removeAllProducts(Long userId) {
        wishedProductDao.clear(userId);
    }

    @Override
    @Transactional
    public WishedProduct updateProduct(Long userId, Long productId, Integer quantity) {
        validateAndGetWishedProduct(userId, productId,"업데이트할 제품이 장바구니에 존재하지 않습니다. ");

        Integer result = wishedProductDao.updateProduct(userId, productId, quantity);
        if (result <= 0) {
            throw new DataRetrievalFailureException("장바구니의 제품을 업데이트하는데 실패했습니다. " +
                    "userId: " + userId + ", productId: " + productId);
        }
        return validateAndGetWishedProduct(userId, productId, "업데이트 후 제품을 찾을 수 없습니다. ");
    }

    @Override
    @Transactional
    public WishedProduct increaseProductQuantity(Long userId, Long productId, Integer quantity) {
        validateAndGetWishedProduct(userId, productId, "수량을 증가시킬 제품이 장바구니에 존재하지 않습니다. ");

        Integer result = wishedProductDao.increaseProductQuantity(userId, productId, quantity);
        if (result <= 0) {
            throw new DataRetrievalFailureException("장바구니의 제품 수량을 증가시키는데 실패했습니다. " +
                    "userId: " + userId + ", productId: " + productId);
        }
        return validateAndGetWishedProduct(userId, productId, "수량 증가 후 제품을 찾을 수 없습니다. ");
    }

    @Override
    @Transactional
    public WishedProduct decreaseProductQuantity(Long userId, Long productId, Integer quantity) {
        validateAndGetWishedProduct(userId, productId, "수량을 감소시킬 제품이 장바구니에 존재하지 않습니다. ");
        Integer result = wishedProductDao.decreaseProductQuantity(userId, productId, quantity);
        if (result <= 0) {
            throw new DataRetrievalFailureException("장바구니의 제품 수량을 감소시키는데 실패했습니다. " +
                    "userId: " + userId + ", productId: " + productId);
        }
        return validateAndGetWishedProduct(userId, productId, "수량 감소 후 제품을 찾을 수 없습니다. ");
    }

    @Override
    public Integer countBy(Long userId) {
        return wishedProductDao.count(userId);
    }
}

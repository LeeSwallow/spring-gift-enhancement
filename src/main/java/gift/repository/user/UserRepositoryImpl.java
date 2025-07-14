package gift.repository.user;

import gift.dao.user.UserDaoImpl;
import gift.entity.User;
import gift.common.model.CustomPage;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserDaoImpl userDao;

    public UserRepositoryImpl(UserDaoImpl userDaoImpl) {
        this.userDao = userDaoImpl;
    }

    @Override
    @Deprecated
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public CustomPage<User> findAll(int page, int size) {
        var builder = CustomPage.builder(
                userDao.findAll(page, size),
                userDao.count())
                .page(page)
                .size(size);

        return builder.build();
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userDao.findById(userId);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public User save(User user) {
        // 사용자 ID가 null인 경우 새 사용자를 추가
        if (user.getId() == null) {
            Long newId = userDao.insertWithKey(user);
            return userDao.findById(newId)
                    .orElseThrow(() ->
                            new DataRetrievalFailureException("새 사용자를 추가하는데 실패했습니다."));
        }
        // 사용자 ID가 있는 경우 기존 사용자를 업데이트
        if (userDao.update(user) <= 0) {
            throw new DataRetrievalFailureException("사용자를 업데이트하는데 실패했습니다.");
        }
        return userDao.findById(user.getId()).orElseThrow(() ->
                new DataRetrievalFailureException("업데이트된 사용자를 찾을 수 없습니다."));
    }

    @Override
    public User updateFieldById(Long userId, String fieldName, Object value) {
        if (fieldName == null || value == null) {
            throw new IllegalArgumentException("필드 이름과 값은 필수입니다.");
        }

        if (userDao.findById(userId).isEmpty()) {
            throw new DataRetrievalFailureException("업데이트할 사용자가 존재하지 않습니다.");
        }

        if (userDao.updateFieldById(userId, fieldName, value) <= 0) {
            throw new DataRetrievalFailureException("사용자 필드를 업데이트하는데 실패했습니다.");
        }
        return userDao.findById(userId).orElseThrow(() ->
                new DataRetrievalFailureException("업데이트된 사용자를 찾을 수 없습니다."));
    }

    @Override
    public Boolean deleteById(Long userId) {
        if (userDao.findById(userId).isEmpty()) {
            throw new DataRetrievalFailureException("삭제할 사용자가 존재하지 않습니다.");
        }
        if (userDao.deleteById(userId) <= 0) {
            throw new DataRetrievalFailureException("사용자를 삭제하는데 실패했습니다.");
        }
        return true;
    }
}

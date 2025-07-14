package gift.dao.user;

import gift.entity.User;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {
    private final JdbcClient jdbcClient;

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
            return new User(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getTimestamp("created_at").toInstant(),
                    rs.getTimestamp("updated_at").toInstant()
            );
        }
    }

    public UserDaoImpl(JdbcClient client) {
        this.jdbcClient = client;
    }

    @Override
    @Deprecated
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcClient.sql(sql)
                .query(new UserRowMapper())
                .stream()
                .toList();
    }

    @Override
    public List<User> findAll(int page, int size) {
        int offset = page * size;
        String sql = "SELECT * FROM users LIMIT ? OFFSET ?";
        return jdbcClient.sql(sql)
                .param(size)
                .param(offset)
                .query(new UserRowMapper())
                .stream()
                .toList();
    }

    @Override
    public Optional<User> findById(Long userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcClient.sql(sql)
                .param(userId)
                .query(new UserRowMapper())
                .optional();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        return jdbcClient.sql(sql)
                .param(email)
                .query(new UserRowMapper())
                .optional();
    }

    @Override
    public Long insertWithKey(User user) {
        String sql = "INSERT INTO users (email, password) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(sql)
                .param(user.getEmail())
                .param(user.getPassword())
                .update(keyHolder);
        if (keyHolder.getKeys() == null || keyHolder.getKeys().get("ID") == null) {
            throw new DataRetrievalFailureException("상품 저장 후 키를 반환받지 못했습니다.");
        }
        Number id = (Number) keyHolder.getKeys().get("ID");
        return id.longValue();
    }

    @Override
    public Integer update(User user) {
        String sql = "UPDATE users SET email = ?, password = ? WHERE id = ?";
        return jdbcClient.sql(sql)
                .param(user.getEmail())
                .param(user.getPassword())
                .param(user.getId())
                .update();
    }

    @Override
    public Integer updateFieldById(Long userId, String fieldName, Object value) {
        if (fieldName == null || value == null) {
            throw new IllegalArgumentException("필드 이름과 값은 필수입니다.");
        }
        String sql = String.format("UPDATE users SET %s = ? WHERE id = ?", fieldName);
        return jdbcClient.sql(sql)
                .param(value)
                .param(userId)
                .update();
    }

    @Override
    public Integer deleteById(Long userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        return jdbcClient.sql(sql)
                .param(userId)
                .update();
    }

    @Override
    public Integer count() {
        String sql = "SELECT COUNT(*) FROM users";
        return jdbcClient.sql(sql)
                .query(Integer.class)
                .single();
    }
}
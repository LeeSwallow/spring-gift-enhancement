package gift.dao.wishlist;

import gift.entity.WishedProduct;
import gift.entity.WishedProductStats;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class WishedProductDaoImpl implements WishedProductDao {
    private final JdbcClient jdbcClient;

    public WishedProductDaoImpl(JdbcClient client) {
        this.jdbcClient = client;
    }

    private static class WishedProductRowMapper implements RowMapper<WishedProduct> {
        @Override
        public WishedProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new WishedProduct(
                    rs.getLong("product_id"),
                    rs.getString("name"),
                    rs.getLong("price"),
                    rs.getString("image_url"),
                    rs.getInt("quantity"),
                    rs.getTimestamp("created_at").toInstant(),
                    rs.getTimestamp("updated_at").toInstant()
            );
        }
    }

    private static class WishedProductStatsRowMapper implements RowMapper<WishedProductStats> {
        @Override
        public WishedProductStats mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new WishedProductStats(
                    rs.getInt("count"),
                    rs.getInt("total_quantity"),
                    rs.getLong("total_price")
            );
        }
    }

    @Override
    @Deprecated
    public List<WishedProduct> findAllProduct(Long userId) {
        String sql = "SELECT w.product_id, p.name, p.price, p.image_url, w.quantity, w.created_at, w.updated_at " +
                     "FROM wished_products w JOIN products p ON w.product_id = p.id " +
                     "WHERE w.user_id = ?";
        return jdbcClient.sql(sql)
                .param(userId)
                .query(new WishedProductRowMapper())
                .stream()
                .toList();
    }

    @Override
    public List<WishedProduct> findAllProduct(Long userId, int page, int size) {
        String sql = "SELECT w.product_id, p.name, p.price, p.image_url, w.quantity, w.created_at, w.updated_at " +
                     "FROM wished_products w JOIN products p ON w.product_id = p.id " +
                     "WHERE w.user_id = ? LIMIT ? OFFSET ?";
        int offset = page * size;
        return jdbcClient.sql(sql)
                .param(userId)
                .param(size)
                .param(offset)
                .query(new WishedProductRowMapper())
                .stream()
                .toList();

    }

    @Override
    public Optional<WishedProduct> findById(Long userId, Long productId) {
        String sql = "SELECT w.product_id, p.name, p.price, p.image_url, w.quantity, w.created_at, w.updated_at " +
                     "FROM wished_products w JOIN products p ON w.product_id = p.id " +
                     "WHERE w.user_id = ? AND w.product_id = ?";
        return jdbcClient.sql(sql)
                .param(userId)
                .param(productId)
                .query(new WishedProductRowMapper())
                .stream()
                .findFirst();
    }

    @Override
    public Integer addProduct(Long userId, Long productId, Integer quantity) {
        String sql = "INSERT INTO wished_products (user_id, product_id, quantity) " +
                     "VALUES (?, ?, ?)";
        return jdbcClient.sql(sql)
                .param(userId)
                .param(productId)
                .param(quantity)
                .update();
    }

    @Override
    public Integer removeProduct(Long userId, Long productId) {
        String sql = "DELETE FROM wished_products WHERE user_id = ? AND product_id = ?";
        return jdbcClient.sql(sql)
                .param(userId)
                .param(productId)
                .update();
    }

    @Override
    public Integer updateProduct(Long userId, Long productId, Integer quantity) {
        String sql = "UPDATE wished_products SET quantity = ? WHERE user_id = ? AND product_id = ?";
        return jdbcClient.sql(sql)
                .param(quantity)
                .param(userId)
                .param(productId)
                .update();
    }

    @Override
    public Integer increaseProductQuantity(Long userId, Long productId, Integer quantity) {
        String sql = "UPDATE wished_products SET quantity = quantity + ? WHERE user_id = ? AND product_id = ?";
        return jdbcClient.sql(sql)
                .param(quantity)
                .param(userId)
                .param(productId)
                .update();
    }

    @Override
    public Integer decreaseProductQuantity(Long userId, Long productId, Integer quantity) {
        String sql = "UPDATE wished_products SET quantity = quantity - ? WHERE user_id = ? AND product_id = ?";
        return jdbcClient.sql(sql)
                .param(quantity)
                .param(userId)
                .param(productId)
                .update();
    }

    @Override
    public void clear(Long userId) {
        String sql = "DELETE FROM wished_products WHERE user_id = ?";
        jdbcClient.sql(sql)
                .param(userId)
                .update();
    }

    @Override
    public Integer count(Long userId) {
        String sql = "SELECT COUNT(*) FROM wished_products WHERE user_id = ?";
        return jdbcClient.sql(sql)
                .param(userId)
                .query(Integer.class)
                .optional()
                .orElse(0);
    }

    @Override
    public WishedProductStats getWishedProductStats(Long userId) {
        String sql = """
                SELECT COUNT(*) AS count,
                       SUM(w.quantity) AS total_quantity,
                       SUM(p.price * w.quantity) AS total_price
                FROM wished_products w
                JOIN products p ON w.product_id = p.id
                WHERE w.user_id = ?
                """;
        return jdbcClient.sql(sql)
                .param(userId)
                .query(new WishedProductStatsRowMapper())
                .single();
    }
}

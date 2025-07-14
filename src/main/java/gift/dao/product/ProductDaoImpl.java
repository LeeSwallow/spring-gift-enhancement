package gift.dao.product;

import gift.entity.Product;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDaoImpl implements ProductDao {
    private final JdbcClient jdbcClient;

    private static class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
            return new Product(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getLong("price"),
                    rs.getString("image_url"),
                    rs.getLong("owner_id"),
                    rs.getTimestamp("created_at").toInstant(),
                    rs.getTimestamp("updated_at").toInstant()
            );
        }
    }

    public ProductDaoImpl(JdbcClient client) {
        this.jdbcClient = client;
    }

    @Override
    @Deprecated
    public List<Product> findAll() {
        String sql = "SELECT * FROM products";
        return jdbcClient.sql(sql)
                .query(new ProductRowMapper())
                .stream()
                .toList();
    }

    @Override
    public List<Product> findAll(int page, int size) {
        int offset = page * size;
        String sql = "SELECT * FROM products LIMIT ? OFFSET ?";
        return jdbcClient.sql(sql)
                .param(size)
                .param(offset)
                .query(new ProductRowMapper())
                .stream()
                .toList();
    }

    @Override
    public Optional<Product> findById(Long productId) {
        String sql = "SELECT * FROM products WHERE id = ?";
        return jdbcClient.sql(sql)
                .param(productId)
                .query(new ProductRowMapper())
                .stream()
                .findFirst();
    }

    @Override
    public Long insertWithKey(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO products (name, price, image_url, owner_id) VALUES (?, ?, ?, ?)";
        jdbcClient.sql(sql)
            .param(product.getName())
            .param(product.getPrice())
            .param(product.getImageUrl())
            .param(product.getOwnerId())
            .update(keyHolder);
       if (keyHolder.getKeys() == null && keyHolder.getKeys().get("ID") == null) {
           throw new DataRetrievalFailureException("상품 저장 후 키를 반환받지 못했습니다.");
        }
       Number id = (Number) keyHolder.getKeys().get("ID");
        return id.longValue();
    }

    public Integer update(Product product) {
        String sql = "UPDATE products SET name = ?, price = ?, image_url = ? WHERE id = ?";
        return jdbcClient.sql(sql)
                .param(product.getName())
                .param(product.getPrice())
                .param(product.getImageUrl())
                .param(product.getId())
                .update();
    }

    public Integer updateFieldById(Long productId, String fieldName, Object value) {
        if (fieldName == null || value == null) {
            throw new IllegalArgumentException("필드 이름과 값은 필수입니다.");
        }
        String sql = "UPDATE products SET " + fieldName + " = ? WHERE id = ?";
        return jdbcClient.sql(sql)
                .param(value)
                .param(productId)
                .update();
    }

    public Integer deleteById(Long productId) {
        String sql = "DELETE FROM products WHERE id = ?";
        return jdbcClient.sql(sql)
                .param(productId)
                .update();
    }

    public Integer count() {
        String sql = "SELECT COUNT(*) FROM products";
        return jdbcClient.sql(sql)
            .query(Integer.class)
            .stream()
            .findFirst().orElseThrow(
                () -> new DataAccessException("상품의 개수를 조회하는 중 오류가 발생했습니다.") {
                }
            );
    }
}

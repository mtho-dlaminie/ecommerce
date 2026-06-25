package co.za.picknpay.assessment.ecommerce.report;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class SalesReportRepository {

    private final JdbcTemplate jdbcTemplate;

    public SalesReportRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TopSellingProductResponse> findTopSellingProducts(
            LocalDateTime from,
            LocalDateTime to,
            int limit
    ) {
        String sql = """
                SELECT
                    p.id AS product_id,
                    p.name AS product_name,
                    SUM(oi.quantity) AS quantity_sold,
                    SUM(oi.quantity * oi.unit_price) AS revenue
                FROM order_items oi
                JOIN products p ON p.id = oi.product_id
                JOIN customer_orders co ON co.id = oi.order_id
                WHERE co.status = 'CONFIRMED'
                  AND co.created_at >= ?
                  AND co.created_at < ?
                GROUP BY p.id, p.name
                ORDER BY quantity_sold DESC
                LIMIT ?
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new TopSellingProductResponse(
                        rs.getLong("product_id"),
                        rs.getString("product_name"),
                        rs.getLong("quantity_sold"),
                        rs.getBigDecimal("revenue")
                ),
                from,
                to,
                limit
        );
    }
}

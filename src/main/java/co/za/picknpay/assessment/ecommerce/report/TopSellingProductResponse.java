package co.za.picknpay.assessment.ecommerce.report;

import java.math.BigDecimal;

public record TopSellingProductResponse(
        Long productId,
        String productName,
        long quantitySold,
        BigDecimal revenue
) {
}

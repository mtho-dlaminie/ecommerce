package co.za.picknpay.assessment.ecommerce.product.dto;

import java.math.BigDecimal;

public  record ProductResponse(
        Long id,
        String name,
        String category,
        BigDecimal price,
        int stockQuantity
) {
}
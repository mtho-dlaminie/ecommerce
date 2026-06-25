package co.za.picknpay.assessment.ecommerce.order.dto;

import co.za.picknpay.assessment.ecommerce.order.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        OrderStatus status,
        LocalDateTime createdAt,
        List<OrderItemResponse> items,
        BigDecimal total
) {
}

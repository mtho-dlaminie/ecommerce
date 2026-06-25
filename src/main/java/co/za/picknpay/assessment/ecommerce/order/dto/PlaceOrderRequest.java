package co.za.picknpay.assessment.ecommerce.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record PlaceOrderRequest(
        @NotEmpty List<@Valid OrderItemRequest> items
) {
}

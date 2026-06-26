package co.za.picknpay.assessment.ecommerce.order;

import co.za.picknpay.assessment.ecommerce.order.dto.OrderItemRequest;
import co.za.picknpay.assessment.ecommerce.order.dto.OrderResponse;
import co.za.picknpay.assessment.ecommerce.order.dto.PlaceOrderRequest;
import co.za.picknpay.assessment.ecommerce.product.Product;
import co.za.picknpay.assessment.ecommerce.product.ProductRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final OrderService service = new OrderService(orderRepository, productRepository);

    @Test
    void placeOrder_shouldReduceStockAndReturnOrderTotal() {
        Product product = new Product("Milk 2L", "Groceries", BigDecimal.valueOf(30.00), 10);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(CustomerOrder.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = service.placeOrder(new PlaceOrderRequest(
                List.of(new OrderItemRequest(1L, 2))
        ));

        assertThat(product.getStockQuantity()).isEqualTo(8);
        assertThat(response.total()).isEqualByComparingTo(BigDecimal.valueOf(60.00));
        assertThat(response.items()).hasSize(1);

        verify(orderRepository).save(any(CustomerOrder.class));
    }

    @Test
    void placeOrder_shouldFailWhenStockIsInsufficient() {
        Product product = new Product("Milk 2L", "Groceries", BigDecimal.valueOf(30.00), 1);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> service.placeOrder(new PlaceOrderRequest(
                List.of(new OrderItemRequest(1L, 2))
        )))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Insufficient stock");

        assertThat(product.getStockQuantity()).isEqualTo(1);

        verify(orderRepository, never()).save(any(CustomerOrder.class));
    }
}

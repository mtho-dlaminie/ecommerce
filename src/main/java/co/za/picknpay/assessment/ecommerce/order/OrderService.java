package co.za.picknpay.assessment.ecommerce.order;

import co.za.picknpay.assessment.ecommerce.order.dto.OrderItemRequest;
import co.za.picknpay.assessment.ecommerce.order.dto.OrderItemResponse;
import co.za.picknpay.assessment.ecommerce.order.dto.OrderResponse;
import co.za.picknpay.assessment.ecommerce.order.dto.PlaceOrderRequest;
import co.za.picknpay.assessment.ecommerce.product.Product;
import co.za.picknpay.assessment.ecommerce.product.ProductRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(
            OrderRepository orderRepository,
            ProductRepository productRepository
    ) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest request) {
        CustomerOrder order = new CustomerOrder(LocalDateTime.now());

        for (OrderItemRequest itemRequest : request.items()) {
            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Product not found: " + itemRequest.productId()
                    ));

            product.reduceStock(itemRequest.quantity());

            OrderItem item = new OrderItem(
                    product,
                    itemRequest.quantity(),
                    product.getPrice()
            );

            order.addItem(item);
        }

        CustomerOrder savedOrder = orderRepository.save(order);

        return toResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public OrderResponse getById(Long orderId) {
        CustomerOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        return toResponse(order);
    }

    @Transactional
    public OrderResponse cancel(Long orderId) {
        CustomerOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        order.cancel();

        for (OrderItem item : order.getItems()) {
            item.getProduct().restoreStock(item.getQuantity());
        }

        return toResponse(order);
    }

    private OrderResponse toResponse(CustomerOrder order) {
        List<OrderItemResponse> items = order.getItems()
                .stream()
                .map(item -> new OrderItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.lineTotal()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getStatus(),
                order.getCreatedAt(),
                items,
                order.total()
        );
    }
}

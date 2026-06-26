package co.za.picknpay.assessment.ecommerce.product;

import co.za.picknpay.assessment.ecommerce.product.dto.CreateProductRequest;
import co.za.picknpay.assessment.ecommerce.product.dto.ProductResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private final ProductRepository repository = mock(ProductRepository.class);
    private final ProductService service = new ProductService(repository);

    @Test
    void create_shouldSaveProductAndReturnResponse() {
        Product savedProduct = new Product("Milk 2L", "Groceries", BigDecimal.valueOf(34.99), 10);

        when(repository.save(any(Product.class))).thenReturn(savedProduct);

        ProductResponse response = service.create(new CreateProductRequest(
                "Milk 2L",
                "Groceries",
                BigDecimal.valueOf(34.99),
                10
        ));

        assertThat(response.name()).isEqualTo("Milk 2L");
        assertThat(response.category()).isEqualTo("Groceries");
        assertThat(response.price()).isEqualByComparingTo(BigDecimal.valueOf(34.99));
        assertThat(response.stockQuantity()).isEqualTo(10);

        verify(repository).save(any(Product.class));
    }
}

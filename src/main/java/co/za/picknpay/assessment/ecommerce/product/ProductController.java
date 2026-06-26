package co.za.picknpay.assessment.ecommerce.product;

import co.za.picknpay.assessment.ecommerce.product.dto.CreateProductRequest;
import co.za.picknpay.assessment.ecommerce.product.dto.ProductResponse;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ProductResponse create(@Valid @RequestBody CreateProductRequest request) {
        return service.create(request);
    }

    @GetMapping
    public Page<ProductResponse> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @ParameterObject Pageable pageable
    ) {
        return service.list(name, category, pageable);
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }
}
package co.za.picknpay.assessment.ecommerce.product;

import co.za.picknpay.assessment.ecommerce.product.dto.CreateProductRequest;
import co.za.picknpay.assessment.ecommerce.product.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public ProductResponse create(CreateProductRequest request) {
        Product product = new Product(
                request.name(),
                request.category(),
                request.price(),
                request.stockQuantity()
        );

        return toResponse(repository.save(product));
    }

    public Page<ProductResponse> list(String name, String category, Pageable pageable) {
        Specification<Product> specification = Specification.where(null);

        if (name != null && !name.isBlank()) {
            specification = specification.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%")
            );
        }

        if (category != null && !category.isBlank()) {
            specification = specification.and((root, query, cb) ->
                    cb.equal(cb.lower(root.get("category")), category.toLowerCase())
            );
        }

        return repository.findAll(specification, pageable)
                .map(this::toResponse);
    }

    public ProductResponse getById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

        return toResponse(product);
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getStockQuantity()
        );
    }
}

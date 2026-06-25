package co.za.picknpay.assessment.ecommerce.report;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SalesReportService {

    private final SalesReportRepository repository;

    public SalesReportService(SalesReportRepository repository) {
        this.repository = repository;
    }

    public List<TopSellingProductResponse> topSellingProducts(
            LocalDateTime from,
            LocalDateTime to,
            int limit
    ) {
        if (!from.isBefore(to)) {
            throw new IllegalArgumentException("'from' must be before 'to'");
        }

        if (limit < 1) {
            throw new IllegalArgumentException("limit must be at least 1");
        }

        return repository.findTopSellingProducts(from, to, limit);
    }
}

package co.za.picknpay.assessment.ecommerce.report;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class SalesReportController {

    private final SalesReportService service;

    public SalesReportController(SalesReportService service) {
        this.service = service;
    }

    @GetMapping("/top-selling-products")
    public List<TopSellingProductResponse> topSellingProducts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return service.topSellingProducts(from, to, limit);
    }
}

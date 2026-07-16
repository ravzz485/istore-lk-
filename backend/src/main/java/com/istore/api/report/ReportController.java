package com.istore.api.report;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('STAFF','ADMIN')")   // ⭐ class level — ඔක්කොම endpoints protected!
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        return reportService.dashboardKpis();
    }

    @GetMapping("/revenue/monthly")
    public List<Map> monthlyRevenue(@RequestParam(required = false) Integer year) {
        return reportService.monthlyRevenue(year != null ? year : Year.now().getValue());
    }

    @GetMapping("/sales/by-category")
    public List<Map> salesByCategory() {
        return reportService.salesByCategory();
    }
}
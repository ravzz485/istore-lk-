package com.istore.api.report;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final MongoTemplate mongoTemplate;

    // ── KPI cards ──
    public Map<String, Object> dashboardKpis() {

        long totalOrders = mongoTemplate.count(new Query(), "orders");

        long pendingOrders = mongoTemplate.count(
                new Query(Criteria.where("status").is("PENDING")), "orders");

        long totalCustomers = mongoTemplate.count(
                new Query(Criteria.where("role").is("CUSTOMER")), "users");

        // ⭐ Aggregation #1 — CANCELLED නොවුනු orders වල total revenue
        Aggregation revenueAgg = newAggregation(
                match(Criteria.where("status").ne("CANCELLED")),
                group().sum("total").as("revenue"));

        AggregationResults<Map> revenueResult =
                mongoTemplate.aggregate(revenueAgg, "orders", Map.class);

        Object revenue = revenueResult.getUniqueMappedResult() != null
                ? revenueResult.getUniqueMappedResult().get("revenue")
                : 0;

        return Map.of(
                "totalRevenue", revenue,
                "totalOrders", totalOrders,
                "pendingOrders", pendingOrders,
                "totalCustomers", totalCustomers);
    }

    // ── Monthly revenue (bar chart data) ──
    public List<Map> monthlyRevenue(int year) {

        // ⭐ Aggregation #2 — pipeline stages 4ක්:
        Aggregation agg = newAggregation(
                // 1. CANCELLED අයින්
                match(Criteria.where("status").ne("CANCELLED")),
                // 2. month field එකක් project කරනවා (createdAt date එකෙන්)
                project("total")
                        .andExpression("month(createdAt)").as("month")
                        .andExpression("year(createdAt)").as("year"),
                // 3. year filter
                match(Criteria.where("year").is(year)),
                // 4. month අනුව group + sum + sort
                group("month").sum("total").as("revenue")
                        .count().as("orders"),
                sort(org.springframework.data.domain.Sort.Direction.ASC, "_id"));

        return mongoTemplate.aggregate(agg, "orders", Map.class).getMappedResults();
    }

    // ── Category sales mix (donut chart data) ──
    public List<Map> salesByCategory() {

        // ⭐ Aggregation #3 — unwind: items array එක "දිග ඇරලා" line-by-line
        Aggregation agg = newAggregation(
                match(Criteria.where("status").ne("CANCELLED")),
                unwind("items"),                          // order 1ක items 3ක් → rows 3ක්
                lookup("products", "items.productId", "_id", "product"),
                unwind("product"),
                group("product.category")
                        .sum("items.lineTotal").as("revenue")
                        .sum("items.qty").as("units"),
                sort(org.springframework.data.domain.Sort.Direction.DESC, "revenue"));

        return mongoTemplate.aggregate(agg, "orders", Map.class).getMappedResults();
    }
}
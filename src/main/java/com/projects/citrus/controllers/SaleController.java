package com.projects.citrus.controllers;

import com.projects.citrus.dto.requests.SaleRequest;
import com.projects.citrus.dto.responses.SaleResponse;
import com.projects.citrus.services.interfaces.ISaleService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/harvests/{harvestId}/sales")
@RequiredArgsConstructor
@Tag(name = "Sale Management", description = "Endpoints for managing sales")
public class SaleController {
    private final ISaleService saleService;

    private Map<String, Object> getResponseWithMessage(Object data, String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", message);
        if (data != null) {
            response.put("data", data);
        }
        return response;
    }

    @PostMapping
    @Operation(summary = "Create a new sale for harvest")
    public ResponseEntity<Map<String, Object>> create(
            @PathVariable Long harvestId,
            @Valid @RequestBody SaleRequest request
    ) {
        SaleResponse sale = saleService.create(harvestId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(getResponseWithMessage(sale, "Sale created successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get sale by ID")
    public ResponseEntity<Map<String, Object>> getById(
            @PathVariable Long harvestId,
            @PathVariable Long id
    ) {
        SaleResponse sale = saleService.getById(id);
        return ResponseEntity.ok(getResponseWithMessage(sale, "Sale retrieved successfully"));
    }

    @GetMapping
    @Operation(summary = "Get all sales for harvest")
    public ResponseEntity<Map<String, Object>> getByHarvestId(@PathVariable Long harvestId) {
        List<SaleResponse> sales = saleService.getByHarvestId(harvestId);
        return ResponseEntity.ok(getResponseWithMessage(sales, "Sales retrieved successfully"));
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get sales by date range")
    public ResponseEntity<Map<String, Object>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<SaleResponse> sales = saleService.getBySaleDateRange(startDate, endDate);
        return ResponseEntity.ok(getResponseWithMessage(sales, "Sales retrieved successfully"));
    }

    @GetMapping("/revenue")
    @Operation(summary = "Calculate total revenue for harvest")
    public ResponseEntity<Map<String, Object>> calculateTotalRevenue(@PathVariable Long harvestId) {
        Double revenue = saleService.calculateTotalRevenue(harvestId);
        return ResponseEntity.ok(getResponseWithMessage(revenue, "Total revenue calculated successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing sale")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable Long harvestId,
            @PathVariable Long id,
            @Valid @RequestBody SaleRequest request
    ) {
        SaleResponse sale = saleService.update(id, request);
        return ResponseEntity.ok(getResponseWithMessage(sale, "Sale updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a sale")
    public ResponseEntity<Map<String, Object>> delete(
            @PathVariable Long harvestId,
            @PathVariable Long id
    ) {
        saleService.delete(id);
        return ResponseEntity.ok(getResponseWithMessage(null, "Sale deleted successfully"));
    }
}

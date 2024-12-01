package com.projects.citrus.controllers;

import com.projects.citrus.dto.requests.HarvestRequest;
import com.projects.citrus.dto.responses.HarvestResponse;
import com.projects.citrus.services.interfaces.IHarvestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/api/harvests")
@RequiredArgsConstructor
@Tag(name = "Harvest Management", description = "Endpoints for managing harvests")
public class HarvestController {
    private final IHarvestService harvestService;

    private Map<String, Object> getResponseWithMessage(Object data, String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", message);
        if (data != null) {
            response.put("data", data);
        }
        return response;
    }

    @PostMapping
    @Operation(summary = "Create a new harvest")
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody HarvestRequest request) {
        HarvestResponse harvest = harvestService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(getResponseWithMessage(harvest, "Harvest created successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get harvest by ID")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
        HarvestResponse harvest = harvestService.getById(id);
        return ResponseEntity.ok(getResponseWithMessage(harvest, "Harvest retrieved successfully"));
    }

    @GetMapping
    @Operation(summary = "Get all harvests")
    public ResponseEntity<Map<String, Object>> getAll() {
        List<HarvestResponse> harvests = harvestService.getAll();
        return ResponseEntity.ok(getResponseWithMessage(harvests, "Harvests retrieved successfully"));
    }

    @GetMapping("/by-date-range")
    @Operation(summary = "Get harvests by date range")
    public ResponseEntity<Map<String, Object>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<HarvestResponse> harvests = harvestService.getHarvestsByDateRange(startDate, endDate);
        return ResponseEntity.ok(getResponseWithMessage(harvests, "Harvests retrieved successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing harvest")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable Long id,
            @Valid @RequestBody HarvestRequest request
    ) {
        HarvestResponse harvest = harvestService.update(id, request);
        return ResponseEntity.ok(getResponseWithMessage(harvest, "Harvest updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a harvest")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        harvestService.delete(id);
        return ResponseEntity.ok(getResponseWithMessage(null, "Harvest deleted successfully"));
    }

    @GetMapping("/{id}/is-sold")
    @Operation(summary = "Check if harvest is sold")
    public ResponseEntity<Map<String, Object>> checkIfSold(@PathVariable Long id) {
        boolean isSold = harvestService.isSold(id);
        return ResponseEntity.ok(getResponseWithMessage(isSold, "Harvest sold status retrieved successfully"));
    }

    @GetMapping("/{id}/total-quantity")
    @Operation(summary = "Get total quantity of harvest")
    public ResponseEntity<Map<String, Object>> getTotalQuantity(@PathVariable Long id) {
        double totalQuantity = harvestService.calculateTotalQuantity(id);
        return ResponseEntity.ok(getResponseWithMessage(totalQuantity, "Harvest total quantity calculated successfully"));
    }
}

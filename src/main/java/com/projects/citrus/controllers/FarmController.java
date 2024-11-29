package com.projects.citrus.controllers;

import com.projects.citrus.dto.requests.FarmRequest;
import com.projects.citrus.dto.responses.FarmResponse;
import com.projects.citrus.services.interfaces.IFarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/farms")
@RequiredArgsConstructor
@Tag(name = "Farm Management", description = "Endpoints for managing farms")
public class FarmController {
    private final IFarmService farmService;

    @PostMapping
    @Operation(summary = "Create a new farm")
    public ResponseEntity<FarmResponse> create(@Valid @RequestBody FarmRequest request)
    {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(farmService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing farm")
    public ResponseEntity<FarmResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody FarmRequest request
    ) {
        return ResponseEntity.ok(farmService.update(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get farm by ID")
    public ResponseEntity<FarmResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(farmService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all farms")
    public ResponseEntity<List<FarmResponse>> getAll() {
        return ResponseEntity.ok(farmService.getAll());
    }

    @GetMapping("/{id}/calculate-left-area")
    @Operation(summary = "Calculate remaining area in a farm")
    public ResponseEntity<Double> calculateLeftArea(@PathVariable Long id) {
        return ResponseEntity.ok(farmService.calculateLeftArea(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a farm")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        farmService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-min-area/{minArea}")
    @Operation(summary = "Get farms by minimum area")
    public ResponseEntity<List<FarmResponse>> getByMinArea(
            @PathVariable Double minArea
    ) {
        return ResponseEntity.ok(farmService.getByMinArea(minArea));
    }

    @GetMapping("/by-date-range")
    @Operation(summary = "Get farms by creation date range")
    public ResponseEntity<List<FarmResponse>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(farmService.getByDateRange(startDate, endDate));
    }
}
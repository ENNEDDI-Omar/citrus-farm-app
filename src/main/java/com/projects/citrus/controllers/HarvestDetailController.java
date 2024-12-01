package com.projects.citrus.controllers;

import com.projects.citrus.dto.responses.HarvestDetailResponse;
import com.projects.citrus.dto.requests.HarvestDetailRequest;
import com.projects.citrus.dto.responses.TreeResponse;
import com.projects.citrus.services.interfaces.IHarvestDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/harvests/{harvestId}/details")
@RequiredArgsConstructor
@Tag(name = "Harvest Detail Management", description = "Endpoints for managing harvest details")
public class HarvestDetailController {
    private final IHarvestDetailService harvestDetailService;

    private Map<String, Object> getResponseWithMessage(Object data, String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", message);
        if (data != null) {
            response.put("data", data);
        }
        return response;
    }

    @PostMapping
    @Operation(summary = "Add a tree to harvest")
    public ResponseEntity<Map<String, Object>> create(
            @PathVariable Long harvestId,
            @Valid @RequestBody HarvestDetailRequest request
    ) {
        HarvestDetailResponse detail = harvestDetailService.create(harvestId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(getResponseWithMessage(detail, "Tree added to harvest successfully"));
    }

    @GetMapping
    @Operation(summary = "Get all details for a harvest")
    public ResponseEntity<Map<String, Object>> getAllByHarvestId(@PathVariable Long harvestId) {
        List<HarvestDetailResponse> details = harvestDetailService.getAllByHarvestId(harvestId);
        return ResponseEntity.ok(getResponseWithMessage(details, "Harvest details retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get harvest detail by ID")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long harvestId, @PathVariable Long id) {
        HarvestDetailResponse detail = harvestDetailService.getById(id);
        return ResponseEntity.ok(getResponseWithMessage(detail, "Harvest detail retrieved successfully"));
    }

    @GetMapping("/available-trees")
    @Operation(summary = "Get list of trees available for harvest")
    public ResponseEntity<Map<String, Object>> getAvailableTreesForHarvest(@PathVariable Long harvestId) {
        List<TreeResponse> availableTrees = harvestDetailService.getAvailableTreesForHarvest(harvestId);
        return ResponseEntity.ok(getResponseWithMessage(availableTrees, "Available trees retrieved successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove tree from harvest")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long harvestId, @PathVariable Long id) {
        harvestDetailService.delete(id);
        return ResponseEntity.ok(getResponseWithMessage(null, "Tree removed from harvest successfully"));
    }
}

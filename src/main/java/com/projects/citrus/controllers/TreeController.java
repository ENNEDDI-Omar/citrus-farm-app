package com.projects.citrus.controllers;

import com.projects.citrus.dto.requests.TreeRequest;
import com.projects.citrus.dto.responses.TreeResponse;
import com.projects.citrus.services.interfaces.ITreeService;
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
@RequestMapping("/api/trees")
@RequiredArgsConstructor
@Tag(name = "Tree Management", description = "Endpoints for managing trees")
public class TreeController {
    private final ITreeService treeService;

    private Map<String, Object> getResponseWithMessage(Object data, String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", message);
        if (data != null) {
            response.put("data", data);
        }
        return response;
    }

    @PostMapping
    @Operation(summary = "Create a new tree")
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody TreeRequest request) {
        TreeResponse tree = treeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(getResponseWithMessage(tree, "Tree planted successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tree by ID")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
        TreeResponse tree = treeService.getById(id);
        return ResponseEntity.ok(getResponseWithMessage(tree, "Tree retrieved successfully"));
    }

    @GetMapping
    @Operation(summary = "Get all trees")
    public ResponseEntity<Map<String, Object>> getAll() {
        List<TreeResponse> trees = treeService.getAll();
        return ResponseEntity.ok(getResponseWithMessage(trees, "Trees retrieved successfully"));
    }

    @GetMapping("/field/{fieldId}")
    @Operation(summary = "Get trees by field ID")
    public ResponseEntity<Map<String, Object>> getByFieldId(@PathVariable Long fieldId) {
        List<TreeResponse> trees = treeService.getByFieldId(fieldId);
        return ResponseEntity.ok(getResponseWithMessage(trees, "Trees for field retrieved successfully"));
    }

    @GetMapping("/field/{fieldId}/count")
    @Operation(summary = "Get tree count in field")
    public ResponseEntity<Map<String, Object>> getTreeCountInField(@PathVariable Long fieldId) {
        long count = treeService.getTreeCountInField(fieldId);
        return ResponseEntity.ok(getResponseWithMessage(count, "Tree count retrieved successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing tree")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable Long id,
            @Valid @RequestBody TreeRequest request
    ) {
        TreeResponse tree = treeService.update(id, request);
        return ResponseEntity.ok(getResponseWithMessage(tree, "Tree updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a tree")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        treeService.delete(id);
        return ResponseEntity.ok(getResponseWithMessage(null, "Tree deleted successfully"));
    }
}

package com.projects.citrus.controllers;

import com.projects.citrus.dto.requests.FieldRequest;
import com.projects.citrus.dto.responses.FieldResponse;
import com.projects.citrus.services.interfaces.IFieldService;
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
@RequestMapping("/api/fields")
@RequiredArgsConstructor
@Tag(name = "Field Management", description = "Endpoints for managing fields")
public class FieldController {
    private final IFieldService fieldService;

    private Map<String, Object> getResponseWithMessage(Object data, String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", message);
        if (data != null) {
            response.put("data", data);
        }
        return response;
    }

    @PostMapping
    @Operation(summary = "Create a new field")
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody FieldRequest request) {
        FieldResponse field = fieldService.createField(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(getResponseWithMessage(field, "Field created successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing field")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable Long id,
            @Valid @RequestBody FieldRequest request
    ) {
        FieldResponse field = fieldService.updateField(id, request);
        return ResponseEntity.ok(getResponseWithMessage(field, "Field updated successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get field by ID")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
        FieldResponse field = fieldService.getFieldById(id);
        return ResponseEntity.ok(getResponseWithMessage(field, "Field retrieved successfully"));
    }

    @GetMapping("/with-trees/{id}")
    @Operation(summary = "Get field with its trees")
    public ResponseEntity<Map<String, Object>> getFieldWithTrees(@PathVariable Long id) {
        FieldResponse field = fieldService.getFieldWithTrees(id);
        return ResponseEntity.ok(getResponseWithMessage(field, "Field with trees retrieved successfully"));
    }

    @GetMapping
    @Operation(summary = "Get all fields")
    public ResponseEntity<Map<String, Object>> getAll() {
        List<FieldResponse> fields = fieldService.getAllFields();
        return ResponseEntity.ok(getResponseWithMessage(fields, "Fields retrieved successfully"));
    }

    @GetMapping("/farm/{farmId}")
    @Operation(summary = "Get fields by farm ID")
    public ResponseEntity<Map<String, Object>> getByFarmId(@PathVariable Long farmId) {
        List<FieldResponse> fields = fieldService.getFieldsByFarmId(farmId);
        return ResponseEntity.ok(getResponseWithMessage(fields, "Fields for farm retrieved successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a field")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        fieldService.deleteField(id);
        return ResponseEntity.ok(getResponseWithMessage(null, "Field deleted successfully"));
    }
}

package org.example.controller;

import org.example.dto.VariantRequestDto;
import org.example.dto.VariantResponseDto;
import org.example.service.VariantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
@RequestMapping("/api/v1/variants")
@RequiredArgsConstructor
@Tag(name = "Variant Generator", description = "API для генерації PDF завдань")
public class VariantController {

    private final VariantService variantService;

    @PostMapping("/generate")
    @Operation(summary = "Генерує новий варіант завдання та експортує у хмару")
    public ResponseEntity<?> generateVariant(@Valid @RequestBody VariantRequestDto request) {
        try {
            VariantResponseDto response = variantService.generateVariant(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
}
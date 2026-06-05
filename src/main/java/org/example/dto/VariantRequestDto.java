package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record VariantRequestDto(
        @NotBlank(message = "Template ID is mandatory") String templateId,
        @NotBlank(message = "Student name is mandatory") String studentName,
        @NotNull(message = "Seed is mandatory") Integer seed,
        @NotNull(message = "Parameters cannot be null") Map<String, Integer> parameters,
        boolean cloudSync
) {}
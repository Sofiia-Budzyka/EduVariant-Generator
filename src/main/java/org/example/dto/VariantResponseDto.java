package org.example.dto;

import java.time.Instant;
import java.util.Map;

public record VariantResponseDto(
        String status,
        String variantId,
        Instant createdAt,
        String pdfPreviewUrl,
        Map<String, String> cloudStorage
) {}
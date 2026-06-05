package org.example.service;

import com.eduvariant.dto.VariantRequestDto;
import com.eduvariant.dto.VariantResponseDto;
import com.eduvariant.exception.CloudSyncException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
public class VariantService {

    public org.example.dto.VariantResponseDto generateVariant(org.example.dto.@Valid @org.jetbrains.annotations.UnknownNullability VariantRequestDto request) {
        Integer min = request.parameters().getOrDefault("min_value", 0);
        Integer max = request.parameters().getOrDefault("max_value", 0);
        if (min > max) {
            throw new IllegalArgumentException("min_value cannot be greater than max_value");
        }

        String variantId = "var-" + UUID.randomUUID().toString().substring(0, 8);

        if (request.cloudSync() && "expired_token".equals(request.templateId())) {
            throw new CloudSyncException("Google Drive OAuth token expired");
        }

        return new VariantResponseDto(
                "success",
                variantId,
                Instant.now(),
                "https://api.eduvariant.com/v1/storage/" + variantId + ".pdf",
                Map.of(
                        "provider", "google_drive",
                        "sync_status", "completed",
                        "file_id", "gdrive_" + variantId
                )
        );
    }
}
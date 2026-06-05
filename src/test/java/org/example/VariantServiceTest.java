package org.example;

import org.example.dto.VariantRequestDto;
import org.example.dto.VariantResponseDto;
import org.example.exception.CloudSyncException;
import org.example.service.VariantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class VariantServiceTest {

    private VariantService variantService;

    @BeforeEach
    void setUp() {
        variantService = new VariantService();
    }

    @Test
    void testSuccessfulGenerationReturnsValidResponse() {
        VariantRequestDto request = new VariantRequestDto(
                "math-geometry-60deg", "Sofiia Budzyka", 42109,
                Map.of("min_value", 10, "max_value", 50), true
        );

        VariantResponseDto response = variantService.generateVariant(request);

        assertNotNull(response);
        assertEquals("success", response.status());
        assertTrue(response.variantId().startsWith("var-"));
        assertEquals("google_drive", response.cloudStorage().get("provider"));
    }

    @Test
    void testGenerateThrowsExceptionWhenMinValueGreaterThanMaxValue() {
        VariantRequestDto request = new VariantRequestDto(
                "math-geometry", "Test User", 123,
                Map.of("min_value", 100, "max_value", 10), true // min > max!
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            variantService.generateVariant(request);
        });

        assertEquals("min_value cannot be greater than max_value", exception.getMessage());
    }

    @Test
    void testGenerateThrowsCloudSyncExceptionOnExpiredToken() {
        VariantRequestDto request = new VariantRequestDto(
                "expired_token", "Test User", 123,
                Map.of("min_value", 10, "max_value", 50), true
        );

        CloudSyncException exception = assertThrows(CloudSyncException.class, () -> {
            variantService.generateVariant(request);
        });

        assertEquals("Google Drive OAuth token expired", exception.getMessage());
    }
}
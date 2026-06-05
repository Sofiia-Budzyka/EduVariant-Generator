package org.example;

import org.example.dto.VariantRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class VariantControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSuccessfulGenerationEndpoint() throws Exception {
        VariantRequestDto request = new VariantRequestDto(
                "math-geometry-60deg", "Sofiia Budzyka", 42109,
                Map.of("min_value", 10, "max_value", 50), true
        );

        mockMvc.perform(post("/api/v1/variants/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.variantId").exists())
                .andExpect(jsonPath("$.cloudStorage.sync_status").value("completed"));
    }

    @Test
    void testValidationErrorMissingNameEndpoint() throws Exception {
        VariantRequestDto request = new VariantRequestDto(
                "math-geometry-60deg", "", 42109, // Пусте ім'я
                Map.of("min_value", 10, "max_value", 50), true
        );

        mockMvc.perform(post("/api/v1/variants/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.studentName").value("Student name is mandatory"));
    }
    @Test
    void testCloudSyncFailureEndpoint() throws Exception {
        VariantRequestDto request = new VariantRequestDto(
                "expired_token", "Sofiia Budzyka", 42109,
                Map.of("min_value", 10, "max_value", 50), true
        );

        mockMvc.perform(post("/api/v1/variants/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.sync_status").value("failed"))
                .andExpect(jsonPath("$.message").value("Google Drive OAuth token expired"));
    }
}
package com.project.bankapi.controller;

import com.project.bankapi.dto.response.HealthResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@Slf4j
@RestController
@RequestMapping("/api/v1/health")
public class HealthControllerApi {

    @GetMapping
    public ResponseEntity<HealthResponse> health() {
        log.debug("Health check requested");

        HealthResponse healthResponse = new HealthResponse(
                "UP",
                OffsetDateTime.now()
        );

        return ResponseEntity.ok(healthResponse);
    }
}

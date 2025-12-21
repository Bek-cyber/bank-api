package com.project.bankapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;

/**
 * DTO для health-check ответа.
 * Явный контракт → удобно для мониторинга и клиентов.
 */
@Getter
@AllArgsConstructor
public class HealthResponse {
    private String status;
    private OffsetDateTime timestamp;
}

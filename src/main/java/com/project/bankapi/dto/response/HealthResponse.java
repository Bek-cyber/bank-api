package com.project.bankapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class HealthResponse {
    private String status;
    private OffsetDateTime timestamp;
}

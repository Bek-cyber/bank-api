package com.project.bankapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO ответа по счету.
 */
@Getter
@AllArgsConstructor
public class AccountResponse {
    private UUID id;
    private BigDecimal balance;
    private String status;
}

package com.project.bankapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class AccountResponse {
    private UUID id;
    private BigDecimal balance;
    private String status;
}

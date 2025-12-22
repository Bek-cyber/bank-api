package com.project.bankapi.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class DepositRequest {
    @NotNull
    @Positive
    private BigDecimal amount;
}

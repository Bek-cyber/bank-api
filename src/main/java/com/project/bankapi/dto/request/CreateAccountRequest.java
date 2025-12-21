package com.project.bankapi.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CreateAccountRequest {

    @NotNull
    @PositiveOrZero
    private BigDecimal initialBalance;
}

package com.project.bankapi.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * DTO запроса на создание счета.
 */
@Getter
public class CreateAccountRequest {

    /**
     * Начальный баланс.
     * Может быть 0, но не отрицательный.
     */
    @NotNull
    @PositiveOrZero
    private BigDecimal initialBalance;
}

package com.project.bankapi.exception;

public class InvalidInitialBalanceException extends ApiException {
    public InvalidInitialBalanceException() {
        super(
                ErrorCode.VALIDATION_ERROR,
                "Начальный баланс не может быть отрицательным"
        );
    }
}

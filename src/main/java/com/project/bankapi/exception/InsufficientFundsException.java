package com.project.bankapi.exception;

public class InsufficientFundsException extends ApiException {
    public InsufficientFundsException() {
        super(
                ErrorCode.VALIDATION_ERROR,
                "Недостаточно средств на счете"
        );
    }
}

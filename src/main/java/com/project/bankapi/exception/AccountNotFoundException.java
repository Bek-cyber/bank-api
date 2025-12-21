package com.project.bankapi.exception;

public class AccountNotFoundException extends ApiException {

    public AccountNotFoundException(String accountId) {
        super(
                ErrorCode.ACCOUNT_NOT_FOUND,
                "Счет не найден: " + accountId
        );
    }
}

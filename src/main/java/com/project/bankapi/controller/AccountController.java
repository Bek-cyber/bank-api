package com.project.bankapi.controller;

import com.project.bankapi.dto.request.CreateAccountRequest;
import com.project.bankapi.dto.response.AccountResponse;
import com.project.bankapi.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request
    ) {
        log.debug("HTTP POST /accounts");

        AccountService.Account account = accountService.createAccount(request.getInitialBalance());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(account));
    }

    private AccountResponse toResponse(AccountService.Account account) {
        return new AccountResponse(
                account.id(),
                account.balance(),
                account.status()
        );
    }
}

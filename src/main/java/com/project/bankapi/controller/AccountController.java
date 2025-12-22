package com.project.bankapi.controller;

import com.project.bankapi.domain.entity.Account;
import com.project.bankapi.dto.request.CreateAccountRequest;
import com.project.bankapi.dto.request.WithdrawRequest;
import com.project.bankapi.dto.response.AccountResponse;
import com.project.bankapi.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<Void> withdraw(
            @PathVariable UUID id,
            @RequestBody @Valid WithdrawRequest request
    ) {
        log.debug("HTTP PATCH /accounts/{}/withdraw", id);

        accountService.withdraw(id, request.getAmount());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable("id") UUID id) {
        log.debug("HTTP GET /accounts/{}", id);

        Account account = accountService.getAccountById(id);

        return ResponseEntity.ok(toResponse(account));
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request
    ) {
        log.debug("HTTP POST /accounts");

        Account account = accountService.createAccount(request.getInitialBalance());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(account));
    }

    private AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getBalance(),
                account.getStatus()
        );
    }
}

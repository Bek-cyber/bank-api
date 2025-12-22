package com.project.bankapi.controller;

import com.project.bankapi.domain.entity.Account;
import com.project.bankapi.dto.request.CreateAccountRequest;
import com.project.bankapi.dto.request.DepositRequest;
import com.project.bankapi.dto.request.WithdrawRequest;
import com.project.bankapi.dto.response.AccountResponse;
import com.project.bankapi.service.AccountService;
import com.project.bankapi.service.IdempotencyKeyService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final IdempotencyKeyService idempotencyService;

    @PatchMapping("{id}/deposit")
    public ResponseEntity<?> deposit(
            @PathVariable UUID id,
            @RequestHeader("Idempotency-key") String idemKey,
            @Valid @RequestBody DepositRequest request,
            HttpServletRequest servletRequest
    ) {
        String endpoint = servletRequest.getRequestURI();

        return idempotencyService
                .findExisting(idemKey, endpoint)
                .map(sorted -> {
                    log.info("Повторный idempotent запрос (deposit). key={}", idemKey);
                    return ResponseEntity
                            .status(sorted.getResponseStatus())
                            .body(sorted.getResponseBody());
                })
                .orElseGet(() -> {
                    accountService.deposit(id, request.getAmount());

                    idempotencyService.saveResponse(
                            idemKey,
                            endpoint,
                            request.toString(),
                            HttpStatus.NO_CONTENT.value(),
                            null
                    );

                    return ResponseEntity.noContent().build();
                });
    }

    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<?> withdraw(
            @PathVariable UUID id,
            @RequestHeader("Idempotency-Key") String idemKey,
            @Valid @RequestBody WithdrawRequest request,
            HttpServletRequest servletRequest) {
        String endpoint = servletRequest.getRequestURI();

        return idempotencyService
                .findExisting(idemKey, endpoint)
                .map(stored -> {
                    log.info("Повторный idempotent запрос. key={}", idemKey);
                    return ResponseEntity
                            .status(stored.getResponseStatus())
                            .body(stored.getResponseBody());
                })
                .orElseGet(() -> {
                    accountService.withdraw(id, request.getAmount());

                    idempotencyService.saveResponse(
                            idemKey,
                            endpoint,
                            request.toString(),
                            HttpStatus.NO_CONTENT.value(),
                            null
                    );

                    return ResponseEntity.noContent().build();
                });
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

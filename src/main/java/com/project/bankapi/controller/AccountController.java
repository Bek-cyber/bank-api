package com.project.bankapi.controller;

import com.project.bankapi.dto.request.CreateAccountRequest;
import com.project.bankapi.dto.response.AccountResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request
    ) {
        log.info("Создание счета. initialBalance={}", request.getInitialBalance());

        AccountResponse response = new AccountResponse(
                UUID.randomUUID(),
                request.getInitialBalance(),
                "ACTIVE"
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}

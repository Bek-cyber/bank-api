package com.project.bankapi.service;

import com.project.bankapi.dto.request.CreateAccountRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
public class AccountService {

    public Account createAccount(BigDecimal initialBalance) {
        log.info("Создание счета в сервисе. initialBalance={}", initialBalance);

        Account account = new Account(
                UUID.randomUUID(),
                initialBalance,
                "ACTIVE"
        );

        log.debug("Счет успешно создан. accountId={}", account.id());
        return account;
    }

    public record Account(
            UUID id,
            BigDecimal balance,
            String status
    ) {}
}


package com.project.bankapi.service;

import com.project.bankapi.domain.entity.Account;
import com.project.bankapi.domain.repository.AccountRepository;
import com.project.bankapi.exception.AccountNotFoundException;
import com.project.bankapi.exception.InvalidInitialBalanceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository repository;

    @Transactional(readOnly = true)
    public Account getAccountById(UUID accountId) {
        log.info("Запрос счета. accountId={}", accountId);

        return repository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId.toString()));
    }

    @Transactional
    public Account createAccount(BigDecimal initialBalance) {
        log.info("Создание счета в сервисе. initialBalance={}", initialBalance);

        if (initialBalance.signum() < 0) {
            log.warn("Попытка создать счет с отрицательным балансом: {}", initialBalance);
            throw new InvalidInitialBalanceException();
        }

        Account account = Account.builder()
                .balance(initialBalance)
                .status("ACTIVE")
                .build();

        Account saved = repository.save(account);

        log.info("Счет сохранён. accountId={}", saved.getId());
        return saved;
    }
}


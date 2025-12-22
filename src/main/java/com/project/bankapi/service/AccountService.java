package com.project.bankapi.service;

import com.project.bankapi.domain.entity.Account;
import com.project.bankapi.domain.repository.AccountRepository;
import com.project.bankapi.exception.AccountNotFoundException;
import com.project.bankapi.exception.InsufficientFundsException;
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

    @Transactional
    public void withdraw(UUID accountId, BigDecimal amount) {
        log.info("Списание средств. accountId={}, amount={}", accountId, amount);

        Account account = repository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId.toString()));

        if (account.getBalance().compareTo(amount) < 0) {
            log.warn("Недостаточно средств. balance={}, amount={}",
                    account.getBalance(), amount);
            throw new InsufficientFundsException();
        }

        account.setBalance(account.getBalance().subtract(amount));

        log.info("Списание выполнено. Новый баланс={}", account.getBalance());
    }

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


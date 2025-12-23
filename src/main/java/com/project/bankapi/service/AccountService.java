package com.project.bankapi.service;

import com.project.bankapi.domain.entity.Account;
import com.project.bankapi.domain.entity.Transaction;
import com.project.bankapi.domain.enums.TransactionType;
import com.project.bankapi.domain.repository.AccountRepository;
import com.project.bankapi.domain.repository.TransactionRepository;
import com.project.bankapi.dto.response.TransactionResponse;
import com.project.bankapi.exception.AccountNotFoundException;
import com.project.bankapi.exception.InsufficientFundsException;
import com.project.bankapi.exception.InvalidInitialBalanceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Retryable(
            retryFor = ObjectNotFoundException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 100)
    )
    @Transactional
    public void transfer(UUID fromId, UUID toId, BigDecimal amount) {
        log.info("Перевод средств. from={}, to={}, amount={}", fromId, toId, amount);

        if (fromId.equals(toId)) {
            throw new IllegalArgumentException("Нельзя переводить средства на тот же счет");
        }

        Account from = accountRepository.findById(fromId)
                .orElseThrow(() -> new AccountNotFoundException(fromId.toString()));

        Account to = accountRepository.findById(toId)
                .orElseThrow(() -> new AccountNotFoundException(toId.toString()));

        if (fromId.compareTo(toId) < 0) {
            throw new InsufficientFundsException();
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        transactionRepository.save(Transaction.builder()
                .id(UUID.randomUUID())
                .account(from)
                .amount(amount)
                .type(TransactionType.WITHDRAW)
                .createdAt(OffsetDateTime.now())
                .build());
        transactionRepository.save(Transaction.builder()
                .id(UUID.randomUUID())
                .account(to)
                .amount(amount)
                .type(TransactionType.DEPOSIT)
                .createdAt(OffsetDateTime.now())
                .build());

        log.info("Перевод выполнен успешно");
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponse> getTransactions(
            UUID accountId,
            Pageable pageable) {
        log.info("Запрос истории транзакций. accountId={}", accountId);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId.toString()));

        return transactionRepository
                .findByAccountId(account.getId(), pageable)
                .map(tx -> TransactionResponse.builder()
                        .id(tx.getId())
                        .type(tx.getType())
                        .amount(tx.getAmount())
                        .createdAt(tx.getCreatedAt())
                        .build());
    }

    @Retryable(
            retryFor = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 100)
    )
    @Transactional
    public void deposit(UUID accountId, BigDecimal amount) {
        log.info("Зачисление средств. accountId={}, amount={}", accountId, amount);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId.toString()));

        account.setBalance(account.getBalance().add(amount));

        Transaction tx = Transaction.builder()
                .id(UUID.randomUUID())
                .amount(amount)
                .account(account)
                .type(TransactionType.DEPOSIT)
                .createdAt(OffsetDateTime.now())
                .build();
        transactionRepository.save(tx);

        log.info("Транзакция DEPOSIT сохранена. txId={}", tx.getId());
    }

    @Retryable(
            retryFor = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 100)
    )
    @Transactional
    public void withdraw(UUID accountId, BigDecimal amount) {
        log.info("Списание средств. accountId={}, amount={}", accountId, amount);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId.toString()));

        if (account.getBalance().compareTo(amount) < 0) {
            log.warn("Недостаточно средств. balance={}, amount={}",
                    account.getBalance(), amount);
            throw new InsufficientFundsException();
        }

        account.setBalance(account.getBalance().subtract(amount));

        Transaction tx = Transaction.builder()
                .id(UUID.randomUUID())
                .amount(amount)
                .account(account)
                .type(TransactionType.WITHDRAW)
                .createdAt(OffsetDateTime.now())
                .build();
        transactionRepository.save(tx);

        log.info("Транзакция WITHDRAW сохранена. txId={}", tx.getId());
    }

    @Transactional(readOnly = true)
    public Account getAccountById(UUID accountId) {
        log.info("Запрос счета. accountId={}", accountId);

        return accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId.toString()));
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

        Account saved = accountRepository.save(account);

        log.info("Счет сохранён. accountId={}", saved.getId());
        return saved;
    }

    @Recover
    public void recoverOptimisticLock(
            ObjectOptimisticLockingFailureException ex,
            UUID accountId,
            BigDecimal amount
    ) {
        log.error(
                "Retry исчерпан. Не удалось списать средства. accountId={}, amount={}",
                accountId,
                amount,
                ex
        );

        throw ex;
    }
}

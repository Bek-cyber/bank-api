package com.project.bankapi.domain.repository;

import com.project.bankapi.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository
        extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByAccountIdOrderByCreatedAtDesc(UUID actionId);
}

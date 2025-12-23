package com.project.bankapi.domain.repository;

import com.project.bankapi.domain.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository
        extends JpaRepository<Transaction, UUID> {

    Page<Transaction> findByAccountId(UUID actionId, Pageable pageable);
}

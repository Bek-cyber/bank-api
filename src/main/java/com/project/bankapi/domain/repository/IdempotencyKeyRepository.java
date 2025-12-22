package com.project.bankapi.domain.repository;

import com.project.bankapi.domain.entity.IdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IdempotencyKeyRepository
        extends JpaRepository<IdempotencyKey, UUID> {

    Optional<IdempotencyKey> findByKeyValueAndEndpoint(
            String keyValue,
            String endpoint
    );
}

package com.project.bankapi.domain.repository;

import com.project.bankapi.domain.entity.IdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

public interface IdempotencyKeyRepository
        extends JpaRepository<IdempotencyKey, UUID> {

    Optional<IdempotencyKey> findByKeyValueAndEndpoint(
            String keyValue,
            String endpoint
    );

    @Modifying
    @Query("""
                       delete from IdempotencyKey k
                       where k.createdAt < :threshold
            """)
    int deleteOlderThan(@Param("threshold") OffsetDateTime threshold);
}

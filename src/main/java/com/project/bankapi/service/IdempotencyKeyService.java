package com.project.bankapi.service;

import com.project.bankapi.domain.entity.IdempotencyKey;
import com.project.bankapi.domain.repository.IdempotencyKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdempotencyKeyService {
    private final IdempotencyKeyRepository idempotencyKeyRepository;

    public Optional<IdempotencyKey> findExisting(
            String key,
            String endpoint
    ) {
        return idempotencyKeyRepository.findByKeyValueAndEndpoint(key, endpoint);
    }

    public void saveResponse(
            String key,
            String endpoint,
            String requestHash,
            int status,
            String responseBody
    ) {
        IdempotencyKey idempotencyKey = IdempotencyKey.builder()
                .id(UUID.randomUUID())
                .keyValue(key)
                .endpoint(endpoint)
                .requestHash(requestHash)
                .responseStatus(status)
                .responseBody(responseBody)
                .createdAt(OffsetDateTime.now())
                .build();

        idempotencyKeyRepository.save(idempotencyKey);
    }
}

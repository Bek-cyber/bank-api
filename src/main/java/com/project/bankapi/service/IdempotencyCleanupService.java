package com.project.bankapi.service;

import com.project.bankapi.domain.repository.IdempotencyKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdempotencyCleanupService {
    private final IdempotencyKeyRepository idempotencyRepository;

    @Value("${idempotency.ttl-hours}")
    private long ttlHours;

    @Transactional
    public void cleanupExpiredKeys() {
        OffsetDateTime threshold = OffsetDateTime.now().minusHours(ttlHours);

        int deleted = idempotencyRepository.deleteOlderThan(threshold);

        if (deleted > 0) {
            log.info("Удалено устаревших idempotency-ключей: {}", deleted);
        } else {
            log.debug("Устаревших idempotency-ключей не найдено");
        }
    }
}

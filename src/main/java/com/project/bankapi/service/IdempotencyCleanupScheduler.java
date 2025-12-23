package com.project.bankapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IdempotencyCleanupScheduler {
    private final IdempotencyCleanupService cleanupService;

    @Scheduled(cron = "${idempotency.cleanup.cron}")
    public void runCleanup() {
        log.debug("Запуск очистки idempotency-ключей");
        cleanupService.cleanupExpiredKeys();
    }
}

package com.project.bankapi.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "idempotency_keys",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_idempotency",
                columnNames = {"key_value", "endpoint"}
        ))
public class IdempotencyKey {
    @Id
    private UUID id;

    @Column(name = "key_value", nullable = false)
    private String keyValue;

    private String endpoint;

    @Column(nullable = false)
    private String requestHash;

    @Column(columnDefinition = "text")
    private String responseBody;

    @Column(nullable = false)
    private int responseStatus;

    @Column(nullable = false)
    private OffsetDateTime createdAt;
}

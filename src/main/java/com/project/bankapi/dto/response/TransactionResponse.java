package com.project.bankapi.dto.response;

import com.project.bankapi.domain.enums.TransactionType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
public class TransactionResponse {
    private UUID id;

    private TransactionType type;

    private BigDecimal amount;

    private OffsetDateTime createdAt;
}

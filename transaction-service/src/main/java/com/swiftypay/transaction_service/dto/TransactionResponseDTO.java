package com.swiftypay.transaction_service.dto;

import com.swiftypay.transaction_service.model.TransactionStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record TransactionResponseDTO(
        UUID transactionId,
        String idempotencyKey,
        BigDecimal amount,
        TransactionStatus status,
        LocalDateTime createdAt
) {
}

package com.swiftypay.transaction_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionRequestDTO(
        @NotNull @Positive BigDecimal amount,
        @NotNull UUID accountIdFrom,
        @NotNull UUID accountIdTo
) {
}

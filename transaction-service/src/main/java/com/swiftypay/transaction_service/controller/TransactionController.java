package com.swiftypay.transaction_service.controller;

import com.swiftypay.transaction_service.dto.TransactionRequestDTO;
import com.swiftypay.transaction_service.dto.TransactionResponseDTO;
import com.swiftypay.transaction_service.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @RequestBody @Valid TransactionRequestDTO transactionRequestDTO,
            @RequestHeader("x-idempootency-key") String idempotencyKey
    ) {
        TransactionResponseDTO transactionResponseDTO = transactionService.process(transactionRequestDTO, idempotencyKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponseDTO);
    }
}

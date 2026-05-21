package com.swiftypay.transaction_service.service;

import com.swiftypay.transaction_service.dto.TransactionRequestDTO;
import com.swiftypay.transaction_service.dto.TransactionResponseDTO;
import com.swiftypay.transaction_service.exception.AccountNotExistsException;
import com.swiftypay.transaction_service.model.Account;
import com.swiftypay.transaction_service.model.Transaction;
import com.swiftypay.transaction_service.model.TransactionStatus;
import com.swiftypay.transaction_service.repository.AccountRepository;
import com.swiftypay.transaction_service.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public TransactionResponseDTO process(TransactionRequestDTO transactionRequestDTO, String idempotencyKey) {
        Optional<Transaction> existing = transactionRepository.findByIdempotencyKey(idempotencyKey);
        if(existing.isPresent()) {
            return mapToResponse(existing.get());
        }

        Account accountFrom = accountRepository.findById(transactionRequestDTO.accountIdFrom())
                .orElseGet(() -> null);
        Account accountTo = accountRepository.findById(transactionRequestDTO.accountIdTo())
                .orElseGet(() -> null);

        TransactionStatus transactionStatus = validateTransaction(accountFrom, accountTo, transactionRequestDTO.amount());

        if(transactionStatus.equals(TransactionStatus.SUCCESS)){
            accountFrom.setBalance(accountFrom.getBalance().subtract(transactionRequestDTO.amount()));
            accountTo.setBalance(accountTo.getBalance().add(transactionRequestDTO.amount()));
        }

        Transaction transaction = Transaction.builder()
                .idempotencyKey(idempotencyKey)
                .accountIdFrom(transactionRequestDTO.accountIdFrom())
                .accountIdTo(transactionRequestDTO.accountIdTo())
                .amount(transactionRequestDTO.amount())
                .status(transactionStatus)
                .createdAt(LocalDateTime.now())
                .build();

        return mapToResponse(transactionRepository.save(transaction));
    }

    private TransactionStatus validateTransaction(Account accountFrom, Account accountTo, BigDecimal amount) {
        if(accountFrom == null || accountTo == null) return TransactionStatus.ACCOUNT_NOT_FOUND;
        if(accountFrom.getBalance().compareTo(amount) < 0.0) return TransactionStatus.INSUFFICIENT_FUNDS;
        return TransactionStatus.SUCCESS;
    }

    private TransactionResponseDTO mapToResponse(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getIdempotencyKey(),
                transaction.getAmount(),
                transaction.getStatus(),
                LocalDateTime.now()
        );
    }


}

package com.ebuka.notesapi.dto.res;

import com.ebuka.notesapi.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WalletTransactionResponse {

    private Long id;
    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime createdAt;

    public WalletTransactionResponse(
            Long id,
            TransactionType type,
            BigDecimal amount,
            LocalDateTime createdAt) {

        this.id = id;
        this.type = type;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getDescription() {
    return switch (type) {
        case DEPOSIT -> "Deposit";
        case TRANSFER_OUT -> "Transfer sent";
        case TRANSFER_IN -> "Money received";
        case TRANSFER ->
                "Older transfer (direction unavailable)";
    };
}
}
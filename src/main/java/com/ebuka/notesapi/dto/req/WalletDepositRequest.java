package com.ebuka.notesapi.dto.req;

import java.math.BigDecimal;

public class WalletDepositRequest {

    private BigDecimal amount;

    public WalletDepositRequest() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
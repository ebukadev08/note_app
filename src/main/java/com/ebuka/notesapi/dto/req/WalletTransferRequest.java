package com.ebuka.notesapi.dto.req;

import java.math.BigDecimal;

public class WalletTransferRequest {

    private String recipientEmail;
    private BigDecimal amount;

    public WalletTransferRequest() {
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

package com.ebuka.notesapi.service;

import com.ebuka.notesapi.dto.res.WalletTransactionResponse;
import com.ebuka.notesapi.model.Wallet;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {

    Wallet createWallet(String email);

    BigDecimal getMyBalance(String email);

    BigDecimal deposit(String email, BigDecimal amount);

    BigDecimal transfer(
            String senderEmail,
            String recipientEmail,
            BigDecimal amount
    );
    List<WalletTransactionResponse> getMyTransactions(String email);
}

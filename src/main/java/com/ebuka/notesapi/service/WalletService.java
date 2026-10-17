
package com.ebuka.notesapi.service;

import com.ebuka.notesapi.model.Wallet;

import java.math.BigDecimal;

public interface WalletService {

    Wallet createWallet(String email);

    BigDecimal getMyBalance(String email);

    BigDecimal deposit(String email, BigDecimal amount);
}

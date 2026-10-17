package com.ebuka.notesapi.repository;

import com.ebuka.notesapi.model.Wallet;
import com.ebuka.notesapi.model.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletTransactionRepository
        extends JpaRepository<WalletTransaction, Long> {

    List<WalletTransaction> findByWalletOrderByCreatedAtDesc(
            Wallet wallet
    );
}
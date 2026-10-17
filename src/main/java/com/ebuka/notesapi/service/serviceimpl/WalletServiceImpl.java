
package com.ebuka.notesapi.service.serviceimpl;

import com.ebuka.notesapi.model.User;
import com.ebuka.notesapi.model.Wallet;
import com.ebuka.notesapi.repository.UserRepository;
import com.ebuka.notesapi.repository.WalletRepository;
import com.ebuka.notesapi.service.WalletService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class WalletServiceImpl implements WalletService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    public WalletServiceImpl(
            UserRepository userRepository,
            WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }

    @Override
    @Transactional
    public Wallet createWallet(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        if (walletRepository.existsByUser(user)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Wallet already exists");
        }

        Wallet wallet = new Wallet(user);
        wallet.setBalance(BigDecimal.ZERO);

        return walletRepository.save(wallet);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getMyBalance(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Wallet not found"));

        return wallet.getBalance();
    }

    @Override
    @Transactional
    public BigDecimal deposit(String email, BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Deposit amount must be greater than zero"
            );
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"
                ));

        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Wallet not found"
                ));

        BigDecimal newBalance = wallet.getBalance().add(amount);

        wallet.setBalance(newBalance);
        walletRepository.save(wallet);

        return newBalance;
}
}

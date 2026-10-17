
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

    @Override
    @Transactional
    public BigDecimal transfer(
            String senderEmail,
            String recipientEmail,
            BigDecimal amount) {

        // 1. Validate the amount
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Transfer amount must be greater than zero"
            );
        }

        // 2. Prevent transferring to yourself
        if (senderEmail.equalsIgnoreCase(recipientEmail)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You cannot transfer money to yourself"
            );
        }

        // 3. Find the sender
        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Sender not found"
                ));

        // 4. Find the recipient
        User recipient = userRepository.findByEmail(recipientEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Recipient not found"
                ));

        // 5. Find both wallets
        Wallet senderWallet = walletRepository.findByUser(sender)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Sender wallet not found"
                ));

        Wallet recipientWallet = walletRepository.findByUser(recipient)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Recipient wallet not found"
                ));

        // 6. Check the sender's balance
        if (senderWallet.getBalance().compareTo(amount) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Insufficient wallet balance"
            );
        }

        // 7. Calculate the new balances
        BigDecimal senderBalance =
                senderWallet.getBalance().subtract(amount);

        BigDecimal recipientBalance =
                recipientWallet.getBalance().add(amount);

        // 8. Update both wallets
        senderWallet.setBalance(senderBalance);
        recipientWallet.setBalance(recipientBalance);

        walletRepository.save(senderWallet);
        walletRepository.save(recipientWallet);

        // 9. Return the sender's new balance
        return senderBalance;
    }
}

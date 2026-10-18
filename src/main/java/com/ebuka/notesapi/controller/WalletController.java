
package com.ebuka.notesapi.controller;

import com.ebuka.notesapi.dto.req.WalletDepositRequest;
import com.ebuka.notesapi.dto.req.WalletTransferRequest;
import com.ebuka.notesapi.dto.res.ApiRes;
import com.ebuka.notesapi.dto.res.WalletTransactionResponse;
import com.ebuka.notesapi.model.Wallet;
import com.ebuka.notesapi.service.WalletService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<ApiRes<Map<String, Object>>> createWallet(
            Authentication authentication) {

        Wallet wallet = walletService.createWallet(
                authentication.getName());

        Map<String, Object> data = Map.of(
                "id", wallet.getId(),
                "balance", wallet.getBalance(),
                "createdAt", wallet.getCreatedAt()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiRes.success(
                        "Wallet created successfully", data));
    }

    @GetMapping("/balance")
    public ResponseEntity<ApiRes<Map<String, Object>>> getMyBalance(
            Authentication authentication) {

        BigDecimal balance = walletService.getMyBalance(
                authentication.getName());

        return ResponseEntity.ok(ApiRes.success(
                "Wallet balance retrieved successfully",
                Map.of("balance", balance)
        ));
    }

    @PostMapping("/deposit")
    public ResponseEntity<ApiRes<Map<String, Object>>> deposit(
            Authentication authentication,
            @RequestBody WalletDepositRequest request) {

        BigDecimal newBalance = walletService.deposit(
                authentication.getName(),
                request.getAmount()
        );

        return ResponseEntity.ok(
                ApiRes.success(
                        "Demo deposit successful",
                        Map.of("balance", newBalance)
                )
        );
    }
    @PostMapping("/transfer")
    public ResponseEntity<ApiRes<Map<String, Object>>> transfer(
        Authentication authentication,
        @RequestBody WalletTransferRequest request) {

        BigDecimal newBalance = walletService.transfer(
                authentication.getName(),
                request.getRecipientEmail(),
                request.getAmount()
        );

        return ResponseEntity.ok(
                ApiRes.success(
                        "Transfer successful",
                        Map.of("balance", newBalance)
                )
        );
    }

    @GetMapping("/transactions")
    public ResponseEntity<ApiRes<List<WalletTransactionResponse>>> getMyTransactions(
        Authentication authentication) {

        List<WalletTransactionResponse> transactions =
                walletService.getMyTransactions(authentication.getName());

        return ResponseEntity.ok(
                ApiRes.success(
                        "Transaction history retrieved successfully",
                        transactions
                )
        );
        }
}

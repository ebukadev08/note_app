
package com.ebuka.notesapi.repository;

import com.ebuka.notesapi.model.User;
import com.ebuka.notesapi.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUser(User user);

    boolean existsByUser(User user);
}

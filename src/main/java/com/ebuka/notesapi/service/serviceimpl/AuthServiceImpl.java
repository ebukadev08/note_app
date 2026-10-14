
package com.ebuka.notesapi.service.serviceimpl;

import com.ebuka.notesapi.model.User;
import com.ebuka.notesapi.repository.UserRepository;
import com.ebuka.notesapi.service.AuthService;
import com.ebuka.notesapi.service.EmailService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
public class AuthServiceImpl implements AuthService {

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    private final SecureRandom secureRandom = new SecureRandom();

    public AuthServiceImpl(EmailService emailService, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public void signup(String name, String email, String password) {
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        String normalizedName = name.trim();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException(
                    "An account with this email already exists"
            );
        }

        String passwordHash = passwordEncoder.encode(password);

        User user = new User(normalizedName, normalizedEmail, passwordHash);

        String otp = String.format(
                "%06d",
                secureRandom.nextInt(1_000_000)
        );

        user.setOtpHash(passwordEncoder.encode(otp));
        user.setOtpExpiresAt(LocalDateTime.now().plusMinutes(5));
        user.setOtpAttempts(0);
        user.setVerified(false);

        userRepository.save(user);
        emailService.sendOtpEmail(normalizedEmail, otp);
    }
    @Override
    public void resendOtp(String email) {
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("Account not found")
                );

        if (user.isVerified()) {
            throw new IllegalArgumentException(
                    "This account is already verified"
            );
        }

        String otp = String.format(
                "%06d",
                secureRandom.nextInt(1_000_000)
        );

        user.setOtpHash(passwordEncoder.encode(otp));
        user.setOtpExpiresAt(LocalDateTime.now().plusMinutes(5));
        user.setOtpAttempts(0);

        userRepository.save(user);

        emailService.sendOtpEmail(normalizedEmail, otp);
    }
}

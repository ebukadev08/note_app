
package com.ebuka.notesapi.service.serviceimpl;

import com.ebuka.notesapi.model.User;
import com.ebuka.notesapi.repository.UserRepository;
import com.ebuka.notesapi.service.AuthService;
import com.ebuka.notesapi.service.EmailService;
import com.ebuka.notesapi.service.JwtService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    private final SecureRandom secureRandom = new SecureRandom();

    public AuthServiceImpl(EmailService emailService, JwtService jwtService, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.jwtService = jwtService;
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
    @Override
    public void verifyOtp(String email, String otp) {

        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid email or OTP")
                );

        if (user.isVerified()) {
                throw new IllegalArgumentException(
                        "This account is already verified"
                );
        }

        if (user.getOtpHash() == null
                || user.getOtpExpiresAt() == null) {
                throw new IllegalArgumentException("No active OTP. Request a new one.");
        }

        if (!LocalDateTime.now().isBefore(user.getOtpExpiresAt())) {
                throw new IllegalArgumentException("OTP has expired. Request a new one.");
        }

        if (user.getOtpAttempts() >= 5) {
                throw new IllegalArgumentException(
                        "Too many incorrect attempts. Request a new OTP."
                );
        }

        user.setOtpAttempts(user.getOtpAttempts() + 1);

        if (!passwordEncoder.matches(otp, user.getOtpHash())) {
                userRepository.save(user);

                throw new IllegalArgumentException("Invalid email or OTP");
        }

        user.setVerified(true);
        user.setOtpHash(null);
        user.setOtpExpiresAt(null);
        user.setOtpAttempts(0);

        userRepository.save(user);
        }


   @Override
   public String login(String email, String password) {

        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid email or password")
                );

        if (!user.isVerified()) {
                throw new IllegalArgumentException(
                        "Please verify your email before logging in"
                );
        }

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
                throw new IllegalArgumentException("Invalid email or password");
        }

        return jwtService.generateToken(user.getEmail());
        }

}

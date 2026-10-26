
package com.ebuka.notesapi.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OtpService {

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    private final SecureRandom secureRandom = new SecureRandom();

    private final ConcurrentHashMap<String, OtpData> otpStorage =
            new ConcurrentHashMap<>();

    private static final int MAX_ATTEMPTS = 5;
    private static final int OTP_EXPIRY_MINUTES = 5;

    public String generateOtp(String email) {
        String otp = String.format(
                "%06d", secureRandom.nextInt(1_000_000));

        OtpData data = new OtpData(
                passwordEncoder.encode(otp),
                LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES)
        );

        otpStorage.put(email, data);

        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        OtpData data = otpStorage.get(email);

        if (data == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No active OTP. Please request a new one."
            );
        }

        if (!LocalDateTime.now().isBefore(data.expiresAt)) {
            otpStorage.remove(email, data);

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "OTP has expired. Please request a new one."
            );
        }

        if (data.attempts.get() >= MAX_ATTEMPTS) {
            otpStorage.remove(email, data);

            throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "Too many incorrect attempts. Please request a new OTP."
            );
        }

        if (!passwordEncoder.matches(otp, data.otpHash)) {
            int attempts = data.attempts.incrementAndGet();
            if (attempts >= MAX_ATTEMPTS) {
                otpStorage.remove(email, data);

                throw new ResponseStatusException(
                        HttpStatus.TOO_MANY_REQUESTS,
                        "Too many incorrect attempts. Please request a new OTP."
                );
            }

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid email or OTP."
            );
        }

        // Ensure this OTP is still the active one and remove it once used.
        if (!otpStorage.remove(email, data)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "OTP is no longer valid. Please request a new one."
            );
        }

        return true;
    }

    private static class OtpData {

        private final String otpHash;
        private final LocalDateTime expiresAt;
        private final AtomicInteger attempts = new AtomicInteger(0);

        private OtpData(String otpHash, LocalDateTime expiresAt) {
            this.otpHash = otpHash;
            this.expiresAt = expiresAt;
        }
    }
}

package com.ebuka.notesapi.service.serviceimpl;

import com.ebuka.notesapi.model.User;
import com.ebuka.notesapi.repository.UserRepository;
import com.ebuka.notesapi.service.AuthService;
import com.ebuka.notesapi.service.JwtService;
import com.ebuka.notesapi.service.OtpService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log =
            LoggerFactory.getLogger(AuthServiceImpl.class);

    private final JwtService jwtService;
    private final OtpService otpService;
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public AuthServiceImpl(
            JwtService jwtService,
            OtpService otpService,
            UserRepository userRepository) {

        this.jwtService = jwtService;
        this.otpService = otpService;
        this.userRepository = userRepository;
    }

    @Override
    public void signup(String name, String email, String password) {

        String normalizedEmail =
                email.trim().toLowerCase(Locale.ROOT);

        String normalizedName = name.trim();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "An account with this email already exists");
        }

        String passwordHash = passwordEncoder.encode(password);

        User user = new User(
                normalizedName,
                normalizedEmail,
                passwordHash
        );

        user.setVerified(false);

        userRepository.save(user);

        String otp = otpService.generateOtp(normalizedEmail);

        // LOCAL DEVELOPMENT ONLY. Never log OTPs in production.
        log.info("OTP for {} (development only): {}", normalizedEmail, otp);
    }

    @Override
    public void resendOtp(String email) {

        String normalizedEmail =
                email.trim().toLowerCase(Locale.ROOT);

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        if (user.isVerified()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "This account is already verified");
        }

        String otp = otpService.generateOtp(normalizedEmail);

        // LOCAL DEVELOPMENT ONLY.
        log.info("OTP for {} (development only): {}", normalizedEmail, otp);
    }

    @Override
    public void verifyOtp(String email, String otp) {

        String normalizedEmail =
                email.trim().toLowerCase(Locale.ROOT);

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or OTP"));

        if (user.isVerified()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "This account is already verified");
        }

        otpService.verifyOtp(normalizedEmail, otp);

        user.setVerified(true);

        userRepository.save(user);
    }

    @Override
    public String login(String email, String password) {

        String normalizedEmail =
                email.trim().toLowerCase(Locale.ROOT);

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Invalid email or password"));

        if (!user.isVerified()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Please verify your email before logging in");
        }

        if (!passwordEncoder.matches(
                password, user.getPasswordHash())) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Invalid email or password");
        }

        return jwtService.generateToken(user.getEmail());
    }
}
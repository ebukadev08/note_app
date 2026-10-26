package com.ebuka.notesapi.service.serviceimpl;

import com.ebuka.notesapi.dto.req.SignupRequest;
import com.ebuka.notesapi.mapper.UserMapper;
import com.ebuka.notesapi.mapper.UserProfileMapper;
import com.ebuka.notesapi.model.User;
import com.ebuka.notesapi.model.UserProfile;
import com.ebuka.notesapi.repository.UserProfileRepository;
import com.ebuka.notesapi.repository.UserRepository;
import com.ebuka.notesapi.service.AuthService;
import com.ebuka.notesapi.service.JwtService;
import com.ebuka.notesapi.service.OtpService;

import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private static final Logger log =
            LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserProfileMapper userProfileMapper;
    private final UserProfileRepository userProfileRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final OtpService otpService;
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    

    public AuthServiceImpl(
            JwtService jwtService,
            OtpService otpService,
            UserRepository userRepository,
            UserProfileRepository userProfileRepository,
            UserProfileMapper userProfileMapper,
            UserMapper userMapper
        ) {

        this.jwtService = jwtService;
        this.otpService = otpService;
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.userProfileMapper = userProfileMapper;
        this.userMapper = userMapper;
    }

    @Override
    public void signup(SignupRequest request) {

        String normalizedEmail =
                request.getEmail().trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User already exist");
        }

        String passwordHash = passwordEncoder.encode(request.getPassword());

        User user = userMapper.toUser(request, passwordHash);

        user.setVerified(false);

        User savedUser = userRepository.save(user);

        UserProfile profile =
            userProfileMapper.toUserProfile(request, savedUser);

        userProfileRepository.save(profile);

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
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid email or OTP"));

        if (user.isVerified()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
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
                        new ResponseStatusException(HttpStatus.FORBIDDEN,
                                "Invalid email or password"));

        if (!passwordEncoder.matches(
                password, user.getPasswordHash())) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Invalid email or password");
        }

        if (!user.isVerified()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Please verify your email before logging in");
        }

        return jwtService.generateToken(user.getEmail());
    }
}
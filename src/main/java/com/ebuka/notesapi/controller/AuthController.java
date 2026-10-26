
package com.ebuka.notesapi.controller;

import com.ebuka.notesapi.dto.req.LoginRequest;
import com.ebuka.notesapi.dto.req.ResendOtpRequest;
import com.ebuka.notesapi.dto.req.SignupRequest;
import com.ebuka.notesapi.dto.req.VerifyOtpRequest;
import com.ebuka.notesapi.dto.res.ApiRes;
import com.ebuka.notesapi.service.AuthService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Signup
    @PostMapping("/signup")
    public ResponseEntity<ApiRes<Void>> signup(
            @Valid @RequestBody SignupRequest request) {

        authService.signup(
                request
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiRes.success(
                        "Signup successful. Verify your account to continue.",
                        null
                ));
    }

    // Resend OTP
    @PostMapping("/resend-otp")
    public ResponseEntity<ApiRes<Void>> resendOtp(
            @Valid @RequestBody ResendOtpRequest request) {

        authService.resendOtp(request.getEmail());

        return ResponseEntity.ok(
                ApiRes.success(
                        "OTP sent",
                        null
                )
        );
    }

    // Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiRes<Void>> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) {

        authService.verifyOtp(
                request.getEmail(),
                request.getOtp()
        );

        return ResponseEntity.ok(
                ApiRes.success(
                        "Account verified successfully.",
                        null
                )
        );
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<ApiRes<Map<String, String>>> login(
            @Valid @RequestBody LoginRequest request) {

        String token = authService.login(
                request.getEmail(),
                request.getPassword()
        );

        return ResponseEntity.ok(
                ApiRes.success(
                        "Login successful.",
                        Map.of("token", token)
                )
        );
    }
}

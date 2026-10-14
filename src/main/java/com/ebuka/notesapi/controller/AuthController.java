
package com.ebuka.notesapi.controller;

import com.ebuka.notesapi.dto.ResendOtpRequest;
import com.ebuka.notesapi.dto.SignupRequest;
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

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(
            @Valid @RequestBody SignupRequest request) {

        authService.signup(
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                        "message",
                        "Signup received. Check your email for the OTP."
                ));
    }
    @PostMapping("/resend-otp")
    public ResponseEntity<Map<String, String>> resendOtp(
        @Valid @RequestBody ResendOtpRequest request) {

        authService.resendOtp(request.getEmail());

        return ResponseEntity.ok(
        Map.of(
                "message",
                "If the account is eligible, an OTP request has been processed."
        )
        );
        }
}

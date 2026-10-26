package com.ebuka.notesapi.service;

import com.ebuka.notesapi.dto.req.SignupRequest;

public interface AuthService {

    void signup(SignupRequest request);
    
    void resendOtp(String email);

    void verifyOtp(String email, String otp);

    String login(String email, String password);
}

package com.ebuka.notesapi.service;

public interface AuthService {

    void signup(String name, String email, String password);
    
    void resendOtp(String email);

    void verifyOtp(String email, String otp);

    String login(String email, String password);
    
}

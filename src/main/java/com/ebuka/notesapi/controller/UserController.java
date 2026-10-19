package com.ebuka.notesapi.controller;

import com.ebuka.notesapi.dto.req.UpdateProfileRequest;
import com.ebuka.notesapi.dto.res.ApiRes;
import com.ebuka.notesapi.dto.res.UserResponse;
import com.ebuka.notesapi.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiRes<UserResponse>> getMyProfile(
            Authentication authentication) {

        UserResponse user = userService.getMyProfile(
                authentication.getName()
        );

        return ResponseEntity.ok(
                ApiRes.success("Profile retrieved successfully", user)
        );
    }

    @PutMapping("/me")
    public ResponseEntity<ApiRes<UserResponse>> updateMyProfile(
            Authentication authentication,
            @RequestBody UpdateProfileRequest request) {

        UserResponse user = userService.updateMyProfile(
                authentication.getName(),
                request
        );

        return ResponseEntity.ok(
                ApiRes.success("Profile updated successfully", user)
        );
    }
}
package com.ebuka.notesapi.service;

import com.ebuka.notesapi.dto.req.UpdateProfileRequest;
import com.ebuka.notesapi.dto.res.UserResponse;

public interface UserService {

    UserResponse getMyProfile(String email);

    UserResponse updateMyProfile(
        String email,
        UpdateProfileRequest request
);

}

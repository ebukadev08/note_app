package com.ebuka.notesapi.service;

import com.ebuka.notesapi.dto.res.UserResponse;

public interface UserService {

    UserResponse getMyProfile(String email);

}

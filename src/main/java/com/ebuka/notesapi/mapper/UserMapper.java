
package com.ebuka.notesapi.mapper;

import org.springframework.stereotype.Component;

import com.ebuka.notesapi.dto.req.SignupRequest;
import com.ebuka.notesapi.model.User;

@Component
public class UserMapper {

    public User toUser(SignupRequest request, String passwordHash) {
        return new User(
                request.getFirstName().trim(),
                request.getLastName().trim(),
                request.getEmail().trim().toLowerCase(),
                request.getRole(),
                passwordHash
        );
    }
}

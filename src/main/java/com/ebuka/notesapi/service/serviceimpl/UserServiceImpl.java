package com.ebuka.notesapi.service.serviceimpl;

import com.ebuka.notesapi.dto.req.UpdateProfileRequest;
import com.ebuka.notesapi.dto.res.UserResponse;
import com.ebuka.notesapi.model.User;
import com.ebuka.notesapi.repository.UserRepository;
import com.ebuka.notesapi.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getMyProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        User foundUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        return new UserResponse(
                foundUser.getId(),
                foundUser.getName(),
                foundUser.getEmail(),
                foundUser.isVerified(),
                foundUser.getRole()
        );
    }

@Override
@Transactional
public UserResponse updateMyProfile(
        String email,
        UpdateProfileRequest request) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found"
            ));

    if (request.getName() == null || request.getName().isBlank()) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Name cannot be blank"
        );
    }

    user.setName(request.getName().trim());

    User savedUser = userRepository.save(user);

    return new UserResponse(
            savedUser.getId(),
            savedUser.getName(),
            savedUser.getEmail(),
            savedUser.isVerified(),
            savedUser.getRole()
    );
}
}

package com.ebuka.notesapi.controller;

import com.ebuka.notesapi.dto.res.AdminUserResponse;
import com.ebuka.notesapi.dto.res.ApiRes;
import com.ebuka.notesapi.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<ApiRes<List<AdminUserResponse>>> getAllUsers() {

        List<AdminUserResponse> users = userRepository.findAll()
                .stream()
                .map(AdminUserResponse::from)
                .toList();

        return ResponseEntity.ok(
                ApiRes.success("Users retrieved successfully", users)
        );
    }
}

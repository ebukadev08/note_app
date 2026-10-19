package com.ebuka.notesapi.dto.res;

import com.ebuka.notesapi.model.Role;

public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private boolean verified;
    private Role role;

    public UserResponse(
            Long id,
            String name,
            String email,
            boolean verified,
            Role role) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.verified = verified;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isVerified() {
        return verified;
    }

    public Role getRole() {
        return role;
    }
}
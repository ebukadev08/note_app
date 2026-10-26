package com.ebuka.notesapi.dto.res;

import com.ebuka.notesapi.enums.Role;

public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean verified;
    private Role role;

    public UserResponse(
            Long id,
            String firstName,
            String lastName,
            String email,
            boolean verified,
            Role role) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.verified = verified;
        this.role = role;
    }

    public Long getId() {
        return id;
    }
    
     public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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
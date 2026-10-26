package com.ebuka.notesapi.dto.res;
import com.ebuka.notesapi.model.User;


public class AdminUserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean verified;
    private String role;

    public AdminUserResponse(
            Long id,
            String firstName,
            String lastName,
            String email,
            boolean verified,
            String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.verified = verified;
        this.role = role;
    }

    public static AdminUserResponse from(User user) {
        return new AdminUserResponse(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.isVerified(),
            user.getRole().name()
        );
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

    public String getRole() {
        return role;
    }
}

package com.ebuka.notesapi.dto.res;
import com.ebuka.notesapi.model.User;

public class AdminUserResponse {

    private Long id;
    private String name;
    private String email;
    private boolean verified;
    private String role;

    public AdminUserResponse(
            Long id,
            String name,
            String email,
            boolean verified,
            String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.verified = verified;
        this.role = role;
    }

    public static AdminUserResponse from(User user) {
        return new AdminUserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.isVerified(),
            user.getRole().name()
        );
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

    public String getRole() {
        return role;
    }
}

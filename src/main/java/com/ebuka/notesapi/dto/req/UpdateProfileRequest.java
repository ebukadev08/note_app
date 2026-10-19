package com.ebuka.notesapi.dto.req;

public class UpdateProfileRequest {

    private String name;

    public UpdateProfileRequest() {
    }

    public UpdateProfileRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
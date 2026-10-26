package com.ebuka.notesapi.mapper;

import org.springframework.stereotype.Component;
import com.ebuka.notesapi.dto.req.SignupRequest;
import com.ebuka.notesapi.model.User;
import com.ebuka.notesapi.model.UserProfile;

@Component
public class UserProfileMapper {

    public UserProfile toUserProfile(SignupRequest request, User user) {
        UserProfile profile =  new UserProfile(
                request.getPhoneNumber(),
                request.getDateOfBirth(),
                request.getGender(),
                request.getCountry(),
                request.getState(),
                request.getCity(),
                request.getAddress(),
                request.getOccupation(),
                request.getPostalCode()
        );

        profile.setUser(user);
        return profile;
    }
}
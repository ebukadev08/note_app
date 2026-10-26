package com.ebuka.notesapi.repository;

import com.ebuka.notesapi.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository
        extends JpaRepository<UserProfile, Long> {
}
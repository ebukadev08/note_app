package com.ebuka.notesapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @NotBlank
    @Pattern(regexp = "^\\+?[1-9]\\d{7,14}$")
    @Column(nullable = false, length = 16)
    private String phoneNumber;

    @NotNull
    @Past
    @Column(nullable = false)
    private LocalDate dateOfBirth;

    // the mapper stores gender in upper case
    @Pattern(regexp = "^(MALE|FEMALE|OTHER)$")
    @Column(length = 10)
    private String gender;

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(nullable = false, length = 100)
    private String country;

    @Size(max = 100)
    @Column(length = 100)
    private String state;

    @Size(max = 100)
    @Column(length = 100)
    private String city;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String address;

    @Size(max = 100)
    @Column(length = 100)
    private String occupation;

    @Pattern(regexp = "^[A-Za-z0-9\\- ]{3,10}$")
    @Column(length = 10)
    private String postalCode;

    public UserProfile() {
    }

    public UserProfile(
            String phoneNumber,
            LocalDate dateOfBirth,
            String gender,
            String country,
            String state,
            String city,
            String address,
            String occupation,
            String postalCode
    ) {
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.country = country;
        this.state = state;
        this.city = city;
        this.address = address;
        this.occupation = occupation;
        this.postalCode = postalCode;
    }

    public Long getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public User getUser() {
    return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
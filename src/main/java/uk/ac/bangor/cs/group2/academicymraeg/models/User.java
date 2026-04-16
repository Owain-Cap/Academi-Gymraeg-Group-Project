package uk.ac.bangor.cs.group2.academicymraeg.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    private String username;
    private String passwordHash;
    private String email;
    private String firstName;
    private String lastName;

    public enum IsInstructor {
        NO, YES
    }

    public enum IsAdmin {
        NO, YES
    }

    @Enumerated(EnumType.STRING)
    private IsInstructor isInstructor;

    @Enumerated(EnumType.STRING)
    private IsAdmin isAdmin;

    // Default constructor
    public User() {}

    // Constructor
    public User(long userId, String username, String passwordHash, String email,
                String firstName, String lastName,
                IsInstructor isInstructor, IsAdmin isAdmin) {

        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isInstructor = isInstructor;
        this.isAdmin = isAdmin;
    }

    // Getters & Setters

    public long getUserId() {
        return userId;
    }
    
    public void setUserId(long userId) {
    	this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public IsInstructor getIsInstructor() {
        return isInstructor;
    }

    public void setIsInstructor(IsInstructor isInstructor) {
        this.isInstructor = isInstructor;
    }

    public IsAdmin getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(IsAdmin isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId +
                ", username=" + username +
                ", email=" + email +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", isInstructor=" + isInstructor +
                ", isAdmin=" + isAdmin + "]";
    }
}

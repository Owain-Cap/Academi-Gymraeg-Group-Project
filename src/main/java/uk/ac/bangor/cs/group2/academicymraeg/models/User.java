package uk.ac.bangor.cs.group2.academicymraeg.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * User entity for the system.
 * Stores basic user details and role flags (admin/instructor).
 */
@Entity
public class User {

    /**
     * Primary key for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    /** Username used to log in */
    private String username;

    /** Hashed password */
    private String passwordHash;

    /** User email */
    private String email;

    /** First name */
    private String firstName;

    /** Last name */
    private String lastName;

    /**
     * Indicates if the user is an instructor.
     */
    public enum IsInstructor {
        NO, YES
    }

    /**
     * Indicates if the user is an admin.
     */
    public enum IsAdmin {
        NO, YES
    }

    /** Instructor flag */
    @Enumerated(EnumType.STRING)
    private IsInstructor isInstructor;

    /** Admin flag */
    @Enumerated(EnumType.STRING)
    private IsAdmin isAdmin;

    /**
     * Default constructor.
     */
    public User() {}

    /**
     * Full constructor.
     * 
     * @param userId user ID
     * @param username username
     * @param passwordHash password hash
     * @param email email
     * @param firstName first name
     * @param lastName last name
     * @param isInstructor instructor flag
     * @param isAdmin admin flag
     */
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

    /**
     * @return user ID
     */
    public long getUserId() {
        return userId;
    }
    
    /**
     * @param userId set user ID
     */
    public void setUserId(long userId) {
    	this.userId = userId;
    }

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username set username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return password hash
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * @param passwordHash set password hash
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email set email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName set first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName set last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return instructor flag
     */
    public IsInstructor getIsInstructor() {
        return isInstructor;
    }

    /**
     * @param isInstructor set instructor flag
     */
    public void setIsInstructor(IsInstructor isInstructor) {
        this.isInstructor = isInstructor;
    }

    /**
     * @return admin flag
     */
    public IsAdmin getIsAdmin() {
        return isAdmin;
    }

    /**
     * @param isAdmin set admin flag
     */
    public void setIsAdmin(IsAdmin isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * String version of the user (no password).
     */
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
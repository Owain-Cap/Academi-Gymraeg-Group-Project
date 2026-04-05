package uk.ac.bangor.cs.group2.academicymraeg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import uk.ac.bangor.cs.group2.academicymraeg.models.User;
import uk.ac.bangor.cs.group2.academicymraeg.repository.UserRepository;

/**
 * Class for the System Administrators
 * Handles the core logic and operations for Users (Create, Read, Update, Delete).
 * It uses the UserRepository to save or fetch data from the database.
 * Uses pre-built Spring Data JPA methods
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    // Password encoder used to securely hash user passwords
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Create or Update a user
    public User saveUser(User user) {

        // Ensure password is encoded before saving
        // Prevents storing plain text passwords and avoids login failures
        if (user.getPasswordHash() != null && !user.getPasswordHash().startsWith("$2a$")) {
            user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        }

        // Ensure role flags are not null (default to NO)
        // Prevents null pointer issues and ensures correct role assignment
        if (user.getIsInstructor() == null) {
            user.setIsInstructor(User.IsInstructor.NO);
        }

        if (user.getIsAdmin() == null) {
            user.setIsAdmin(User.IsAdmin.NO);
        }

        return userRepository.save(user);
    }

    // Read - find user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Read - find user by Username
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Read - get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Delete a user by ID
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    // Check if user exists
    public boolean userExists(Long id) {
        return userRepository.existsById(id);
    }
}
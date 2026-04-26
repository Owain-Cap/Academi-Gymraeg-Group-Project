package uk.ac.bangor.cs.group2.academicymraeg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import uk.ac.bangor.cs.group2.academicymraeg.models.User;
import uk.ac.bangor.cs.group2.academicymraeg.repository.UserRepository;

/**
 * Service class for managing system users.
 * Handles the core business logic and CRUD operations for the User entity.
 It uses the UserRepository to save or fetch data from the database.
 * Uses pre-built Spring Data JPA methods
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * Password encoder used to securely hash user passwords before storing them.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new user or updates an existing one.
     * Evaluates the given users password to ensure it is encoded properly in BCrypt format.
     * Also enforces default values for role flags (IsInstructor, IsAdmin) to avoid null references.
     *
     * @param user The User entity to be saved or updated.
     * @return The persisted User entity.
     */
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

    /**
     * Retrieves a user by their unique database ID.
     *
     * @param id The unique identifier of the user.
     * @return An Optional containing the User if found, otherwise empty.
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Retrieves a user by their unique username.
     *
     * @param username The exact username to query.
     * @return An Optional containing the User if found, otherwise empty.
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Retrieves a complete list of all users registered in the system.
     *
     * @return A List of all stored User entities.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Deletes a user from the system by their unique database ID.
     *
     * @param id The unique identifier of the user to remove.
     */
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Checks whether a user with the specified ID currently exists in the database.
     *
     * @param id The unique identifier to check.
     * @return True if the user exists, false otherwise.
     */
    public boolean userExists(Long id) {
        return userRepository.existsById(id);
    }
}
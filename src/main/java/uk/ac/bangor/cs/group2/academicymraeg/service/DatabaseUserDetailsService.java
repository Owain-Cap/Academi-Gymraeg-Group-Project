package uk.ac.bangor.cs.group2.academicymraeg.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uk.ac.bangor.cs.group2.academicymraeg.models.User;
import uk.ac.bangor.cs.group2.academicymraeg.repository.UserRepository;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public DatabaseUserDetailsService(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Retrieve the user from the database using the username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Build a list of roles based on the user's flags
        java.util.List<String> roles = new java.util.ArrayList<>();

        // Assign STUDENT role to all users by default
        roles.add("STUDENT");

        // Assign INSTRUCTOR role if the user is marked as an instructor
        if (user.getIsInstructor() == User.IsInstructor.YES) {
            roles.add("INSTRUCTOR");
        }

        // Assign SYSTEM_ADMIN role if the user is marked as an admin
        if (user.getIsAdmin() == User.IsAdmin.YES) {
            roles.add("SYSTEM_ADMIN");
        }

        // Note:
        // The .roles(...) method automatically prefixes each role with "ROLE_"
        // For example, "STUDENT" becomes "ROLE_STUDENT"
        // This must match how roles are checked in SecurityConfig (e.g., hasRole("STUDENT"))

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPasswordHash()) // Password should be stored using BCrypt encoding
                .roles(roles.toArray(new String[0])) // Convert role list to array
                .build();
    }
}
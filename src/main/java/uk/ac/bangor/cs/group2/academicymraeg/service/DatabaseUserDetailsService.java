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

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Build roles based on isInstructor and isAdmin flags
        java.util.List<String> roles = new java.util.ArrayList<>();
        roles.add("STUDENT"); // everyone gets STUDENT as a base role

        if (user.getIsInstructor() == User.IsInstructor.YES) {
            roles.add("INSTRUCTOR");
        }
        if (user.getIsAdmin() == User.IsAdmin.YES) {
            roles.add("SYSTEM_ADMIN");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .roles(roles.toArray(new String[0]))
                .build();
    }
}
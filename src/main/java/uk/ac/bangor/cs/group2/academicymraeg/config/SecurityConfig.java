package uk.ac.bangor.cs.group2.academicymraeg.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain permissionsFilter(HttpSecurity http) throws Exception {

        http
            // Disable CSRF if you're not using forms with tokens (optional but common in dev)
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                // Public resources (no authentication required)
                .requestMatchers("/css/**", "/images/**", "/js/**").permitAll()

             // STUDENT access (all logged-in users)
                .requestMatchers("/tests/**", "/my-tests/**")
                .hasAnyRole("STUDENT", "INSTRUCTOR", "SYSTEM_ADMIN")

                // INSTRUCTOR access
                .requestMatchers("/nouns/**")
                .hasAnyRole("INSTRUCTOR", "SYSTEM_ADMIN")

                // SYSTEM ADMIN only access
                .requestMatchers("/admin/**", "/register/**")
                .hasRole("SYSTEM_ADMIN")

                // Any other request requires authentication
                .anyRequest().authenticated()
            )

            // Enable default Spring Security login form
            .formLogin(withDefaults())

            // Enable logout support
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }

    // Password encoder for hashing passwords (must match how passwords are stored)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
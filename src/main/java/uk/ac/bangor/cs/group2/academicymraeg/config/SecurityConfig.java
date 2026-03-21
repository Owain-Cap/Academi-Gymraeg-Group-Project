package uk.ac.bangor.cs.group2.academicymraeg.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

//	@Bean
//	public UserDetailsService userDetailsService() {
//		UserDetails student = User.builder().username("student").password("{noop}password").roles("STUDENT").build();
//
//		UserDetails instructor = User.builder().username("instructor").password("{noop}password").roles("INSTRUCTOR")
//				.build();
//
//		UserDetails admin = User.builder().username("admin").password("{noop}password").roles("ADMIN").build();
//		
//		return new InMemoryUserDetailsManager(student, instructor, admin);
//	}

	@Bean
	public SecurityFilterChain permissionsFilter(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated() // access to any page requires a login
				).formLogin(withDefaults());
// For testing purposes
//		http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
}

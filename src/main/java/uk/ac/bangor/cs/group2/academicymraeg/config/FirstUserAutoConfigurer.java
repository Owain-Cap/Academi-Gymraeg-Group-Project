package uk.ac.bangor.cs.group2.academicymraeg.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import uk.ac.bangor.cs.group2.academicymraeg.models.User;
import uk.ac.bangor.cs.group2.academicymraeg.repository.UserRepository;

@Component
public class FirstUserAutoConfigurer {

	
	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	
	
	
	public FirstUserAutoConfigurer(UserRepository userRepo, PasswordEncoder passwordEncoder) {
		super();
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}

	@PostConstruct
	public void createFirstUser() {
		try {
			if (!userRepo.existsByUsername("admin")) {
				User firstUser = new User();
				firstUser.setUsername("admin");
				firstUser.setPasswordHash(passwordEncoder.encode("password"));
				firstUser.setEmail("admin@local");
				firstUser.setFirstName("System");
				firstUser.setLastName("Administrator");
				firstUser.setIsInstructor(User.IsInstructor.NO);
				firstUser.setIsAdmin(User.IsAdmin.YES);
				
				userRepo.save(firstUser);
			}
		} catch (Exception e) {
			System.err.println("Could not create first user. " + e.getMessage());
		}
	}

}

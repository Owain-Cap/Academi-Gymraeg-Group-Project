//package uk.ac.bangor.cs.group2.academicymraeg.config;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import uk.ac.bangor.cs.group2.academicymraeg.models.User;
//import uk.ac.bangor.cs.group2.academicymraeg.models.User.Role;
//import uk.ac.bangor.cs.group2.academicymraeg.repository.UserRepository;
//
//@Configuration
//public class UserDataInitialiser {
//
//	@Bean
//	CommandLineRunner userDatainitialiser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//		return args -> {
//			User user = new User();
//			user.setUsername("testStudent");
//			user.setPasswordHash(passwordEncoder.encode("password"));
//			user.setEmail("student@test.com");
//			user.setFirstName("Bob");
//			user.setLastName("Bobson");
//			user.setRole(Role.STUDENT);
//
//			userRepository.save(user);
//
//			System.out.println("User saved to DB:" + user);
//		};
//	}
//}

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
		
		//Use repo to find user from the DB
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		
		//return the user credentials andconvert data for UserDetails
		return org.springframework.security.core.userdetails.User 
				.withUsername(user.getUsername())
				.password(user.getPasswordHash())
				.roles(user.getRole().name())
				.build();
	}

}

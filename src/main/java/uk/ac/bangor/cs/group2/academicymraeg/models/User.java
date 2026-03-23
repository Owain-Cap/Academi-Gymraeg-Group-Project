package uk.ac.bangor.cs.group2.academicymraeg.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	
	private String username;
	private String passwordHash;
	private String email;
	private String firstName;
	private String lastName;

	public enum Role {
		STUDENT, INSTRUCTOR, SYSTEM_ADMIN

	};

//	private boolean enabled;
	@Enumerated(EnumType.STRING)
	private Role role;

	public User() {

	}

	//Constructors
	public User(Long userId, String username, String passwordHash, String email, String firstName, String lastName,
			boolean enabled, Role role) {
		super();
		this.userId = userId;
		this.username = username;
		this.passwordHash = passwordHash;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
//		this.enabled = enabled;
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

//	public boolean isEnabled() {
//		return enabled;
//	}

//	public void setEnabled(boolean enabled) {
//		this.enabled = enabled;
//	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Long getUserId() {
		return userId;
	}
	
	// Used for editing a user
	public void setUserId(Long userId) {
		this.userId = userId;
	}


	@Override
	public String toString() {
		return "User [userId=" + userId + ", username=" + username + ", passwordHash=" + passwordHash + ", email="
				+ email + ", firstName=" + firstName + ", lastName=" + lastName + ", role="
				+ role + "]";
	}

}
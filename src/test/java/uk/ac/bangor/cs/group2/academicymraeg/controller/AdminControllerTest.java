package uk.ac.bangor.cs.group2.academicymraeg.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;

import uk.ac.bangor.cs.group2.academicymraeg.models.User;
import uk.ac.bangor.cs.group2.academicymraeg.service.UserService;

class AdminControllerTest {

	@Mock
	private UserService userService;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private Model model;

	@InjectMocks
	private AdminController adminController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void AddNewUserWhenUsernameDoesNotExist() {

		User user = new User();
		user.setUsername("testuser");
		user.setPasswordHash("plainPassword"); // before encoding

		// Username does NOT exist
		when(userService.getUserByUsername("testuser")).thenReturn(Optional.empty());

		// Mock password encoding
		when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");

		// Act
		String result = adminController.saveNewUser(user, model);

		// Assert
		assertEquals("redirect:/admin", result);

		// Password should be encoded
		assertEquals("encodedPassword", user.getPasswordHash());

		// Save should be called
		verify(userService, times(1)).saveUser(user);
	}
	@Test
	void AddUserWhenUsernameAlreadyExists() {
	    // Arrange
	    User user = new User();
	    user.setUsername("existingUser");
	    user.setPasswordHash("plainPassword");

	    // Username DOES exist
	    when(userService.getUserByUsername("existingUser"))
	            .thenReturn(Optional.of(new User()));

	    // Act
	    String result = adminController.saveNewUser(user, model);

	    // Assert
	    assertEquals("NewUser", result);

	    // Should NOT save the user
	    verify(userService, never()).saveUser(any());

	    // Should NOT encode password
	    verify(passwordEncoder, never()).encode(any());

	    // Should add error message to model
	    verify(model).addAttribute(
	            eq("errorMessage"),
	            contains("already exists")
	    );
	}
}
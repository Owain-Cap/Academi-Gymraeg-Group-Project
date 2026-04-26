package uk.ac.bangor.cs.group2.academicymraeg.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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

	@Mock
	private Authentication authentication;

	@Mock
	private SecurityContext securityContext;

	@InjectMocks
	private AdminController adminController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		SecurityContextHolder.setContext(securityContext);
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

	@Test
	void AdminPageReturnsAdminViewWithUsers() {
		// Arrange
		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(authentication.getName()).thenReturn("adminUser");
		
		List<User> users = Arrays.asList(new User(), new User());
		when(userService.getAllUsers()).thenReturn(users);

		// Act
		String result = adminController.adminPage(model);

		// Assert
		assertEquals("admin", result);
		verify(model).addAttribute("users", users);
		verify(model).addAttribute("username", "adminUser");
	}

	@Test
	void ShowNewUserFormReturnsNewUserView() {
		// Arrange
		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(authentication.getName()).thenReturn("adminUser");

		// Act
		String result = adminController.showNewUserForm(model);

		// Assert
		assertEquals("NewUser", result);
		verify(model).addAttribute(eq("user"), any(User.class));
		verify(model).addAttribute("username", "adminUser");
	}

	@Test
	void SaveExistingUserWithNewPassword() {
		// Arrange
		User updatedUser = new User();
		updatedUser.setUserId(1L);
		updatedUser.setUsername("existingUser");
		updatedUser.setPasswordHash("newPassword");

		User existingUser = new User();
		existingUser.setUserId(1L);
		existingUser.setPasswordHash("oldEncodedPassword");

		when(userService.getUserById(1L)).thenReturn(Optional.of(existingUser));
		when(passwordEncoder.encode("newPassword")).thenReturn("newEncodedPassword");

		// Act
		String result = adminController.saveNewUser(updatedUser, model);

		// Assert
		assertEquals("redirect:/admin", result);
		assertEquals("newEncodedPassword", updatedUser.getPasswordHash());
		verify(userService).saveUser(updatedUser);
	}

	@Test
	void SaveExistingUserWithBlankPassword() {
		// Arrange
		User updatedUser = new User();
		updatedUser.setUserId(1L);
		updatedUser.setUsername("existingUser");
		updatedUser.setPasswordHash("");

		User existingUser = new User();
		existingUser.setUserId(1L);
		existingUser.setPasswordHash("oldEncodedPassword");

		when(userService.getUserById(1L)).thenReturn(Optional.of(existingUser));

		// Act
		String result = adminController.saveNewUser(updatedUser, model);

		// Assert
		assertEquals("redirect:/admin", result);
		assertEquals("oldEncodedPassword", updatedUser.getPasswordHash());
		verify(userService).saveUser(updatedUser);
	}

	@Test
	void ShowEditUserFormReturnsNewUserViewWithClearedPassword() {
		// Arrange
		User existingUser = new User();
		existingUser.setUserId(1L);
		existingUser.setPasswordHash("encodedPassword");

		when(userService.getUserById(1L)).thenReturn(Optional.of(existingUser));

		// Act
		String result = adminController.showEditUserForm(1L, model);

		// Assert
		assertEquals("NewUser", result);
		assertEquals("", existingUser.getPasswordHash()); // Ensure password hash is cleared for the form
		verify(model).addAttribute("user", existingUser);
	}
	
	@Test
	void ShowEditUserFormThrowsExceptionIfUserNotFound() {
		// Arrange
		when(userService.getUserById(1L)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(IllegalArgumentException.class, () -> {
			adminController.showEditUserForm(1L, model);
		});
	}

	@Test
	void DeleteUserPreventsSelfDeletion() {
		// Arrange
		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(authentication.getName()).thenReturn("adminUser");

		User selfUser = new User();
		selfUser.setUserId(1L);
		selfUser.setUsername("adminUser");

		when(userService.getUserById(1L)).thenReturn(Optional.of(selfUser));

		// Act
		String result = adminController.deleteUser(1L);

		// Assert
		assertEquals("redirect:/admin?error=cannotDeleteSelf", result);
		verify(userService, never()).deleteUserById(any());
	}

	@Test
	void DeleteUserAllowsDeletingOthers() {
		// Arrange
		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(authentication.getName()).thenReturn("adminUser");

		User otherUser = new User();
		otherUser.setUserId(2L);
		otherUser.setUsername("otherUser");

		when(userService.getUserById(2L)).thenReturn(Optional.of(otherUser));

		// Act
		String result = adminController.deleteUser(2L);

		// Assert
		assertEquals("redirect:/admin", result);
		verify(userService).deleteUserById(2L);
	}
}
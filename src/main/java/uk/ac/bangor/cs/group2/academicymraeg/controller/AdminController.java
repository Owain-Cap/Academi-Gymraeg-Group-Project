package uk.ac.bangor.cs.group2.academicymraeg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import uk.ac.bangor.cs.group2.academicymraeg.models.User;
import uk.ac.bangor.cs.group2.academicymraeg.service.UserService;

/**
 * Controller class for mapping user requests to the Admin Dashboard. Interacts
 * with the UserService to provide data to the admin views.
 */
@Controller
public class AdminController {

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public AdminController(UserService userService, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * Handles HTTP GET requests for the "/admin" URL. Fetches all users from the
	 * database and adds them to the Model so they can be displayed on the
	 * admin.html Thymeleaf template.
	 *
	 * @param model Spring's Model object used to pass data to the view.
	 * @return The name of the Thymeleaf template to render ("admin").
	 */
	@GetMapping("/admin")
	public String adminPage(Model model) {
		model.addAttribute("users", userService.getAllUsers()); // gets all users from database
		return "admin"; // returns the admin.html template
	}

	// Show the new/edit user form
	@GetMapping("/register")
	public String showNewUserForm(Model model) {
		model.addAttribute("user", new User());
		return "NewUser";
	}

	// Handle form submission for Add/Edit
	@PostMapping("/register")
	public String saveNewUser(@ModelAttribute("user") User user, Model model) {
		if (user.getUserId() == 0) { // defaults to 0 for new users
			// New user validation
			if (userService.getUserByUsername(user.getUsername()).isPresent()) {
				model.addAttribute("errorMessage", "Username " + user.getUsername() + " already exists.");
				return "NewUser";
			}
			user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
		} else {
			// Updating existing user
			User existingUser = userService.getUserById(user.getUserId()).orElse(null);
			if (existingUser != null) {
				if (user.getPasswordHash() == null || user.getPasswordHash().isEmpty()) {
					// They left password blank, keep original password
					user.setPasswordHash(existingUser.getPasswordHash());
				} else {
					// They provided a new password, encode it
					user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
				}
			}
		}
		userService.saveUser(user);
		return "redirect:/admin";
	}

	// Show the edit user form
	@GetMapping("/admin/users/edit/{id}")
	public String showEditUserForm(@PathVariable("id") long id, Model model) {
		User user = userService.getUserById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		// Clear the password hash so the form doesn't show the Bcrypt hash
		user.setPasswordHash("");
		model.addAttribute("user", user);
		return "NewUser";
	}

	// Delete a user
	@PostMapping("/admin/users/delete/{id}")
	public String deleteUser(@PathVariable("id") long id) {
		userService.deleteUserById(id);
		return "redirect:/admin";
	}
}
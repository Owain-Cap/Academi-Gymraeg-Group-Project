package uk.ac.bangor.cs.group2.academicymraeg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	String username = auth.getName();
    	model.addAttribute("username", username);
		return "admin"; // returns the admin.html template
	}

	/**
	 * Displays the form for creating a new user.
	 * Injects an empty User object into the model for Thymeleaf to bind form data to.
	 *
	 * @param model Spring's Model object used to pass data to the view.
	 * @return The name of the Thymeleaf template to render ("NewUser").
	 */
	@GetMapping("/register")
	public String showNewUserForm(Model model) {
		model.addAttribute("user", new User());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	String username = auth.getName();
    	model.addAttribute("username", username);
		return "NewUser";
	}

	/**
	 * Handles form submission for adding a new user or editing an existing user.
	 * Performs validation to ensure usernames are unique if creating a new user,
	 * and hashes passwords using BCrypt before sending to the database.
	 *
	 * @param user  The User object populated from the form data.
	 * @param model Spring's Model object to pass error messages if validation fails.
	 * @return A redirect to the "/admin" dashboard on success, or back to the form on failure.
	 */
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

	/**
	 * Displays the form for editing an existing user.
	 * Retrieves the user by ID and clears the password hash to prevent the BCrypt string
	 * from rendering visibly inside the form input.
	 *
	 * @param id    The Id of the user to edit, extracted from the PathVariable.
	 * @param model Springs Model object used to pass the selected user to the view.
	 * @return The name of the Thymeleaf template to render ("NewUser").
	 */
	@GetMapping("/admin/users/edit/{id}")
	public String showEditUserForm(@PathVariable("id") long id, Model model) {
		User user = userService.getUserById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		// Clear the password hash so the form doesn't show the Bcrypt hash
		user.setPasswordHash("");
		model.addAttribute("user", user);
		return "NewUser";
	}

	/**
	 * Processes the deletion of a user.
	 * Enforces a backend security check to prevent the currently logged-in administrator
	 * from accidentally or maliciously deleting their own account.
	 *
	 * @param id The ID of the user to delete, extracted from the PathVariable.
	 * @return A redirect to the "/admin" dashboard (with an error parameter if deletion of the admin is attempted).
	 */
	@PostMapping("/admin/users/delete/{id}")
	public String deleteUser(@PathVariable("id") long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = auth.getName();
		
		User userToDelete = userService.getUserById(id).orElse(null);
		if (userToDelete != null && userToDelete.getUsername().equals(currentUsername)) {
			// Prevent admin from deleting themselves
			return "redirect:/admin?error=cannotDeleteSelf";
		}
		
		userService.deleteUserById(id);
		return "redirect:/admin";
	}
}
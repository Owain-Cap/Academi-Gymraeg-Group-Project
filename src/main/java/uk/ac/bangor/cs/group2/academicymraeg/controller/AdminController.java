package uk.ac.bangor.cs.group2.academicymraeg.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import uk.ac.bangor.cs.group2.academicymraeg.models.User;
import uk.ac.bangor.cs.group2.academicymraeg.service.UserService;

@Controller
public class AdminController {

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	public AdminController(UserService userService, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}

	// Show the new user form
	@GetMapping("/register")
	public String showNewUserForm(Model model) {
		model.addAttribute("user", new User());
		return "NewUser";
	}

	// Handle form submission
	@PostMapping("/register")
	public String saveNewUser(@ModelAttribute("user") User user, Model model) {
		user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
		userService.saveUser(user);
		model.addAttribute("successMessage", "User " + user.getUsername() + " has been added successfully.");
		model.addAttribute("user", new User());
		return "NewUser";
	}
}
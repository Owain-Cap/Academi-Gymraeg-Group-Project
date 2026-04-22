package uk.ac.bangor.cs.group2.academicymraeg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class for mapping requests for the login page.
 */
@Controller
public class LoginController {

	/**
	 * Handles HTTP GET requests for the "/login" URL.
	 * Displays the login page.
	 *
	 * @return The name of the Thymeleaf template to render ("login").
	 */
	@GetMapping("/login")
	public String login() {
		return "login";
	}
}

package uk.ac.bangor.cs.group2.academicymraeg.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Handles requests for the landing page.
 */
@Controller
public class GreetingController {

	/**
	 * Shows the landing page and adds the logged in username to the model.
	 *
	 * @param model the model used by the view
	 * @return the landing page template name
	 */
	@GetMapping("/")
	public String landing(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		model.addAttribute("username", username);
		return "landing-page";
	}
}

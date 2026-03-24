package uk.ac.bangor.cs.group2.academicymraeg.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GreetingController {

	@GetMapping("/")
	public String greeting(Principal principal, Model model) {
	    String username = principal.getName();
	    model.addAttribute("name", username);
	    return "greeting";
	}
    }


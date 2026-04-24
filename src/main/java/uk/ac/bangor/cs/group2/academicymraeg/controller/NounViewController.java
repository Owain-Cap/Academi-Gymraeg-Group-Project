package uk.ac.bangor.cs.group2.academicymraeg.controller;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import uk.ac.bangor.cs.group2.academicymraeg.models.Noun;
import uk.ac.bangor.cs.group2.academicymraeg.service.NounService;

@Controller
public class NounViewController {

	private final NounService nounService;

	public NounViewController(NounService nounService) {
		this.nounService = nounService;
	}

	// Main Noun Page
	@GetMapping("/nouns")
	public String getNouns(@RequestParam(required = false) String search, Model model) {

		//remove white space from input if its not empty
		search = search == null ? null : search.trim();
		
		// this is the search function on the noun page
		if (search != null && !search.isEmpty()) {
			model.addAttribute("nouns", nounService.searchNouns(search));
		} else {
			model.addAttribute("nouns", nounService.getAllNouns());
		}

		model.addAttribute("search", search);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		model.addAttribute("username", username);

		return "noun";
	}

	// This is to add the new noun
	@GetMapping("/nouns/edit")
	public String addNoun(Model model) {
		Noun noun = new Noun();
		noun.setCreatedAt(LocalDateTime.now());
		model.addAttribute("noun", noun);
		model.addAttribute("isNew", true);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		model.addAttribute("username", username);
		return "editNoun";
	}

	// editing a noun by ID
	@GetMapping("/nouns/edit/{nounId}")
	public String editNoun(@PathVariable Long nounId, Model model) {
		Noun noun = nounService.getNounById(nounId);
		model.addAttribute("noun", noun);
		model.addAttribute("isNew", false);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		model.addAttribute("username", username);
		return "editNoun";
	}

	// this saves the Noun
	@PostMapping("/nouns/save")
	public String saveNoun(@ModelAttribute Noun noun, Authentication authentication, Model model) {

		//remove white space from user input
		noun.setEnglish(noun.getEnglish().trim());
		noun.setWelsh(noun.getWelsh().trim());
		
		if (!nounService.isValid(noun.getEnglish()) || !nounService.isValid(noun.getWelsh())) {
			model.addAttribute("nounError", "Nouns must only contain letters");
			model.addAttribute("noun", noun);
			model.addAttribute("isNew", noun.getNounId() == null);
			return "editNoun";
		}

		//check the noun isn't already in the database
		if (noun.getNounId() == null && nounService.nounExists(noun.getEnglish(), noun.getWelsh())) {
			model.addAttribute("nounError", "This word is already saved");
			model.addAttribute("noun", noun);
			model.addAttribute("isNew", true);
			return "editNoun";
		}

		noun.setCreatedAt(LocalDateTime.now());
		noun.setCreatedByUsername(authentication.getName()); // get the username of the person adding the noun
		nounService.saveNoun(noun);

		return "redirect:/nouns";
	}

	// deletes the noun
	@PostMapping("/nouns/delete/{nounId}")
	public String deleteNoun(@PathVariable Long nounId) {
		nounService.deleteNoun(nounId);
		return "redirect:/nouns";
	}
}
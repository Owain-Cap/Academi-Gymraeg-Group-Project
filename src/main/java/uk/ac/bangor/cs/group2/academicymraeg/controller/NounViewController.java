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

/**
 * controller responsible for handling the web request related to the nouns 
 * 
 * this includes displaying the nouns, searching for a noun, adding, editing,deleting and saving a noun
 */

@Controller
public class NounViewController {
	
	private final NounService nounService;
	
	/**
	 * constructor for NounViewController
	 * 
	 * @param  nounService service used to handle noun login
	 */

	public NounViewController(NounService nounService) {
		this.nounService = nounService;
	}
	
	/**
	 * this displays the main noun page 
	 * 
	 * when the user uses the search bar, it will return a result that matches what the user has typed 
	 * 
	 * @param search this is optional search that will allow the user to search for a specific noun
	 * @param model used to send data to the Thymeleaf page 
	 * @return the noun page
	 */

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

	/**
	 * opens the edit noun page for adding a new noun.
	 * a new empty noun object is created and sent to the page.
	 *
	 * @param model used to send the noun object to the page
	 * @return the edit noun page
	 */
	
	//This is to add the new noun
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

	
	/**
	 * opens the edit noun page for an existing noun.
	 * to edit the noun the noun is found by the noun ID and sent to the page so the user can edit it 
	 *
	 * @param nounId the ID of the noun being edited
	 * @param model used to send the noun data to the page
	 * @return the edit noun page
	 */
	
	//editing a noun by ID
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


	
	/**
	 * saves a noun after it has been added or edited.
	 *
	 * the method validates that both the English and Welsh fields only contain
	 * valid letters. 
	 * 
	 * if the validation fails, the user stays on the edit page
	 * and an error message is shown.
	 *
	 * @param noun the noun object submitted from the form
	 * @param authentication contains the details of the login in user this is what gets the username
	 * from the login 
	 * @param model used to send data back to the page if validation fails
	 * @return redirect to the noun page if saved, otherwise the edit noun page
	 */
	
	//this saves the Noun
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
	
	
	/**
	 * deletes a noun by using the noun ID
	 *
	 * @param nounId the ID of the noun to delete
	 * @return redirect to the noun page
	 */

	// deletes the noun
	@PostMapping("/nouns/delete/{nounId}")
	public String deleteNoun(@PathVariable Long nounId) {
		nounService.deleteNoun(nounId);
		return "redirect:/nouns";
	}
}
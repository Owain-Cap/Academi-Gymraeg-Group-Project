package uk.ac.bangor.cs.group2.academicymraeg.controller;

import java.time.LocalDateTime;

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
public class NounView {

	private final NounService nounService;

	public NounView(NounService nounService) {
		this.nounService = nounService;
	}

	//Main Noun Page
	@GetMapping("/nouns")
	public String getNouns(@RequestParam(required = false) String search, Model model) {

		//this is the search function on the noun page
	    if (search != null && !search.isEmpty()) {
	        model.addAttribute("nouns", nounService.searchNouns(search));
	    } else {
	        model.addAttribute("nouns", nounService.getAllNouns());
	    }

	    model.addAttribute("search", search);

	    return "noun";
	}

	//This is to add the new noun
	@GetMapping("/nouns/edit")
	public String addNoun(Model model) {
		Noun noun = new Noun();
		noun.setCreatedAt(LocalDateTime.now()); 
		model.addAttribute("noun", noun);
		model.addAttribute("isNew", true);
		return "editNoun";
	}

	//editing a noun by ID
	@GetMapping("/nouns/edit/{nounId}")
	public String editNoun(@PathVariable Long nounId, Model model) {
		Noun noun = nounService.getNounById(nounId);
		model.addAttribute("noun", noun);
		model.addAttribute("isNew", false);
		return "editNoun";
	}

	//this saves the Noun
	@PostMapping("/nouns/save")
	public String saveNoun(@ModelAttribute Noun noun) {
		noun.setCreatedAt(LocalDateTime.now());
		nounService.saveNoun(noun);
		return "redirect:/nouns";
	}

	//deletes the noun
	@PostMapping("/nouns/delete/{nounId}")
	public String deleteNoun(@PathVariable Long nounId) {
		nounService.deleteNoun(nounId);
		return "redirect:/nouns";
	}
}
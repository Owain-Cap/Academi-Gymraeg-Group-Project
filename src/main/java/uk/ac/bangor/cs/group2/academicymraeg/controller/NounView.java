package uk.ac.bangor.cs.group2.academicymraeg.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import uk.ac.bangor.cs.group2.academicymraeg.models.Noun;
import uk.ac.bangor.cs.group2.academicymraeg.service.NounService;

@Controller
public class NounView {

	private final NounService nounService;

	public NounView(NounService nounService) {
		this.nounService = nounService;
	}


	@GetMapping("/nouns")
	public String getNouns(Model model) {
		model.addAttribute("nouns", nounService.getAllNouns());
		return "noun";
	}

	@GetMapping("/nouns/edit")
	public String addNoun(Model model) {
		Noun noun = new Noun();
		noun.setCreatedAt(LocalDateTime.now()); 
		model.addAttribute("noun", noun);
		model.addAttribute("isNew", true);
		return "editNoun";
	}


	@GetMapping("/nouns/edit/{nounId}")
	public String editNoun(@PathVariable Long nounId, Model model) {
		Noun noun = nounService.getNounById(nounId);
		model.addAttribute("noun", noun);
		model.addAttribute("isNew", false);
		return "editNoun";
	}


	@PostMapping("/nouns/save")
	public String saveNoun(@ModelAttribute Noun noun) {
		noun.setCreatedAt(LocalDateTime.now());
		nounService.saveNoun(noun);
		return "redirect:/nouns";
	}

	
	@PostMapping("/nouns/delete/{nounId}")
	public String deleteNoun(@PathVariable Long nounId) {
		nounService.deleteNoun(nounId);
		return "redirect:/nouns";
	}
}
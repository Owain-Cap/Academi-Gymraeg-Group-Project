package uk.ac.bangor.cs.group2.academicymraeg.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import uk.ac.bangor.cs.group2.academicymraeg.models.Test;
import uk.ac.bangor.cs.group2.academicymraeg.repository.TestRepository;

import java.util.List;

@Controller
public class TestHistoryController {

    private final TestRepository testRepository;

    public TestHistoryController(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @GetMapping("/my-tests")
    public String viewTestHistory(Authentication authentication, Model model) {
        String username = authentication.getName();
        List<Test> tests = testRepository.findByUsernameOrderByCreatedAtDesc(username);
        model.addAttribute("tests", tests);
        return "test-history";
    }
}
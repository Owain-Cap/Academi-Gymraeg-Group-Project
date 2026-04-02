package uk.ac.bangor.cs.group2.academicymraeg.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import uk.ac.bangor.cs.group2.academicymraeg.models.Test;
import uk.ac.bangor.cs.group2.academicymraeg.models.TestQuestions;
import uk.ac.bangor.cs.group2.academicymraeg.repository.TestQuestionRepository;
import uk.ac.bangor.cs.group2.academicymraeg.repository.TestRepository;

@Controller
public class TestHistoryController {

    private final TestRepository testRepository;
	private TestQuestionRepository testQuestionRepository;
    

    public TestHistoryController(TestRepository testRepository, TestQuestionRepository testQuestionRepository) {
        this.testRepository = testRepository;
        this.testQuestionRepository = testQuestionRepository;
    }

    @GetMapping("/my-tests")
    public String viewTestHistory(Authentication authentication, Model model) {
        String username = authentication.getName();
        List<Test> tests = testRepository.findByUsernameOrderByCreatedAtDesc(username);
        model.addAttribute("tests", tests);
        return "test-history";
    }
    
    
    @GetMapping("/review-test/{testId}")
    public String reviewTest(@PathVariable Long testId, Authentication authentication, Model model) {
        String username = authentication.getName();
        Test test = testRepository.findById(testId)
        	    .orElseThrow(() -> new IllegalArgumentException("Test not found"));

        if (!test.getUsername().equals(username)) {
            throw new IllegalArgumentException("Not allowed");
        }

        // Load test questions
        List<TestQuestions> questions = testQuestionRepository.findByTestOrderByPositionAsc(test);

        model.addAttribute("test", test);
        model.addAttribute("questions", questions);
        return "review-test";
    }
}
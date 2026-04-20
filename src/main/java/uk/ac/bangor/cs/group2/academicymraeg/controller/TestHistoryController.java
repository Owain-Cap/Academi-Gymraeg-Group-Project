package uk.ac.bangor.cs.group2.academicymraeg.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import uk.ac.bangor.cs.group2.academicymraeg.models.Test;
import uk.ac.bangor.cs.group2.academicymraeg.models.TestQuestions;
import uk.ac.bangor.cs.group2.academicymraeg.repository.TestQuestionRepository;
import uk.ac.bangor.cs.group2.academicymraeg.repository.TestRepository;
import uk.ac.bangor.cs.group2.academicymraeg.service.TestGeneratorService;

@Controller
public class TestHistoryController {

    private final TestRepository testRepository;
	private TestQuestionRepository testQuestionRepository;
	private final TestGeneratorService testGeneratorService;
    

    public TestHistoryController(TestRepository testRepository, TestQuestionRepository testQuestionRepository, TestGeneratorService testGeneratorService) {
        this.testRepository = testRepository;
        this.testQuestionRepository = testQuestionRepository;
        this.testGeneratorService = testGeneratorService;
    }

    @GetMapping("/my-tests")
    public String viewTestHistory(Authentication authentication, Model model) {
       
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	String username = auth.getName();
    	model.addAttribute("username", username);
        
        
        //if there's an active test 
        Test activeTest = testGeneratorService.getActiveTest(username);
        if (activeTest != null) {
            model.addAttribute("activeTestId", activeTest.getTestId());
            model.addAttribute("message", "You cannot access previous tests while a test is in progress.");
            return "test-locked";
        }
        
        List<Test> tests = testRepository.findByUsernameAndStatusOrderByCreatedAtDesc(username, Test.TestStatus.SUBMITTED);
        
      //converts the score to a percentage
        for (Test test : tests) {
        	int percentage = (int) Math.round((test.getResult() * 100.0) / 20);
            test.setResult(percentage);
        }
        
        model.addAttribute("tests", tests);
        return "test-history";
    }
    
    
    @GetMapping("/review-test/{testId}")
    public String reviewTest(@PathVariable Long testId, Authentication authentication, Model model) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	String username = auth.getName();
    	model.addAttribute("username", username);
        
     // if the user has an active test, show the locked page instead
        Test activeTest = testGeneratorService.getActiveTest(username);
        if (activeTest != null) {
            model.addAttribute("activeTestId", activeTest.getTestId());
            model.addAttribute("message", "You cannot access previous tests while a test is in progress.");
            return "test-locked";
        }
        
        Test test = testRepository.findById(testId)
        	    .orElseThrow(() -> new IllegalArgumentException("Test not found"));

        if (!test.getUsername().equals(username)) {
            throw new IllegalArgumentException("Not allowed");
        }
        

        // Load test questions
        List<TestQuestions> questions = testQuestionRepository.findByTestOrderByPositionAsc(test);
        
        //converts the score to a percentage
        int percentage = (int) Math.round((test.getResult() * 100.0) / 20);
        test.setResult(percentage);
        
        long correctCount = questions.stream()
    			.filter(q -> q.getAnswerStatus() == TestQuestions.AnswerStatus.CORRECT)
    			.count();
        
        long incorrectCount = questions.stream()
    			.filter(q -> q.getAnswerStatus() == TestQuestions.AnswerStatus.INCORRECT)
    			.count();

    	long skippedCount = questions.stream()
    			.filter(q -> q.getAnswerStatus() == TestQuestions.AnswerStatus.SKIPPED)
    			.count();

        model.addAttribute("test", test);
        model.addAttribute("questions", questions);
        model.addAttribute("correctCount",correctCount);
        model.addAttribute("incorrectCount",incorrectCount);
        model.addAttribute("skippedCount",skippedCount);
        
        return "review-test";
    }
}
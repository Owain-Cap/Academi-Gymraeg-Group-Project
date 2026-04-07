package uk.ac.bangor.cs.group2.academicymraeg.controller;

import java.net.http.HttpResponse;
import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletResponse;
import uk.ac.bangor.cs.group2.academicymraeg.models.Test;
import uk.ac.bangor.cs.group2.academicymraeg.models.TestQuestions;
import uk.ac.bangor.cs.group2.academicymraeg.models.TestSubmissionForm;
import uk.ac.bangor.cs.group2.academicymraeg.service.TestGeneratorService;

@Controller
public class TakeTestController {
	private final TestGeneratorService testGeneratorService;

	public TakeTestController(TestGeneratorService testGeneratorService) {
		this.testGeneratorService = testGeneratorService;
	}

//	@GetMapping("/tests/take")
//	public String startTest() {
//		Test test = testGeneratorService.generateTestForUser("demoUser");
//		return "redirect:/tests/take/" + test.getTestId();
//	}

	@GetMapping("/tests/take")
	public String startTest(Principal principal) {
		// Get username
		String username = principal.getName();
		Test test = testGeneratorService.generateTestForUser(username);
		return "redirect:/tests/take/" + test.getTestId();
	}

	@GetMapping("/tests/take/{testId}")
	public String showTest(@PathVariable long testId, Principal principal, Model model, HttpServletResponse httpResponse) {
		
		//Stop browser from caching the page so a refresh is always triggered on load
		httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		httpResponse.setHeader("Pragma", "no-cache");
		httpResponse.setDateHeader("Expires", 0); 
		
		Test test = testGeneratorService.getTestById(testId);

		// make sure this is the users own test
		if (!test.getUsername().equals(principal.getName())) {
			throw new IllegalArgumentException("You do not have access to this test");
		}

		// check this test is not already submitted
		// if it is redirect to view results
		if (test.getStatus() == Test.TestStatus.SUBMITTED) {
			return "redirect:/tests/result/" + testId;
		}

		// render new or in progress test
		List<TestQuestions> questions = testGeneratorService.getQuestionsForTest(test);

		TestSubmissionForm form = new TestSubmissionForm();
		for (int i = 0; i < questions.size(); i++) {
			form.getAnswers().add("");
		}

		model.addAttribute("test", test);
		model.addAttribute("questions", questions);
		model.addAttribute("testSubmissionForm", form);

		return "take-test";
	}

	@PostMapping("/tests/take/{testId}")
	public String submitTest(@PathVariable long testId, @ModelAttribute TestSubmissionForm testSubmissionForm,
			Principal principal, Model model) {
		
		try {
			testGeneratorService.submitTest(testId, testSubmissionForm.getAnswers());
			return "redirect:/tests/result/" + testId;
		} catch (IllegalArgumentException | IllegalStateException e) {
			Test test = testGeneratorService.getTestById(testId);

			//if a user tries to access a test that is not their own throw error
			if (!test.getUsername().equals(principal.getName())) {
				throw new IllegalArgumentException("You do not have access to this test");
			}

			//if a user tried to alter a submitted test redirect them to the submitted test result
			if (test.getStatus() == Test.TestStatus.SUBMITTED) {
				return "redirect:/tests/result/" + testId;
			}

			List<TestQuestions> questions = testGeneratorService.getQuestionsForTest(test);
			model.addAttribute("test", test);
			model.addAttribute("questions", questions);
			model.addAttribute("testSubmissionForm", testSubmissionForm);
			model.addAttribute("errorMessage", e.getMessage());

			return "take-test";
		}
	}

	@GetMapping("/tests/result/{testId}")
	public String showResult(@PathVariable long testId, Principal principal, Model model) {
		
		Test test = testGeneratorService.getTestById(testId);

		// Check the test belongs to this user
		if (!test.getUsername().equals(principal.getName())) {
			throw new IllegalArgumentException("You do not have access to this test");
		}

		// check the test is submitted and not in progress.
		if (test.getStatus() != Test.TestStatus.SUBMITTED) {
			return "redirect:/tests/take/" + testId;
		}

		List<TestQuestions> questions = testGeneratorService.getQuestionsForTest(test);

		int correctCount = 0;
		int skippedCount = 0;
		int incorrectCount = 0;

		for (TestQuestions question : questions) {
			if (question.getAnswerStatus() == TestQuestions.AnswerStatus.CORRECT) {
				correctCount++;
			} else if (question.getAnswerStatus() == TestQuestions.AnswerStatus.SKIPPED) {
				skippedCount++;
			} else if (question.getAnswerStatus() == TestQuestions.AnswerStatus.INCORRECT) {
				incorrectCount++;
			}
		}
		model.addAttribute("test", test);
		model.addAttribute("questions", questions);
		model.addAttribute("correctCount", correctCount);
		model.addAttribute("skippedCount", skippedCount);
		model.addAttribute("incorrectCount", incorrectCount);

		return "test-result";
	}

}

//package uk.ac.bangor.cs.group2.academicymraeg.controller;
//
//import java.util.List;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import uk.ac.bangor.cs.group2.academicymraeg.models.Test;
//import uk.ac.bangor.cs.group2.academicymraeg.models.TestQuestions;
//import uk.ac.bangor.cs.group2.academicymraeg.repository.TestQuestionRepository;
//import uk.ac.bangor.cs.group2.academicymraeg.service.TestGeneratorService;
//
//@Controller
//public class TestViewController {
//
//	
//	//ignore this file
//    private final TestGeneratorService testGeneratorService;
//    private final TestQuestionRepository testQuestionRepository;
//
//    public TestViewController(TestGeneratorService testGeneratorService,
//                              TestQuestionRepository testQuestionRepository) {
//        this.testGeneratorService = testGeneratorService;
//        this.testQuestionRepository = testQuestionRepository;
//    }
//
//    @GetMapping("/tests/preview")
//    public String previewTest(Model model) {
//        Test test = testGeneratorService.generateTestForUser("demoUser");
//        List<TestQuestions> questions = testQuestionRepository.findByTestOrderByPositionAsc(test);
//
//        model.addAttribute("test", test);
//        model.addAttribute("questions", questions);
//
//        return "test-preview";
//    }
//}
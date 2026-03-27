package uk.ac.bangor.cs.group2.academicymraeg.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.ac.bangor.cs.group2.academicymraeg.models.Test;
import uk.ac.bangor.cs.group2.academicymraeg.service.TestGeneratorService;

@RestController
public class TestController {

    private final TestGeneratorService testGeneratorService;

    public TestController(TestGeneratorService testGeneratorService) {
        this.testGeneratorService = testGeneratorService;
    }

    @PostMapping("/tests/generate/{username}")
    public Test generateTest(@PathVariable String username) {
        return testGeneratorService.generateTestForUser(username);
    }
}
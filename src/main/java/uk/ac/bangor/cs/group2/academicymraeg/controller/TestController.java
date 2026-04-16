package uk.ac.bangor.cs.group2.academicymraeg.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.ac.bangor.cs.group2.academicymraeg.models.Test;
import uk.ac.bangor.cs.group2.academicymraeg.service.TestGeneratorService;


/**
 * REST controller for handling test generation.
 * 
 * generates a new test for a given user
 * or return an in-progress test if user has one.
 */
@RestController //formats response as JSON
public class TestController {

    private final TestGeneratorService testGeneratorService;

    /**
     * Constructor for injecting the TestGeneratorService dependency.
     * 
     * @param testGeneratorService the service used to generate and manage tests
     */
    public TestController(TestGeneratorService testGeneratorService) {
        this.testGeneratorService = testGeneratorService;
    }

    /**
     * Generates a test for the specified user.
     * 
     * accepts a POST request containing a username in the URL,
     * then calls TestGeneratorService to either create a new test
     * and return it, or return an active, in-progress test if the 
     * user has one
     * 
     * @param username the username extracted from the request path
     * @return a Test object (new-generated or existing-in progress)
     */
    @PostMapping("/tests/generate/{username}")
    //extract username from url, call TestGeneratorService for the user
    public Test generateTest(@PathVariable String username) {
    	//return generate test (as JSON)
        return testGeneratorService.generateTestForUser(username);
    }
}
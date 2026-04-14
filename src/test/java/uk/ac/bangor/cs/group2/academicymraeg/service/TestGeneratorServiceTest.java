package uk.ac.bangor.cs.group2.academicymraeg.service;


import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import uk.ac.bangor.cs.group2.academicymraeg.repository.NounRepository;
import uk.ac.bangor.cs.group2.academicymraeg.repository.TestQuestionRepository;
import uk.ac.bangor.cs.group2.academicymraeg.repository.TestRepository;

@SpringBootTest
class TestGeneratorServiceTest {
	
	@Autowired
    private TestGeneratorService testGeneratorService;
	
	
	@MockitoBean
	private NounRepository nounRepository;
	
	@MockitoBean
	private TestRepository testRepository;
	
	@MockitoBean
	private TestQuestionRepository testQuestionRepository;
	

	@Test
	void testGenerateTestForUser() {
		fail("Not yet implemented");
	}

	@Test
	void testGetTestById() {
		fail("Not yet implemented");
	}

	@Test
	void testSubmitTest() {
		fail("Not yet implemented");
	}

	@Test
	void testHasActiveTest() {
		//sets up mock data
		when(testRepository.existsByUsernameAndStatus("alice", uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.IN_PROGRESS)).thenReturn(true);
		
		//test
		boolean result = testGeneratorService.hasActiveTest("alice");
		
		//check result
		assertTrue(result);
	}
	
	@Test
	void testHasActiveTest_returnsFalseWhenNoActiveTestExists() {
	    when(testRepository.existsByUsernameAndStatus("bob", uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.IN_PROGRESS))
	        .thenReturn(false);

	    boolean result = testGeneratorService.hasActiveTest("bob");

	    assertFalse(result);
	}

	@Test
	void testGetActiveTest() {
		fail("Not yet implemented");
	}

}

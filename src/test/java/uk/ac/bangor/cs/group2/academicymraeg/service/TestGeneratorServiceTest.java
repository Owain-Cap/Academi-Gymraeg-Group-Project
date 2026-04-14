package uk.ac.bangor.cs.group2.academicymraeg.service;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.Optional;

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
		// sets up mock data use has active test
		when(testRepository.existsByUsernameAndStatus("alice",
				uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.IN_PROGRESS)).thenReturn(true);

		// test does the user have an active test
		boolean result = testGeneratorService.hasActiveTest("alice");

		// check result is true
		assertTrue(result);
	}

	@Test
	void testHasActiveTest_returnsFalseWhenNoActiveTestExists() {
		// sets up mock data user doesn't have an active test
		when(testRepository.existsByUsernameAndStatus("bob",
				uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.IN_PROGRESS)).thenReturn(false);

		// test does the user have an active test
		boolean result = testGeneratorService.hasActiveTest("bob");

		// check result is false
		assertFalse(result);
	}

	@Test
	void testGetActiveTest_returnsTest() {
		// sets up test data fake active test
		uk.ac.bangor.cs.group2.academicymraeg.models.Test activeTest = new uk.ac.bangor.cs.group2.academicymraeg.models.Test(
				1, "alice", 0, java.time.LocalDateTime.now(),
				uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.IN_PROGRESS,
				java.time.LocalDateTime.now().plusMinutes(30));
		
		//test request for alice's test
		when(testRepository.findByUsernameAndStatus("alice",
				uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.IN_PROGRESS))
				.thenReturn(java.util.Optional.of(activeTest));

		uk.ac.bangor.cs.group2.academicymraeg.models.Test result = testGeneratorService.getActiveTest("alice");

		//check test data is returned
		assertSame(activeTest, result);
	}
	
	@Test
	void testGetActiveTest_noActiveTest() {
		// sets up mock empty test data
		when(testRepository.findByUsernameAndStatus("bob", uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.IN_PROGRESS)).thenReturn(Optional.empty());
		
		//request a test which has no active data set up
		uk.ac.bangor.cs.group2.academicymraeg.models.Test result = testGeneratorService.getActiveTest("bob");
		
		//check result it Null
		assertNull(result);
	}

}

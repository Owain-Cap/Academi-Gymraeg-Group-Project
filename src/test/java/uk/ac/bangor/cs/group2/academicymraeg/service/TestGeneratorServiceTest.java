package uk.ac.bangor.cs.group2.academicymraeg.service;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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

//--------------getTestById()------
	
	@Test
	void testGetTestById_autoSubmitsExpiredTest() {
		// sets up mock test data for an expired in-progress test
		uk.ac.bangor.cs.group2.academicymraeg.models.Test expiredTest =
			new uk.ac.bangor.cs.group2.academicymraeg.models.Test(
				1,
				"alice",
				0,
				java.time.LocalDateTime.now().minusHours(1),
				uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.IN_PROGRESS,
				java.time.LocalDateTime.now().minusMinutes(1)
			);

		// sets up mock question data
		uk.ac.bangor.cs.group2.academicymraeg.models.TestQuestions unansweredQuestion =
			org.mockito.Mockito.mock(uk.ac.bangor.cs.group2.academicymraeg.models.TestQuestions.class);

		uk.ac.bangor.cs.group2.academicymraeg.models.TestQuestions answeredQuestion =
			org.mockito.Mockito.mock(uk.ac.bangor.cs.group2.academicymraeg.models.TestQuestions.class);

		// mock repo will return an expired test
		when(testRepository.findById(1L)).thenReturn(java.util.Optional.of(expiredTest));

		// mock question list 
		when(testQuestionRepository.findByTestOrderByPositionAsc(expiredTest))
			.thenReturn(java.util.List.of(unansweredQuestion, answeredQuestion));

		// sets up one unanswered question and one ansdwered question
		when(unansweredQuestion.getUserAnswer()).thenReturn(null);
		when(answeredQuestion.getUserAnswer()).thenReturn("cath");

		// request test with ID 1
		uk.ac.bangor.cs.group2.academicymraeg.models.Test result =
			testGeneratorService.getTestById(1L);

		// check unanswered question is marked as skipped
		verify(unansweredQuestion).setUserAnswer("SKIPPED_BY_USER");
		verify(unansweredQuestion).setAnswerStatus(
			uk.ac.bangor.cs.group2.academicymraeg.models.TestQuestions.AnswerStatus.SKIPPED
		);
		verify(testQuestionRepository).save(unansweredQuestion);

		// check answered question is not changed
		verify(answeredQuestion, never()).setUserAnswer("SKIPPED_BY_USER");
		verify(testQuestionRepository, never()).save(answeredQuestion);

		// check test status is changed to submitted
		assertEquals(
			uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.SUBMITTED,
			result.getStatus()
		);

		// check updated test is saved
		verify(testRepository).save(expiredTest);
	}
	
	@Test
	void testGetTestById_returnsTestWhenNotExpired() {
		// sets up mock test data (test is still in progress and not expired)
		uk.ac.bangor.cs.group2.academicymraeg.models.Test test = new uk.ac.bangor.cs.group2.academicymraeg.models.Test(
				1, "alice", 0, java.time.LocalDateTime.now(),
				uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.IN_PROGRESS,
				java.time.LocalDateTime.now().plusMinutes(30));

		//return this test when searched by ID
		when(testRepository.findById(1L)).thenReturn(java.util.Optional.of(test));

		// request test with ID 1
		uk.ac.bangor.cs.group2.academicymraeg.models.Test result = testGeneratorService.getTestById(1L);

		// check that the same test object is returned without submitting
		assertSame(test, result);
	}

	@Test
	void testGetTestById_throwsExceptionWhenNoTestFound() {
		// mock data says no test exists with the ID 1
		when(testRepository.findById(1L)).thenReturn(java.util.Optional.empty());

		// make sure requesting test with ID 1 throws the IllegalArgumentException
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> testGeneratorService.getTestById(1L));

		// make sure exception message is returned and correct
		assertEquals("Test not found", exception.getMessage());
	}

	@Test
	void testSubmitTest() {
		fail("Not yet implemented");
	}

//--------------hasActiveTest()------
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

//--------------getActiveTest()------
	@Test
	void testGetActiveTest_returnsTest() {
		// sets up test data fake active test
		uk.ac.bangor.cs.group2.academicymraeg.models.Test activeTest = new uk.ac.bangor.cs.group2.academicymraeg.models.Test(
				1, "alice", 0, java.time.LocalDateTime.now(),
				uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.IN_PROGRESS,
				java.time.LocalDateTime.now().plusMinutes(30));

		// test request for alice's test
		when(testRepository.findByUsernameAndStatus("alice",
				uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.IN_PROGRESS))
				.thenReturn(java.util.Optional.of(activeTest));

		uk.ac.bangor.cs.group2.academicymraeg.models.Test result = testGeneratorService.getActiveTest("alice");

		// check test data is returned
		assertSame(activeTest, result);
	}

	@Test
	void testGetActiveTest_noActiveTest() {
		// sets up mock empty test data
		when(testRepository.findByUsernameAndStatus("bob",
				uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.IN_PROGRESS)).thenReturn(Optional.empty());

		// request a test which has no active data set up
		uk.ac.bangor.cs.group2.academicymraeg.models.Test result = testGeneratorService.getActiveTest("bob");

		// check result it Null
		assertNull(result);
	}

}

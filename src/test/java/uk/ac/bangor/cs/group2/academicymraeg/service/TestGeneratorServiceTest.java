package uk.ac.bangor.cs.group2.academicymraeg.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;

import java.util.List;
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
	
//--------------------------TESTS-------------------------------//



//--------------getTestById()------

	@Test
	void testGetTestById_submitsExpiredTest() {
		// sets up mock test data for an expired in-progress test
		uk.ac.bangor.cs.group2.academicymraeg.models.Test expiredTest = new uk.ac.bangor.cs.group2.academicymraeg.models.Test(
				1, "alice", 0, java.time.LocalDateTime.now().minusHours(1),
				uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.IN_PROGRESS,
				java.time.LocalDateTime.now().minusMinutes(1));

		// sets up mock question data
		uk.ac.bangor.cs.group2.academicymraeg.models.TestQuestions unansweredQuestion = org.mockito.Mockito
				.mock(uk.ac.bangor.cs.group2.academicymraeg.models.TestQuestions.class);

		uk.ac.bangor.cs.group2.academicymraeg.models.TestQuestions answeredQuestion = org.mockito.Mockito
				.mock(uk.ac.bangor.cs.group2.academicymraeg.models.TestQuestions.class);

		// mock repo will return an expired test
		when(testRepository.findById(1L)).thenReturn(java.util.Optional.of(expiredTest));

		// mock question list
		when(testQuestionRepository.findByTestOrderByPositionAsc(expiredTest))
				.thenReturn(java.util.List.of(unansweredQuestion, answeredQuestion));

		// sets up one unanswered question and one ansdwered question
		when(unansweredQuestion.getUserAnswer()).thenReturn(null);
		when(answeredQuestion.getUserAnswer()).thenReturn("cath");

		// request test with ID 1
		uk.ac.bangor.cs.group2.academicymraeg.models.Test result = testGeneratorService.getTestById(1L);

		// check unanswered question is marked as skipped
		verify(unansweredQuestion).setUserAnswer("SKIPPED_BY_USER");
		verify(unansweredQuestion)
				.setAnswerStatus(uk.ac.bangor.cs.group2.academicymraeg.models.TestQuestions.AnswerStatus.SKIPPED);
		verify(testQuestionRepository).save(unansweredQuestion);

		// check answered question is not changed
		verify(answeredQuestion, never()).setUserAnswer("SKIPPED_BY_USER");
		verify(testQuestionRepository, never()).save(answeredQuestion);

		// check test status is changed to submitted
		assertEquals(uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.SUBMITTED, result.getStatus());

		// check updated test is saved
		verify(testRepository).save(expiredTest);
	}

	@Test
	void testGetTestById_notExpired() {
		// sets up mock test data (test is still in progress and not expired)
		uk.ac.bangor.cs.group2.academicymraeg.models.Test test = new uk.ac.bangor.cs.group2.academicymraeg.models.Test(
				1, "alice", 0, java.time.LocalDateTime.now(),
				uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.IN_PROGRESS,
				java.time.LocalDateTime.now().plusMinutes(30));

		// return this test when searched by ID
		when(testRepository.findById(1L)).thenReturn(java.util.Optional.of(test));

		// request test with ID 1
		uk.ac.bangor.cs.group2.academicymraeg.models.Test result = testGeneratorService.getTestById(1L);

		// check that the same test object is returned without submitting
		assertSame(test, result);
	}

	@Test
	void testGetTestById_noTestFound() {
		// mock data says no test exists with the ID 1
		when(testRepository.findById(1L)).thenReturn(java.util.Optional.empty());

		// make sure requesting test with ID 1 throws the IllegalArgumentException
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> testGeneratorService.getTestById(1L));

		// make sure exception message is returned and correct
		assertEquals("Test not found", exception.getMessage());
	}

//--------------submitTest()--------
	@Test
	void testSubmitTest() {
		// sets up mock test data for an in progress active test
		uk.ac.bangor.cs.group2.academicymraeg.models.Test activeTest = new uk.ac.bangor.cs.group2.academicymraeg.models.Test(
				1, "alice", 0, java.time.LocalDateTime.now(),
				uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.IN_PROGRESS,
				java.time.LocalDateTime.now().plusMinutes(30));
		// sets up mock question, answer == "cath"
		uk.ac.bangor.cs.group2.academicymraeg.models.TestQuestions question = org.mockito.Mockito
				.mock(uk.ac.bangor.cs.group2.academicymraeg.models.TestQuestions.class);

		when(question.getCorrectAnswer()).thenReturn("cath");
		when(question.getQuestionType())
				.thenReturn(uk.ac.bangor.cs.group2.academicymraeg.models.Question.QuestionType.WELSH);

		// mock repo
		when(testRepository.findById(1L)).thenReturn(Optional.of(activeTest));
		when(testQuestionRepository.findByTestOrderByPositionAsc(activeTest)).thenReturn(List.of(question));

		// sets up correct answer (cath)
		List<String> answers = List.of("cath");

		// submit the mock test
		testGeneratorService.submitTest(1L, answers);

		// check answer is recognised as correct
		verify(question)
				.setAnswerStatus(uk.ac.bangor.cs.group2.academicymraeg.models.TestQuestions.AnswerStatus.CORRECT);

		// chesk score updated
		assertEquals(1, activeTest.getResult());

		// check test status is set to submitted
		assertEquals(uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.SUBMITTED, activeTest.getStatus());

		// check the test is saved
		verify(testRepository).save(activeTest);
	}

	@Test
	void testSubmitTest_incorrectAnswer() {
		// sets up mock test data for an in progress active test
		uk.ac.bangor.cs.group2.academicymraeg.models.Test activeTest = new uk.ac.bangor.cs.group2.academicymraeg.models.Test(
				1, "alice", 0, java.time.LocalDateTime.now(),
				uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.IN_PROGRESS,
				java.time.LocalDateTime.now().plusMinutes(30));
		// sets up mock question, answer == "cath"
		uk.ac.bangor.cs.group2.academicymraeg.models.TestQuestions question = org.mockito.Mockito
				.mock(uk.ac.bangor.cs.group2.academicymraeg.models.TestQuestions.class);

		when(question.getCorrectAnswer()).thenReturn("cath");
		when(question.getQuestionType())
				.thenReturn(uk.ac.bangor.cs.group2.academicymraeg.models.Question.QuestionType.WELSH);

		// mock repo
		when(testRepository.findById(1L)).thenReturn(Optional.of(activeTest));
		when(testQuestionRepository.findByTestOrderByPositionAsc(activeTest)).thenReturn(List.of(question));

		// sets up incorrect answer (ci)
		List<String> answers = List.of("ci");

		// submit the mock test
		testGeneratorService.submitTest(1L, answers);

		// check answer is recognised as incorrect
		verify(question)
				.setAnswerStatus(uk.ac.bangor.cs.group2.academicymraeg.models.TestQuestions.AnswerStatus.INCORRECT);

		// check score doesn't increment
		assertEquals(0, activeTest.getResult());

		// check test status is set to submitted
		assertEquals(uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.SUBMITTED, activeTest.getStatus());

		// check the test is saved
		verify(testRepository).save(activeTest);
	}

	@Test
	void testSubmitTest_skippedAnswer() {
		// sets up mock test data for an in progress active test
		uk.ac.bangor.cs.group2.academicymraeg.models.Test activeTest = new uk.ac.bangor.cs.group2.academicymraeg.models.Test(
				1, "alice", 0, java.time.LocalDateTime.now(),
				uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.IN_PROGRESS,
				java.time.LocalDateTime.now().plusMinutes(30));
		// sets up mock question, answer == "cath"
		uk.ac.bangor.cs.group2.academicymraeg.models.TestQuestions question = org.mockito.Mockito
				.mock(uk.ac.bangor.cs.group2.academicymraeg.models.TestQuestions.class);

		when(question.getCorrectAnswer()).thenReturn("cath");
		when(question.getQuestionType())
				.thenReturn(uk.ac.bangor.cs.group2.academicymraeg.models.Question.QuestionType.WELSH);

		// mock repo
		when(testRepository.findById(1L)).thenReturn(Optional.of(activeTest));
		when(testQuestionRepository.findByTestOrderByPositionAsc(activeTest)).thenReturn(List.of(question));

		// sets up answer as SKIPPED_BY_USER
		List<String> answers = List.of("SKIPPED_BY_USER");

		// submit the mock test
		testGeneratorService.submitTest(1L, answers);

		// check answer is recognised as skipped
		verify(question)
				.setAnswerStatus(uk.ac.bangor.cs.group2.academicymraeg.models.TestQuestions.AnswerStatus.SKIPPED);

		// check score doesn't increment
		assertEquals(0, activeTest.getResult());

		// check test status is set to submitted
		assertEquals(uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.SUBMITTED, activeTest.getStatus());

		// check the test is saved
		verify(testRepository).save(activeTest);
	}

	@Test
	void testSubmitTest_alreadySubmitted() {
		// sets up mock test for an already submitted test
		uk.ac.bangor.cs.group2.academicymraeg.models.Test submittedTest = new uk.ac.bangor.cs.group2.academicymraeg.models.Test(
				1, "alice", 0, java.time.LocalDateTime.now(),
				uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.SUBMITTED,
				java.time.LocalDateTime.now().plusMinutes(30));

		// mock repo returns already submitted test
		when(testRepository.findById(1L)).thenReturn(Optional.of(submittedTest));

		// check submitting again throws an exception
		assertThrows(IllegalStateException.class, () -> testGeneratorService.submitTest(1L, List.of("cath")));
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
	void testHasActiveTest_noActiveTest() {
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
	void testGetActiveTest() {
		// sets up test data for active test
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

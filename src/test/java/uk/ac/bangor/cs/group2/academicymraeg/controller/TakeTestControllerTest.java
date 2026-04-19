package uk.ac.bangor.cs.group2.academicymraeg.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyList;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import uk.ac.bangor.cs.group2.academicymraeg.models.TestQuestions;
import uk.ac.bangor.cs.group2.academicymraeg.service.TestGeneratorService;

@WebMvcTest(TakeTestController.class)
class TakeTestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private TestGeneratorService testGeneratorService;

//--------------------startTest-----------
	@Test
	@WithMockUser(username = "alice")
	void testStartTest() throws Exception {
		// sets up mock test for alice
		uk.ac.bangor.cs.group2.academicymraeg.models.Test activeTest = new uk.ac.bangor.cs.group2.academicymraeg.models.Test(
				1, "alice", 0, LocalDateTime.now(),
				uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.IN_PROGRESS,
				LocalDateTime.now().plusMinutes(30));

		// mock service to return generated test
		when(testGeneratorService.generateTestForUser("alice")).thenReturn(activeTest);

		// request start test page
		mockMvc.perform(get("/tests/take")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/tests/take/1"));

		// check service called with correct username
		verify(testGeneratorService).generateTestForUser("alice");
	}

//--------------------showTest-----------
	@Test
	@WithMockUser(username = "alice")
	void testShowTest() throws Exception {
		// sets up mock test for alice
		uk.ac.bangor.cs.group2.academicymraeg.models.Test activeTest = new uk.ac.bangor.cs.group2.academicymraeg.models.Test(
				1, "alice", 0, LocalDateTime.now(),
				uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.IN_PROGRESS,
				LocalDateTime.now().plusMinutes(30));

		TestQuestions question = mock(TestQuestions.class);

		when(question.getQuestionType())
				.thenReturn(uk.ac.bangor.cs.group2.academicymraeg.models.Question.QuestionType.WELSH);
		when(testGeneratorService.getTestById(1L)).thenReturn(activeTest);
		when(testGeneratorService.getQuestionsForTest(activeTest)).thenReturn(List.of(question));

		// request active test page
		mockMvc.perform(get("/tests/take/1")).andExpect(status().isOk()).andExpect(view().name("take-test"))
				.andExpect(model().attributeExists("test")).andExpect(model().attributeExists("questions"))
				.andExpect(model().attributeExists("testSubmissionForm"));
	}

	@Test
	@WithMockUser(username = "alice")
	void testShowTest_testAlreadySubmitted() throws Exception {
		// sets up mock test for alice which is submitted
		uk.ac.bangor.cs.group2.academicymraeg.models.Test submittedTest = new uk.ac.bangor.cs.group2.academicymraeg.models.Test(
				1, "alice", 0, LocalDateTime.now(),
				uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.SUBMITTED,
				LocalDateTime.now().plusMinutes(30));

		when(testGeneratorService.getTestById(1L)).thenReturn(submittedTest);

		// request submitted test page
		mockMvc.perform(get("/tests/take/1")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/review-test/1"));
	}

	@Test
	@WithMockUser(username = "alice")
	void testShowTest_wrongUser() throws Exception {
		// sets up test belonging to a different user (bob)
		uk.ac.bangor.cs.group2.academicymraeg.models.Test otherUsersTest = new uk.ac.bangor.cs.group2.academicymraeg.models.Test(
				1, "bob", 0, LocalDateTime.now(),
				uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.IN_PROGRESS,
				LocalDateTime.now().plusMinutes(30));

		when(testGeneratorService.getTestById(1L)).thenReturn(otherUsersTest);

		// request test page for a test not owned by alice
		Exception exception = assertThrows(Exception.class, () -> {
			mockMvc.perform(get("/tests/take/1")).andReturn();
		});

		//check exception is throw
		assertTrue(exception.getMessage().contains("You do not have access to this test"));
	}

//--------------------submitTest-----------	
	@Test
	@WithMockUser(username = "alice")
	void testSubmitTest() throws Exception {
		// submit test form with one answer
		mockMvc.perform(post("/tests/take/1").with(csrf()).param("answers[0]", "cath"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/review-test/1"));

		// check service called with correct test id
		verify(testGeneratorService).submitTest(eq(1L), anyList());
	}

}

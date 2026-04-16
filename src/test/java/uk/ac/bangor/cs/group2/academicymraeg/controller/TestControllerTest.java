package uk.ac.bangor.cs.group2.academicymraeg.controller;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import uk.ac.bangor.cs.group2.academicymraeg.service.TestGeneratorService;

@WebMvcTest(TestController.class)
class TestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private TestGeneratorService testGeneratorService;


	@Test
	@WithMockUser
	void testGenerateTest() throws Exception {
		// set up a mock test
		uk.ac.bangor.cs.group2.academicymraeg.models.Test activeTest = new uk.ac.bangor.cs.group2.academicymraeg.models.Test(
				1, "alice", 0, LocalDateTime.now(),
				uk.ac.bangor.cs.group2.academicymraeg.models.Test.TestStatus.IN_PROGRESS,
				LocalDateTime.now().plusMinutes(30));

		// mock service returns the mock test when requested
		when(testGeneratorService.generateTestForUser("alice")).thenReturn(activeTest);

		// POST request to generate test for alice
		mockMvc.perform(post("/tests/generate/alice").with(csrf())).andExpect(status().isOk());

		// verify testGeneratorService was called for correct user (alice)
		verify(testGeneratorService).generateTestForUser("alice");
	}

}

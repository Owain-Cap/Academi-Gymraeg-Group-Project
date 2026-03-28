package uk.ac.bangor.cs.group2.academicymraeg;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class NounViewTest {

	@Autowired
	//This acts like HHTP request, instead of starting the server it pretends it did
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	@WithMockUser(username = "SYSTEMTEST") 
	void testSaveValidNoun() throws Exception {
		// Submitting a valid noun should redirect to /nouns
		mockMvc.perform(post("/nouns/save").param("english", "school").param("welsh", "ysgol")
				.with(csrf())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/nouns"));
	}

	@Test
	@WithMockUser(username = "SYSTEMTEST")
	void testSaveInvalidNoun() throws Exception {
		// submitting an invalid noun with numbers should redisplay form with error
		mockMvc.perform(post("/nouns/save").param("english", "school123").param("welsh", "ysgol").with(csrf()))
				.andExpect(status().isOk()) // form displayed again
				.andExpect(model().attributeExists("error")) // shows error message
				.andExpect(view().name("editNoun")); // returns to the edit noun 
	}
}
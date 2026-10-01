package com.demo.backend;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HelloController.class)
class HelloControllerTests {

	@Autowired
	private MockMvc mvc;

	@Test
	void helloReturnsMessage() throws Exception {
		mvc.perform(get("/api/hello"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").exists())
			.andExpect(jsonPath("$.time").exists());
	}

	@Test
	void addAndListNotes() throws Exception {
		mvc.perform(post("/api/notes").contentType(MediaType.APPLICATION_JSON).content("{\"text\":\"hi\"}"))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.text").value("hi"));
		mvc.perform(get("/api/notes"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].text").value("hi"));
	}

	@Test
	void blankNoteIsRejected() throws Exception {
		mvc.perform(post("/api/notes").contentType(MediaType.APPLICATION_JSON).content("{\"text\":\" \"}"))
			.andExpect(status().isBadRequest());
	}

}

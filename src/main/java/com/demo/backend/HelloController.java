package com.demo.backend;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class HelloController {

	public record Note(long id, String text, Instant createdAt) {
	}

	public record NewNote(String text) {
	}

	private final BuildProperties build;

	private final AtomicLong nextId = new AtomicLong(1);

	private final List<Note> notes = new CopyOnWriteArrayList<>();

	public HelloController(ObjectProvider<BuildProperties> build) {
		this.build = build.getIfAvailable();
	}

	@GetMapping("/hello")
	public Map<String, String> hello() {
		return Map.of(
				"message", "Hello from the Spring Boot backend",
				"version", build != null ? build.getVersion() : "dev",
				"builtAt", build != null ? build.getTime().toString() : "n/a",
				"time", Instant.now().toString());
	}

	@GetMapping("/notes")
	public List<Note> listNotes() {
		return notes;
	}

	@PostMapping("/notes")
	@ResponseStatus(HttpStatus.CREATED)
	public Note addNote(@RequestBody NewNote body) {
		if (body == null || body.text() == null || body.text().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "text is required");
		}
		Note note = new Note(nextId.getAndIncrement(), body.text().strip(), Instant.now());
		notes.add(note);
		return note;
	}

}

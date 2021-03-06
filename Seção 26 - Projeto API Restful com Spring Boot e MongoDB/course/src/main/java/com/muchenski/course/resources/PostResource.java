package com.muchenski.course.resources;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.muchenski.course.domain.Post;
import com.muchenski.course.resources.utils.URL;
import com.muchenski.course.services.PostService;

@RestController
@RequestMapping(value = "/posts")
public class PostResource {

	@Autowired
	private PostService service;

	@GetMapping
	public ResponseEntity<List<Post>> findAll() {
		return ResponseEntity.ok().body(service.findAll());
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Post> findById(@PathVariable String id) {
		return ResponseEntity.ok().body(service.findById(id));
	}

	@GetMapping(value = "/titlesearch")
	public ResponseEntity<List<Post>> findByTitleContaining(
			@RequestParam(value = "keyword", defaultValue = "") String keyword) {

		keyword = URL.decode(keyword);

		return ResponseEntity.ok().body(service.findByTitleContaining(keyword));
	}

	@GetMapping(value = "/fullsearch")
	public ResponseEntity<List<Post>> fullSearch(@RequestParam(value = "keyword", defaultValue = "") String keyword,
			@RequestParam(value = "minDate", defaultValue = "") String minDate,
			@RequestParam(value = "maxDate", defaultValue = "") String maxDate) {

		keyword = URL.decode(keyword);
		Date min = URL.convertDate(minDate, new Date(0l));
		Date max = URL.convertDate(maxDate, new Date());

		return ResponseEntity.ok().body(service.fullSearch(keyword, min, max));
	}
}

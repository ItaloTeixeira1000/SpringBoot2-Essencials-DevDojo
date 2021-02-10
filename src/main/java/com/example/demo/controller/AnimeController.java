package com.example.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Anime;
import com.example.demo.requests.AnimePostRequestBody;
import com.example.demo.requests.AnimePutRequestBody;
import com.example.demo.service.AnimeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@RestController
@RequestMapping("animes")
@RequiredArgsConstructor
@Log4j2
public class AnimeController {
	
	private final AnimeService animeService;

	@GetMapping
	@Operation(summary = "List all animes paginated", description = "The default size is 20, use the parameter size to chenge the default value",
	tags = {"anime"})
	public ResponseEntity<Page<Anime>> list(@ParameterObject Pageable pageable) {

		return ResponseEntity.ok(animeService.listAll(pageable));
	}
	
	@GetMapping(path = "/all")
	public ResponseEntity<List<Anime>> listAll() {

		return ResponseEntity.ok(animeService.listAllNonPageable());
	}
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<Anime> findById(@PathVariable long id) {

		return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
	}
	
	@GetMapping(path = "by-id/{id}")
	public ResponseEntity<Anime> findByIdAuthenticationPrincipal(@PathVariable long id,
																 @AuthenticationPrincipal UserDetails userDetails) {
		
		log.info(userDetails);
		return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
	}
	
	@GetMapping(path = "/find")
	public ResponseEntity<List<Anime>> findByName(@RequestParam(required = false) String name) {

		return ResponseEntity.ok(animeService.findByName(name));
	}
	
	@PostMapping
	public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestBody animePostRequestBody) {
		return new ResponseEntity<>(animeService.save(animePostRequestBody), HttpStatus.CREATED);
		
	}
	
	@DeleteMapping(path = "/admin/{id}")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Successful operation"),
			@ApiResponse(responseCode = "400", description = "When anime does not exist in the database")
	})
	public ResponseEntity<Void> delete(@PathVariable long id) {
		
		animeService.delete(id);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
	}
	
	@PutMapping
	public ResponseEntity<Void> replace(@RequestBody AnimePutRequestBody animePutRequestBody) {
		
		animeService.replace(animePutRequestBody);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
	}
}

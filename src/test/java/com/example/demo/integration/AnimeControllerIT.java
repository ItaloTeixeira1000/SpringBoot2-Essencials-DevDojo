package com.example.demo.integration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.example.demo.domain.Anime;
import com.example.demo.domain.DevDojoUser;
import com.example.demo.repository.AnimeRepository;
import com.example.demo.repository.DevDojoUserRepository;
import com.example.demo.requests.AnimePostRequestBody;
import com.example.demo.util.AnimeCreator;
import com.example.demo.util.AnimePostRequestBodyCreator;
import com.example.demo.wrapper.PageableResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AnimeControllerIT {
	
	@Autowired
	@Qualifier(value = "testRestTemplateRoleUserRoleUser")
	private TestRestTemplate testRestTemplateRoleUser;
	
	@Autowired
	@Qualifier(value = "testRestTemplateRoleAdmin")
	private TestRestTemplate testRestTemplateRoleAdmin;
	
	@Autowired
	private DevDojoUserRepository devDojoUserRepository;
	
	@Autowired
	private AnimeRepository animeRepository;
	
	private static final DevDojoUser USER = DevDojoUser.builder()
			.name("DevDojo Academy")
			.password("{bcrypt}$2a$10$180BQuyf0atfZdDMMb5cyORw.k5gLj3Yws5NdpgR1Iv0qp5SP8nR.")
			.username("devdojo")
			.authorities("ROLE_USER")
			.build();
	
	private static final DevDojoUser ADMIN = DevDojoUser.builder()
			.name("William Suane")
			.password("{bcrypt}$2a$10$180BQuyf0atfZdDMMb5cyORw.k5gLj3Yws5NdpgR1Iv0qp5SP8nR.")
			.username("william")
			.authorities("ROLE_USER,ROLE_ADMIN")
			.build();
	
	@TestConfiguration
	@Lazy
	static class Config {
		
		@Bean(name= "testRestTemplateRoleUserRoleUser")
		public TestRestTemplate testRestTemplateRoleUserRoleUserCreator(@Value("${local.server.port}") int port) {
			RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
					.rootUri("http://localhost:" + port)
					.basicAuthentication("devdojo", "academy");
			return new TestRestTemplate(restTemplateBuilder);
		}
		
		@Bean(name= "testRestTemplateRoleAdmin")
		public TestRestTemplate testRestTemplateRoleUserRoleAdminCreator(@Value("${local.server.port}") int port) {
			RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
					.rootUri("http://localhost:" + port)
					.basicAuthentication("william", "academy");
			return new TestRestTemplate(restTemplateBuilder);
		}
	}
	
	@Test
	@DisplayName("list returns list of anime inside page object when successful")
	void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {
		Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
		
		devDojoUserRepository.save(USER);
		
		String expectedName = savedAnime.getName();
		
		PageableResponse<Anime> animePage = testRestTemplateRoleUser.exchange("/animes", HttpMethod.GET, null,
					new ParameterizedTypeReference<PageableResponse<Anime>>() {}).getBody();
		
		Assertions.assertThat(animePage.toList())
			.isNotEmpty()
			.hasSize(1);
		
		Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
	}
	
	@Test
	@DisplayName("listAll returns list of anime when successful")
	void listAll_ReturnsListOfAnimes_WhenSuccessful() {
		Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
		devDojoUserRepository.save(USER);
		
		String expectedName = savedAnime.getName();
		
		List<Anime> animes = testRestTemplateRoleUser.exchange("/animes/all", HttpMethod.GET, null,
					new ParameterizedTypeReference<List<Anime>>() {}).getBody();
		
		Assertions.assertThat(animes)
			.isNotNull()
			.isNotEmpty()
			.hasSize(1);
		
		Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
	}
	
	@Test
	@DisplayName("findById returns anime when successful")
	void findById_ReturnsAnime_WhenSuccessful() {
		Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
		devDojoUserRepository.save(USER);
		
		Long expectedId = savedAnime.getId();
		
		Anime anime = testRestTemplateRoleUser.getForObject("/animes/{id}", Anime.class, expectedId);
		
		Assertions.assertThat(anime)
			.isNotNull();
		
		Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
	}
	
	@Test
	@DisplayName("findByName returns list of anime when successful")
	void findByName_ReturnsListOfAnime_WhenSuccessful() {
		Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
		devDojoUserRepository.save(USER);
		
		String expectedName = savedAnime.getName();
		String url = String.format("/animes/find?name=%s", expectedName);
		
		List<Anime> animes = testRestTemplateRoleUser.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Anime>>() {}).getBody();
		
		Assertions.assertThat(animes)
			.isNotNull()
			.isNotEmpty()
			.hasSize(1);
		
		Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
	}
	
	@Test
	@DisplayName("findByName returns an empty list of anime when anime is not found")
	void findByName_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound() {
		devDojoUserRepository.save(USER);
		
		List<Anime> animes = testRestTemplateRoleUser.exchange("/animes/find?name=dbz", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Anime>>() {}).getBody();
		
		Assertions.assertThat(animes)
			.isNotNull()
			.isEmpty();
	}
	
	@Test
	@DisplayName("save returns anime when successful")
	void save_ReturnsAnime_WhenSuccessful() {
		devDojoUserRepository.save(USER);
		
		AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createAnimePostRequestBody();
		
		ResponseEntity<Anime> animeResponseEntity = testRestTemplateRoleUser.postForEntity("/animes", animePostRequestBody, Anime.class);
		
		Assertions.assertThat(animeResponseEntity).isNotNull();
		Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
		Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();
		
	}
	
	@Test
	@DisplayName("replace updates anime when successful")
	void replace_UpdatesAnime_WhenSuccessful() {
		
		Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
		savedAnime.setName("new name");
		
		devDojoUserRepository.save(USER);
		
		ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleUser.exchange(
				"/animes",HttpMethod.PUT, new HttpEntity<>(savedAnime), Void.class);
		
		Assertions.assertThat(animeResponseEntity).isNotNull();
		Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
	
	@Test
	@DisplayName("delete removes anime when successful")
	void delete_RemovesAnime_WhenSuccessful() {
		
		Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
		devDojoUserRepository.save(ADMIN);
		
		ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleAdmin.exchange(
				"/animes/admin/{id}",HttpMethod.DELETE, null, Void.class, savedAnime.getId());
		
		Assertions.assertThat(animeResponseEntity).isNotNull();
		Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		
	}
	
	@Test
	@DisplayName("delete returns 403 when user is not admin")
	void delete_Returns403_WhenUserIsNotAdmin() {
		
		Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
		devDojoUserRepository.save(USER);
		
		ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleUser.exchange(
				"/animes/admin/{id}",HttpMethod.DELETE, null, Void.class, savedAnime.getId());
		
		Assertions.assertThat(animeResponseEntity).isNotNull();
		Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		
	}

}

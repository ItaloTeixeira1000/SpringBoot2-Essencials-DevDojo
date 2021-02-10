package com.example.demo.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.demo.domain.Anime;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class SpringClient {
	public static void main(String[] args) {
		ResponseEntity<Anime> entity = new RestTemplate().getForEntity("http://localhost:8080/animes/{id}", Anime.class, 3);
		log.info(entity);
		
		Anime object = new RestTemplate().getForObject("http://localhost:8080/animes/{id}", Anime.class, 3);
		log.info(object);
		
		Anime[] animes = new RestTemplate().getForObject("http://localhost:8080/animes/all", Anime[].class);
		log.info(Arrays.toString(animes));
		
		ResponseEntity<List<Anime>> exchange = new RestTemplate()
				.exchange(
						"http://localhost:8080/animes/all",
						HttpMethod.GET, 
						null, 
						new ParameterizedTypeReference<List<Anime>>() {});
		log.info(exchange.getBody());
		
//		Anime kingdom = Anime.builder().name("kingdom").build();
//		Anime animeSaved = new RestTemplate().postForObject("http://localhost:8080/animes/", kingdom, Anime.class);
//		log.info(animeSaved);
		
		Anime samuraiChamplo = Anime.builder().name("Samurai champlo").build();
		ResponseEntity<Anime> samuraiChamploSaved = new RestTemplate()
				.exchange(
						"http://localhost:8080/animes/", 
						HttpMethod.POST, 
						new HttpEntity<>(samuraiChamplo, createJsonHeader()), 
						Anime.class);
		log.info(samuraiChamploSaved);
		
		Anime animeToBeUpdated =  samuraiChamploSaved.getBody();
		animeToBeUpdated.setName("Samurai champlo 2");
		ResponseEntity<Void> samuraiChamploUpdated = new RestTemplate()
				.exchange(
						"http://localhost:8080/animes/", 
						HttpMethod.PUT, 
						new HttpEntity<>(animeToBeUpdated, createJsonHeader()), 
						Void.class);
		log.info(samuraiChamploUpdated);
		
		ResponseEntity<Void> samuraiChamploDeleted = new RestTemplate()
				.exchange(
						"http://localhost:8080/animes/{id}", 
						HttpMethod.DELETE, 
						null, 
						Void.class,
						animeToBeUpdated.getId());
		log.info(samuraiChamploDeleted);
	}
	
	private static HttpHeaders createJsonHeader() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		return httpHeaders;
	}
}

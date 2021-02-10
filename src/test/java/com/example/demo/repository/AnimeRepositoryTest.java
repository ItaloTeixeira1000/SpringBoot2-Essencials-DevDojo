package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.domain.Anime;
import com.example.demo.util.AnimeCreator;

@DataJpaTest
@DisplayName("Tests for Anime repository")
class AnimeRepositoryTest {
	
	@Autowired
	private AnimeRepository animeRepository;
	
	@Test
	@DisplayName("Save persists anime when successful")
	void save_PersistAnime_WhenSuccessfull() {
		Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
		Anime animeSaved = this.animeRepository.save(animeToBeSaved);
		
		Assertions.assertThat(animeSaved).isNotNull();
		Assertions.assertThat(animeSaved.getId()).isNotNull();
		Assertions.assertThat(animeSaved.getName()).isEqualTo(animeToBeSaved.getName());
	}
	
	@Test
	@DisplayName("Save updates anime when successful")
	void save_UpdateAnime_WhenSuccessfull() {
		Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
		Anime animeSaved = this.animeRepository.save(animeToBeSaved);
		animeSaved.setName("Overload");
		
		Anime animeUpdated = this.animeRepository.save(animeSaved);
		
		Assertions.assertThat(animeUpdated).isNotNull();
		Assertions.assertThat(animeUpdated.getId()).isNotNull();
		Assertions.assertThat(animeUpdated.getName()).isEqualTo(animeSaved.getName());
	}
	
	@Test
	@DisplayName("Delete removes anime when successful")
	void delete_RemovesAnime_WhenSuccessfull() {
		Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
		Anime animeSaved = this.animeRepository.save(animeToBeSaved);
		
		this.animeRepository.delete(animeSaved);
		
		Optional<Anime> animeOptional = this.animeRepository.findById(animeSaved.getId());
		
		Assertions.assertThat(animeOptional).isEmpty();
	}
	
	@Test
	@DisplayName("Find by name returns list of anime when successful")
	void findByName_ReturnsListOfAnime_WhenSuccessfull() {
		Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
		Anime animeSaved = this.animeRepository.save(animeToBeSaved);
		String name = animeSaved.getName();
		
		List<Anime> animes = this.animeRepository.findByName(name);
		
		Assertions.assertThat(animes)
			.isNotEmpty()
			.contains(animeSaved);
	}
	
	@Test
	@DisplayName("Find by name returns empty list when no anime is found")
	void findByName_ReturnsEmptyList_WhenAnimeIsNotFound() {
		
		List<Anime> animes = this.animeRepository.findByName("afsfs");
		
		Assertions.assertThat(animes).isEmpty();
	}
	
	@Test
	@DisplayName("Save throw ConstraintViolationException when anime is empty")
	void save_ThrowsConstraintViolationException_WhenNameIsEmpty() {
		Anime anime = new Anime();
		
//		Assertions.assertThatThrownBy(() -> this.animeRepository.save(anime))
//			.isInstanceOf(ConstraintViolationException.class);
		
		Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
			.isThrownBy(() -> this.animeRepository.save(anime))
			.withMessageContaining("The anime name cannot be empty");
	}
	
	

}

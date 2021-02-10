package com.example.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Anime;
import com.example.demo.exception.BadRequestException;
import com.example.demo.mapper.AnimeMapper;
import com.example.demo.repository.AnimeRepository;
import com.example.demo.requests.AnimePostRequestBody;
import com.example.demo.requests.AnimePutRequestBody;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnimeService{
	
	private final AnimeRepository animeRepository;
	
	public Page<Anime> listAll(Pageable pageable) {
		return animeRepository.findAll(pageable);
	}
	
	public List<Anime> listAllNonPageable() {
		return animeRepository.findAll();
	}
	
	public List<Anime> findByName(String name) {
		return animeRepository.findByName(name);
	}
	
	public Anime findByIdOrThrowBadRequestException(long id) {
		return animeRepository.findById(id)
				.orElseThrow(() -> new BadRequestException("Anime not found"));
	}
	
	@Transactional
	public Anime save(AnimePostRequestBody animePostRequestBody){
		return animeRepository.save(AnimeMapper.INSTANCE.toAnime(animePostRequestBody));
	}

	public void delete(long id) {
		animeRepository.delete(findByIdOrThrowBadRequestException(id));
	}

	public void replace(AnimePutRequestBody animePutRequestBody) {
		Anime savedAnime = findByIdOrThrowBadRequestException(animePutRequestBody.getId());
		
		Anime anime = AnimeMapper.INSTANCE.toAnime(animePutRequestBody);
		anime.setId(savedAnime.getId());
		
		animeRepository.save(anime);
	}
}

package com.example.demo.util;

import com.example.demo.domain.Anime;

public class AnimeCreator {
	
	public static Anime createAnimeToBeSaved() {
		return Anime.builder()
				.name("Hajine no Ippo")
				.build();
	}
	
	public static Anime createValidAnime() {
		return Anime.builder()
				.name("Hajine no Ippo")
				.id(1L)
				.build();
	}
	
	public static Anime createValidUpdatedAnime() {
		return Anime.builder()
				.name("Hajine no Ippo 2")
				.id(1L)
				.build();
	}
}

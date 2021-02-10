package com.example.demo.util;

import com.example.demo.requests.AnimePostRequestBody;

public class AnimePostRequestBodyCreator {
	
	public static AnimePostRequestBody createAnimePostRequestBody() {
		return AnimePostRequestBody.builder()
				.name(AnimeCreator.createAnimeToBeSaved().getName())
				.build();
	}
}

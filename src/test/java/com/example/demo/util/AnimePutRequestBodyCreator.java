package com.example.demo.util;

import com.example.demo.requests.AnimePutRequestBody;

public class AnimePutRequestBodyCreator {
	
	public static AnimePutRequestBody createAnimePutRequestBody() {
		return AnimePutRequestBody.builder()
				.id(AnimeCreator.createValidUpdatedAnime().getId())
				.name(AnimeCreator.createValidUpdatedAnime().getName())
				.build();
	}
}

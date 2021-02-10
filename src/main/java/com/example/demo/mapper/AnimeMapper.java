package com.example.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.example.demo.domain.Anime;
import com.example.demo.requests.AnimePostRequestBody;
import com.example.demo.requests.AnimePutRequestBody;

@Mapper(componentModel = "spring")
public abstract class AnimeMapper {
	
	public static final AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);
	
	public abstract Anime toAnime(AnimePostRequestBody animePostRequestBody);
	
	public abstract Anime toAnime(AnimePutRequestBody animePutRequestBody);
}

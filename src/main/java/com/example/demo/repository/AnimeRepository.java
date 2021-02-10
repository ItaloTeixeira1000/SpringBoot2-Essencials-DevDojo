package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.Anime;

public interface AnimeRepository extends JpaRepository<Anime, Long> {
	List<Anime> findByName(String name);
}

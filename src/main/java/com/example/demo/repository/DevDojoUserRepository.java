package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.DevDojoUser;

public interface DevDojoUserRepository extends JpaRepository<DevDojoUser, Long> {
	
	DevDojoUser findByUsername(String username);
}

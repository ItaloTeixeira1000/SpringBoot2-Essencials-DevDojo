package com.example.demo.requests;

import javax.validation.constraints.NotEmpty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnimePostRequestBody {
	
	@NotEmpty(message = "The anime name cannot be empty")
	@Schema(description = "This is Anime's name", example = "Tensei Shittara $line Datta Ken", required = true)
	private String name;
	
}

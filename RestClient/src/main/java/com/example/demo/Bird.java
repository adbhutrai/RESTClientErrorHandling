package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bird {

	private Long id;

	private String scientificName;

	private String specie;

	private Double mass;

	private Integer length;
}

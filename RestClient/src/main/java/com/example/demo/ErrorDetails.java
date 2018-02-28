package com.example.demo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {
	private String status;
	private String timestamp;
	private String message;
	private String description;
	private List<ErrorMessage> errors;

}

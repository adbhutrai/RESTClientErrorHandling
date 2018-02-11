package com.example.demo;

import java.net.URI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
	private T response;

	private ErrorDetails error;

	private boolean success;
	
	private URI location;
}

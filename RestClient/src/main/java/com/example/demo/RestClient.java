package com.example.demo;

import java.io.IOException;
import java.net.URI;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j

public class RestClient {

	private RestTemplate restTemplate;

	private ObjectMapper objectMapper;

	public RestClient() {
		restTemplate = new RestTemplate();
		//restTemplate.setErrorHandler(new ErrorResponseHandler());
		objectMapper = new ObjectMapper();
	}

	public ApiResponse<Bird> getResource(String url) {
		ApiResponse<Bird> responseObj = new ApiResponse<>();
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
		String responseBody = response.getBody();
		try {
			if (isError(response.getStatusCode())) {
				ErrorDetails error = objectMapper.readValue(responseBody, ErrorDetails.class);
				responseObj.setError(error);
			} else {
				Bird bird = objectMapper.readValue(responseBody, Bird.class);
				responseObj.setSuccess(true);
				responseObj.setResponse(bird);
			}
		} catch (IOException e) {
			log.error("Error with response", e);
		}
		return responseObj;
	}

	public ApiResponse<URI> postResource(String url, Bird bird) {
		ApiResponse<URI> responseObj = new ApiResponse<>();
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<Bird> request = new HttpEntity<>(bird, headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
		String responseBody = response.getBody();
		try {
			if (isError(response.getStatusCode())) {
				ErrorDetails error = objectMapper.readValue(responseBody, ErrorDetails.class);
				responseObj.setError(error);
			} else {
				if (response.getStatusCode().equals(HttpStatus.CREATED)) {
					responseObj.setResponse(response.getHeaders().getLocation());
					responseObj.setSuccess(true);
				} else {
					responseObj.setSuccess(false);
				}
			}
		} catch (IOException e) {
			log.error("Error with response", e);
		}
		return responseObj;
	}
	public static boolean isError(HttpStatus status) {
        HttpStatus.Series series = status.series();
        return (HttpStatus.Series.CLIENT_ERROR.equals(series)
                || HttpStatus.Series.SERVER_ERROR.equals(series));
    }

	public ApiResponse<URI> updateResource(String url, Bird bird) {
		ApiResponse<URI> responseObj = new ApiResponse<>();
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<Bird> request = new HttpEntity<>(bird, headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
		String responseBody = response.getBody();
		try {
			if (isError(response.getStatusCode())) {
				ErrorDetails error = objectMapper.readValue(responseBody, ErrorDetails.class);
				responseObj.setError(error);
			} else {
				if (response.getStatusCode().equals(HttpStatus.CREATED)) {
					responseObj.setResponse(response.getHeaders().getLocation());
					responseObj.setSuccess(true);
				} else {
					responseObj.setSuccess(false);
				}
			}
		} catch (IOException e) {
			log.error("Error with response", e);
		}
		return responseObj;
	}



}

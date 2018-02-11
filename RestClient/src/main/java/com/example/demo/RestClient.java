package com.example.demo;

import java.io.IOException;

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
		restTemplate.setErrorHandler(new ErrorResponseHandler());
		objectMapper = new ObjectMapper();
	}

	public Response<Bird> getResource(String url) {
		Response<Bird> responseObj = new Response<>();
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
		String responseBody = response.getBody();
		try {
			if (RestUtil.isError(response.getStatusCode())) {
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

	public Response<Bird> postResource(String url, Bird bird) {
		Response<Bird> responseObj = new Response<>();
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<Bird> request = new HttpEntity<>(bird, headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
		String responseBody = response.getBody();
		try {
			if (RestUtil.isError(response.getStatusCode())) {
				ErrorDetails error = objectMapper.readValue(responseBody, ErrorDetails.class);
				responseObj.setError(error);
			} else {
				if (response.getStatusCode().equals(HttpStatus.CREATED)) {
					responseObj.setLocation(response.getHeaders().getLocation());
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

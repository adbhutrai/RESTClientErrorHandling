package com.example.demo;

import java.io.IOException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestClientMain {
	public static void main(String[] args) {
		//RestClientMain.getBirds("http://localhost:8080/birds/5");
		//System.out.println(RestClientMain.getBirds("http://localhost:8080/birds/1"));
		
		RestClient client = new RestClient();
		Response<Bird> response = client.getResource("http://localhost:8080/birds/5");
		System.out.println("Response successfull: "+response.isSuccess());
		System.out.println("Response : "+response.getError());
		System.out.println("Response : "+response.getResponse());
		
		Response<Bird> response2 = client.getResource("http://localhost:8080/birds/1");
		System.out.println("Response successfull: "+response2.isSuccess());
		System.out.println("Response : "+response2.getError());
		System.out.println("Response : "+response2.getResponse());
	}
	

	public static Bird getBirds(String url) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new ErrorResponseHandler());

		ObjectMapper objectMapper = new ObjectMapper();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
		String responseBody = response.getBody();
		try {
			if (RestUtil.isError(response.getStatusCode())) {
				ErrorDetails error = objectMapper.readValue(responseBody, ErrorDetails.class);
				throw new RestClientException("[" + error + "] " );
			} else {
				Bird bird = objectMapper.readValue(responseBody, Bird.class);
				return bird;
			}
		} catch (IOException e) {
			log.error("Error with response", e);
		}
		return null;
	}
}

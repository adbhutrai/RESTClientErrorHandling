package com.example.demo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "errordetails" })
public class Example {

	@JsonProperty("errordetails")
	private ErrorDetails errordetails;
	
	@JsonProperty("errordetails")
	public ErrorDetails getErrordetails() {
		return errordetails;
	}

	@JsonProperty("errordetails")
	public void setErrordetails(ErrorDetails errordetails) {
		this.errordetails = errordetails;
	}
}
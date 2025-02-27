package com.insuranceapp.exception;

import java.time.LocalDateTime;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.insuranceapp.model.ApiErrors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		LocalDateTime timestamp = LocalDateTime.now();
		HttpStatus statusMessage = HttpStatus.valueOf(status.value());
		ApiErrors errors = new ApiErrors(timestamp, status.value(), ex.getMessage(), statusMessage);
		headers.add("error", "method not allowed");
		return ResponseEntity.status(status).headers(headers).body(errors);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		LocalDateTime timestamp = LocalDateTime.now();
		HttpStatus statusMessage = HttpStatus.valueOf(status.value());
		ApiErrors errors = new ApiErrors(timestamp, status.value(), ex.getMessage(), statusMessage);
		headers.add("error", "mmedia not supported");
		return ResponseEntity.status(status).headers(headers).body(errors);
	}

	protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
			HttpHeaders headers1, HttpStatusCode status, WebRequest request) {
		LocalDateTime timestamp = LocalDateTime.now();
		HttpStatus statusMessage = HttpStatus.valueOf(status.value());
		ApiErrors errors = new ApiErrors(timestamp, status.value(), ex.getMessage(), statusMessage);
		headers1.add("error", "Missing path variable");
		return ResponseEntity.status(status).headers(headers1).body(errors);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		LocalDateTime timestamp = LocalDateTime.now();
		HttpStatus statusMessage = HttpStatus.valueOf(status.value());
		ApiErrors errors = new ApiErrors(timestamp, status.value(), ex.getMessage(), statusMessage);
		headers.add("error", "missing servlet");
		return ResponseEntity.status(status).headers(headers).body(errors);
	}

	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpHeaders headers1, HttpStatusCode status, WebRequest request) {
		LocalDateTime timestamp = LocalDateTime.now();
		HttpStatus statusMessage = HttpStatus.valueOf(status.value());
		ApiErrors errors = new ApiErrors(timestamp, status.value(), ex.getMessage(), statusMessage);
		headers1.add("error", "type mismatch");
		return ResponseEntity.status(status).headers(headers1).body(errors);
	}

	@ExceptionHandler(InsuranceNotFoundException.class)
	public ResponseEntity<Object> handleInsuranceNotFound(InsuranceNotFoundException ex) {
		LocalDateTime timestamp = LocalDateTime.now();
		HttpStatus statusMessage = HttpStatus.BAD_REQUEST;
		ApiErrors errors = new ApiErrors(timestamp, statusMessage.value(), ex.getMessage(), statusMessage);
		HttpHeaders headers = new HttpHeaders();
		headers.add("error", "Id not found");
		return ResponseEntity.status(statusMessage).headers(headers).body(errors);
	}

}

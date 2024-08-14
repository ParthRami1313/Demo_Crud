package com.crud.demo.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.crud.demo.controller.common.BaseController;
import com.crud.demo.dto.common.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler extends BaseController<Object> {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse<Object>> handleValidationErrors(MethodArgumentNotValidException exception,
			WebRequest webRequest) {
		List<String> error = exception.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
				.collect(Collectors.toList());
		return error(error, HttpStatus.BAD_REQUEST, webRequest.getDescription(false));
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse<Object>> runtimeException(RuntimeException exception, WebRequest webRequest) {
		return error(exception.getMessage(), HttpStatus.BAD_REQUEST, webRequest.getDescription(false));
	}

	@ExceptionHandler(InvalidDataException.class)
	public ResponseEntity<ErrorResponse<Object>> handleInvalidDataException(InvalidDataException exception,
			WebRequest webRequest) {
		return error(exception.getMessage(), HttpStatus.BAD_REQUEST, webRequest.getDescription(false));
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse<Object>> handleResourceNotFoundDataException(
			ResourceNotFoundException exception, WebRequest webRequest) {
		return error(exception.getMessage(), HttpStatus.NOT_FOUND, webRequest.getDescription(false));
	}

	@ExceptionHandler(ResourceAlreadyExistException.class)
	public ResponseEntity<ErrorResponse<Object>> handleResourceAlreadyExistException(
			ResourceAlreadyExistException exception, WebRequest webRequest) {
		return error(exception.getMessage(), HttpStatus.CONFLICT, webRequest.getDescription(false));
	}
}

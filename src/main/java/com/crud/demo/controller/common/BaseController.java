package com.crud.demo.controller.common;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.crud.demo.dto.common.ErrorResponse;
import com.crud.demo.dto.common.SuccessResponse;

public class BaseController<T> {
	
	protected ResponseEntity<SuccessResponse<T>> success(T data, String message) {
		SuccessResponse<T> successResponse = new SuccessResponse<T>();
		successResponse.setData(data);
		successResponse.setMessage(message);
		return new ResponseEntity<>(successResponse, HttpStatus.OK);
	}

	protected ResponseEntity<ErrorResponse<T>> error(T message, HttpStatus httpStatus, String path) {
		ErrorResponse<T> errorResponse = new ErrorResponse<T>();
		errorResponse.setTimeStamp(new Date());
		errorResponse.setMessage(message);
		errorResponse.setStatusCode(httpStatus.value());
		errorResponse.setPath(path);
		return new ResponseEntity<>(errorResponse, httpStatus);
	}
}

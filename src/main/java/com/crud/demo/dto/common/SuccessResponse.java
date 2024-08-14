package com.crud.demo.dto.common;

import lombok.Data;

@Data
public class SuccessResponse<T> {
	private String message;
	private T data;
}

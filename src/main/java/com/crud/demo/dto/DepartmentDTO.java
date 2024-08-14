package com.crud.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class DepartmentDTO {

	private Long id;

	@NotEmpty(message = "Department name should not be null or empty")
	private String name;

}

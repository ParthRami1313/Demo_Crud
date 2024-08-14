package com.crud.demo.dto;

import com.crud.demo.utils.StringLiterals;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeeRequestDTO {

	private Long id;

	@Email(regexp = StringLiterals.EMAIL_REGEX, message = "Invalid email")
	@NotEmpty(message = "Employee email should not be null or empty")
	private String email;

	@NotEmpty(message = "Employee name should not be null or empty")
	private String name;

	@NotNull(message = "Department id should not be null")
	private Long departmentId;

}

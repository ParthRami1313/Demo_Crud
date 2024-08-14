package com.crud.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crud.demo.controller.common.BaseController;
import com.crud.demo.dto.EmployeeRequestDTO;
import com.crud.demo.dto.common.SuccessResponse;
import com.crud.demo.service.EmployeeService;
import com.crud.demo.utils.StringLiterals;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/employee")
public class EmployeeController extends BaseController<Object> {

	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@PostMapping(value = "/create")
	public ResponseEntity<SuccessResponse<Object>> createEmployee(@Valid @RequestBody EmployeeRequestDTO dto) {
		return success(employeeService.createEmployee(dto), StringLiterals.OK);
	}

	@PutMapping(value = "/update")
	public ResponseEntity<SuccessResponse<Object>> updateEmployee(@Valid @RequestBody EmployeeRequestDTO dto) {
		return success(employeeService.updateEmployee(dto), StringLiterals.OK);
	}

	@GetMapping(value = "/get/{id}")
	public ResponseEntity<SuccessResponse<Object>> getEmployeeById(@PathVariable Long id) {
		return success(employeeService.getEmployeeById(id), StringLiterals.OK);
	}

	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<SuccessResponse<Object>> deleteEmployeeById(@PathVariable Long id) {
		return success(employeeService.deleteEmployeeById(id), StringLiterals.OK);
	}

	@GetMapping(value = "/getAll")
	public ResponseEntity<SuccessResponse<Object>> getAllDepartment(
			@RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
			@RequestParam(value = "isAllDataRequired", required = false, defaultValue = "true") boolean isAllDataRequired) {
		return success(employeeService.getAllEmployee(pageNumber, pageSize, isAllDataRequired), StringLiterals.OK);
	}

}

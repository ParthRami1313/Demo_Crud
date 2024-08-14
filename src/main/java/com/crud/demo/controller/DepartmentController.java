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
import com.crud.demo.dto.DepartmentDTO;
import com.crud.demo.dto.common.SuccessResponse;
import com.crud.demo.service.DepartmentService;
import com.crud.demo.utils.StringLiterals;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/department")
public class DepartmentController extends BaseController<Object> {

	private final DepartmentService departmentService;

	public DepartmentController(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@PostMapping(value = "/create")
	public ResponseEntity<SuccessResponse<Object>> createDepartment(@Valid @RequestBody DepartmentDTO dto) {
		return success(departmentService.createDepartment(dto), StringLiterals.OK);
	}

	@PutMapping(value = "/update")
	public ResponseEntity<SuccessResponse<Object>> updateDepartment(@Valid @RequestBody DepartmentDTO dto) {
		return success(departmentService.updateDepartment(dto), StringLiterals.OK);
	}

	@GetMapping(value = "/get/{id}")
	public ResponseEntity<SuccessResponse<Object>> getDepartmentById(@PathVariable Long id) {
		return success(departmentService.getDepartmentById(id), StringLiterals.OK);
	}

	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<SuccessResponse<Object>> deleteDepartmentById(@PathVariable Long id) {
		return success(departmentService.deleteDepartmentById(id), StringLiterals.OK);
	}

	@GetMapping(value = "/getAll")
	public ResponseEntity<SuccessResponse<Object>> getAllDepartment(
			@RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
			@RequestParam(value = "isAllDataRequired", required = false, defaultValue = "true") boolean isAllDataRequired) {
		return success(departmentService.getAllDepartment(pageNumber, pageSize, isAllDataRequired), StringLiterals.OK);
	}

}

package com.crud.demo.service;

import org.springframework.data.domain.Page;

import com.crud.demo.dto.DepartmentDTO;

public interface DepartmentService {

	DepartmentDTO createDepartment(DepartmentDTO requestDTO);

	DepartmentDTO updateDepartment(DepartmentDTO requestDTO);

	boolean deleteDepartmentById(Long id);

	DepartmentDTO getDepartmentById(Long id);

	Page<DepartmentDTO> getAllDepartment(int pageNumber, int pageSize, boolean isAllDataRequired);

}

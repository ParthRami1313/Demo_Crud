package com.crud.demo.service;

import org.springframework.data.domain.Page;

import com.crud.demo.dto.EmployeeDTO;
import com.crud.demo.dto.EmployeeRequestDTO;

public interface EmployeeService {

	EmployeeDTO createEmployee(EmployeeRequestDTO requestDTO);

	EmployeeDTO updateEmployee(EmployeeRequestDTO requestDTO);

	boolean deleteEmployeeById(Long id);

	EmployeeDTO getEmployeeById(Long id);

	Page<EmployeeDTO> getAllEmployee(int pageNumber, int pageSize, boolean isAllDataRequired);

}

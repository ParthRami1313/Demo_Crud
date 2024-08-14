package com.crud.demo.service.impl;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.crud.demo.dto.EmployeeDTO;
import com.crud.demo.dto.EmployeeRequestDTO;
import com.crud.demo.entity.Department;
import com.crud.demo.entity.Employee;
import com.crud.demo.exception.InvalidDataException;
import com.crud.demo.exception.ResourceAlreadyExistException;
import com.crud.demo.exception.ResourceNotFoundException;
import com.crud.demo.mapper.EmployeeMapper;
import com.crud.demo.repository.DepartmentRepository;
import com.crud.demo.repository.EmployeeRepository;
import com.crud.demo.service.EmployeeService;
import com.crud.demo.utils.DateUtility;
import com.crud.demo.utils.PageUtility;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository employeeRepository;
	private final DepartmentRepository departmentRepository;
	private final EmployeeMapper employeeMapper;

	public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper mapper,
			DepartmentRepository departmentRepository) {
		this.employeeRepository = employeeRepository;
		this.employeeMapper = mapper;
		this.departmentRepository = departmentRepository;
	}

	@Override
	public EmployeeDTO createEmployee(EmployeeRequestDTO requestDTO) {
		Optional<Department> departmentEntity = departmentRepository.findById(requestDTO.getDepartmentId());
		if (!departmentEntity.isPresent()) {
			throw new ResourceNotFoundException("Department not found with provided id.");
		}

		Optional<Employee> employeeEntity = employeeRepository.findByEmail(requestDTO.getEmail());
		if (employeeEntity.isPresent()) {
			throw new ResourceAlreadyExistException("Employee already exists with provided email.");
		}

		ZonedDateTime currentUTCDate = DateUtility.getCurrentUTCDate();

		Employee entity = new Employee();
		entity.setEmail(requestDTO.getEmail());
		entity.setName(requestDTO.getName());
		entity.setDepartment(departmentEntity.get());
		entity.setCreatedDate(currentUTCDate);
		entity.setModifiedDate(currentUTCDate);

		return employeeMapper.mapToDTO(employeeRepository.save(entity));
	}

	@Override
	public EmployeeDTO updateEmployee(EmployeeRequestDTO requestDTO) {
		if (Objects.isNull(requestDTO.getId())) {
			throw new InvalidDataException("Employee id is missing.");
		}

		Optional<Employee> employeeEntity = employeeRepository.findById(requestDTO.getId());
		if (!employeeEntity.isPresent()) {
			throw new ResourceNotFoundException("Employee not found with provided id.");
		}

		Optional<Department> departmentEntity = departmentRepository.findById(requestDTO.getDepartmentId());
		if (!departmentEntity.isPresent()) {
			throw new ResourceNotFoundException("Department not found with provided id.");
		}

		Employee employee = employeeEntity.get();
		employee.setName(requestDTO.getName());
		employee.setDepartment(departmentEntity.get());

		return employeeMapper.mapToDTO(employeeRepository.save(employee));
	}

	@Override
	public boolean deleteEmployeeById(Long id) {
		Optional<Employee> employeeEntity = employeeRepository.findById(id);
		if (!employeeEntity.isPresent()) {
			throw new ResourceNotFoundException("Employee not found with provided id.");
		}
		employeeRepository.delete(employeeEntity.get());
		return true;
	}

	@Override
	public EmployeeDTO getEmployeeById(Long id) {
		Optional<Employee> employeeEntity = employeeRepository.findById(id);
		if (!employeeEntity.isPresent()) {
			throw new ResourceNotFoundException("Employee not found with provided id.");
		}
		return employeeMapper.mapToDTO(employeeEntity.get());
	}

	@Override
	public Page<EmployeeDTO> getAllEmployee(int pageNumber, int pageSize, boolean isAllDataRequired) {
		PageRequest pagination = PageUtility.getPagination(pageNumber, pageSize, isAllDataRequired);
		Page<Employee> pageData = employeeRepository.findAll(pagination);
		return convertToDTO(pageData);
	}

	private Page<EmployeeDTO> convertToDTO(Page<Employee> page) {
		List<EmployeeDTO> dtoList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(page.getContent())) {
			dtoList = employeeMapper.mapToListDTO(page.getContent());
		}
		return new PageImpl<>(dtoList, page.getPageable(), page.getTotalElements());
	}

}

package com.crud.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.crud.demo.dto.DepartmentDTO;
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
import com.crud.demo.service.impl.EmployeeServiceImpl;

@SpringBootTest
public class EmployeeServiceTest {

	@InjectMocks
	private EmployeeServiceImpl employeeService;

	@Mock
	private DepartmentRepository departmentRepository;

	@Mock
	private EmployeeRepository employeeRepository;

	@Mock
	private EmployeeMapper employeeMapper;

	private EmployeeRequestDTO getEmployeeDTOWithOutId() {
		EmployeeRequestDTO dto = new EmployeeRequestDTO();
		dto.setName("Test");
		dto.setEmail("test@gmail.com");
		dto.setDepartmentId(1l);
		return dto;
	}

	private EmployeeRequestDTO getEmployeeDTOWithId() {
		EmployeeRequestDTO dto = new EmployeeRequestDTO();
		dto.setId(1l);
		dto.setName("Test");
		dto.setEmail("test@gmail.com");
		dto.setDepartmentId(1l);
		return dto;
	}

	private EmployeeDTO getEmployeeResponseDTO() {
		EmployeeDTO dto = new EmployeeDTO();
		dto.setId(1l);
		dto.setName("Test");
		dto.setEmail("test@gmail.com");
		DepartmentDTO department = new DepartmentDTO();
		department.setId(1l);
		department.setName("IT");
		dto.setDepartment(department);
		return dto;
	}

	private Employee getEmployeeEntity() {
		Employee entity = new Employee();
		entity.setId(1l);
		entity.setName("Test");
		entity.setEmail("test@gmail.com");
		Department department = new Department();
		department.setId(1l);
		department.setName("IT");
		entity.setDepartment(department);
		return entity;
	}

	private Department getDepartmentEntity() {
		Department entity = new Department();
		entity.setId(1l);
		entity.setName("IT");
		return entity;
	}

	@DisplayName("Create employee with success")
	@Test
	void createEmployeeSuccessTest() {
		EmployeeRequestDTO requestDTO = getEmployeeDTOWithOutId();

		Department departmentEntity = getDepartmentEntity();

		when(departmentRepository.findById(any())).thenReturn(Optional.of(departmentEntity));
		when(employeeRepository.findByEmail(any())).thenReturn(Optional.empty());
		when(employeeRepository.save(any())).thenReturn(getEmployeeEntity());
		when(employeeMapper.mapToDTO(any())).thenReturn(getEmployeeResponseDTO());

		EmployeeDTO result = employeeService.createEmployee(requestDTO);

		assertNotNull(result);
		assertEquals(requestDTO.getName(), result.getName());
		assertEquals(requestDTO.getEmail(), result.getEmail());
	}

	@DisplayName("Create employee with department not found exception")
	@Test
	void createEmployeeWithDepartmentNotFoundExceptionTest() {
		EmployeeRequestDTO requestDTO = getEmployeeDTOWithOutId();

		when(departmentRepository.findById(any())).thenReturn(Optional.empty());
		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
			employeeService.createEmployee(requestDTO);
		});

		assertEquals("Department not found with provided id.", exception.getMessage());
	}

	@DisplayName("Create employee with email exists exception")
	@Test
	void createEmployeeWithEmailExistsExceptionTest() {
		EmployeeRequestDTO requestDTO = getEmployeeDTOWithOutId();

		Department departmentEntity = getDepartmentEntity();

		when(departmentRepository.findById(any())).thenReturn(Optional.of(departmentEntity));
		when(employeeRepository.findByEmail(any())).thenReturn(Optional.of(getEmployeeEntity()));
		ResourceAlreadyExistException exception = assertThrows(ResourceAlreadyExistException.class, () -> {
			employeeService.createEmployee(requestDTO);
		});

		assertEquals("Employee already exists with provided email.", exception.getMessage());
	}

	@DisplayName("Update employee with success")
	@Test
	void updateEmployeeSuccessTest() {
		EmployeeRequestDTO requestDTO = getEmployeeDTOWithId();

		Department departmentEntity = getDepartmentEntity();

		when(employeeRepository.findById(any())).thenReturn(Optional.of(getEmployeeEntity()));
		when(departmentRepository.findById(any())).thenReturn(Optional.of(departmentEntity));
		when(employeeRepository.save(any())).thenReturn(getEmployeeEntity());
		when(employeeMapper.mapToDTO(any())).thenReturn(getEmployeeResponseDTO());

		EmployeeDTO result = employeeService.updateEmployee(requestDTO);

		assertNotNull(result);
		assertEquals(requestDTO.getName(), result.getName());
		assertEquals(requestDTO.getEmail(), result.getEmail());
	}

	@DisplayName("Update employee with missing id exception")
	@Test
	void updateEmployeeWithMissingIdTest() {
		EmployeeRequestDTO requestDTO = getEmployeeDTOWithOutId();

		InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
			employeeService.updateEmployee(requestDTO);
		});

		assertEquals("Employee id is missing.", exception.getMessage());
	}

	@DisplayName("Update employee with no employee found exception")
	@Test
	void updateEmployeeWithNoEmployeeFoundExceptionTest() {
		EmployeeRequestDTO requestDTO = getEmployeeDTOWithId();

		when(employeeRepository.findById(any())).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
			employeeService.updateEmployee(requestDTO);
		});

		assertEquals("Employee not found with provided id.", exception.getMessage());
	}

	@DisplayName("Update employee with no department found exception")
	@Test
	void updateEmployeeWithNoDepartmentFoundExceptionTest() {
		EmployeeRequestDTO requestDTO = getEmployeeDTOWithId();

		when(employeeRepository.findById(any())).thenReturn(Optional.of(getEmployeeEntity()));
		when(departmentRepository.findById(any())).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
			employeeService.updateEmployee(requestDTO);
		});

		assertEquals("Department not found with provided id.", exception.getMessage());
	}

	@DisplayName("Delete employee with success")
	@Test
	void deleteEmployeeSuccessTest() {
		when(employeeRepository.findById(any())).thenReturn(Optional.of(getEmployeeEntity()));

		boolean result = employeeService.deleteEmployeeById(1l);

		assertTrue(result);
	}

	@DisplayName("Delete employee with no employee found exception")
	@Test
	void deleteEmployeeWithNoEmployeeFoundExceptionTest() {
		when(employeeRepository.findById(any())).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
			employeeService.deleteEmployeeById(1l);
		});

		assertEquals("Employee not found with provided id.", exception.getMessage());
	}

	@DisplayName("Get employee by id with success")
	@Test
	void getEmployeeByIdSuccessTest() {
		when(employeeRepository.findById(any())).thenReturn(Optional.of(getEmployeeEntity()));
		when(employeeMapper.mapToDTO(any())).thenReturn(getEmployeeResponseDTO());
		EmployeeDTO result = employeeService.getEmployeeById(1l);
		assertNotNull(result);
		assertEquals(1l, result.getId());
	}

	@DisplayName("Get employee by id with no employee found exception")
	@Test
	void getEmployeeByIdWithNoEmployeeFoundExceptionTest() {
		when(employeeRepository.findById(any())).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
			employeeService.getEmployeeById(1l);
		});

		assertEquals("Employee not found with provided id.", exception.getMessage());
	}

	@DisplayName("Get all employee with success")
	@Test
	void getAllEmployeeSuccessTest() {

		List<Employee> list = new ArrayList<>();
		list.add(getEmployeeEntity());

		PageRequest pagination = PageRequest.of(0, 5);

		Page<Employee> pageData = new PageImpl<>(list);

		when(employeeRepository.findAll(pagination)).thenReturn(pageData);

		employeeService.getAllEmployee(0, 5, false);
	}

	@DisplayName("Get all employee with success 1")
	@Test
	void getAllEmployeeSuccessTest1() {
		PageRequest pagination = PageRequest.of(0, 5);

		Page<Employee> pageData = new PageImpl<>(new ArrayList<>());

		when(employeeRepository.findAll(pagination)).thenReturn(pageData);

		employeeService.getAllEmployee(0, 5, false);
	}

}

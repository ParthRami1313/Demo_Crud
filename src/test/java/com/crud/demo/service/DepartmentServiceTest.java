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
import com.crud.demo.entity.Department;
import com.crud.demo.exception.InvalidDataException;
import com.crud.demo.exception.ResourceAlreadyExistException;
import com.crud.demo.exception.ResourceNotFoundException;
import com.crud.demo.mapper.DepartmentMapper;
import com.crud.demo.repository.DepartmentRepository;
import com.crud.demo.service.impl.DepartmentServiceImpl;

@SpringBootTest
public class DepartmentServiceTest {

	@InjectMocks
	private DepartmentServiceImpl departmentService;

	@Mock
	private DepartmentRepository departmentRepository;

	@Mock
	private DepartmentMapper departmentMapper;

	private Department getDepartmentEntity() {
		Department entity = new Department();
		entity.setId(1l);
		entity.setName("IT");
		return entity;
	}

	private DepartmentDTO getDepartmentDTOWithOutId() {
		DepartmentDTO requestDTO = new DepartmentDTO();
		requestDTO.setName("IT");
		return requestDTO;
	}

	private DepartmentDTO getDepartmentDTOWithId() {
		DepartmentDTO requestDTO = new DepartmentDTO();
		requestDTO.setId(1l);
		requestDTO.setName("IT");
		return requestDTO;
	}

	@DisplayName("Create department with success")
	@Test
	void createDepartmentSuccessTest() {
		DepartmentDTO requestDTO = getDepartmentDTOWithOutId();
		Department entity = getDepartmentEntity();

		when(departmentRepository.findByName(requestDTO.getName())).thenReturn(Optional.empty());
		when(departmentRepository.save(any())).thenReturn(entity);
		when(departmentMapper.mapToDTO(any())).thenReturn(getDepartmentDTOWithId());

		DepartmentDTO result = departmentService.createDepartment(requestDTO);

		assertNotNull(result);
		assertEquals(requestDTO.getName(), result.getName());
	}

	@DisplayName("Create department with already exists exception")
	@Test
	void createDepartmentWithAlreadyExistsException() {
		DepartmentDTO requestDTO = getDepartmentDTOWithOutId();
		Department entity = getDepartmentEntity();

		when(departmentRepository.findByName(requestDTO.getName())).thenReturn(Optional.of(entity));

		ResourceAlreadyExistException exception = assertThrows(ResourceAlreadyExistException.class, () -> {
			departmentService.createDepartment(requestDTO);
		});

		assertEquals("Department already exists with same name.", exception.getMessage());
	}

	@DisplayName("Update department with success")
	@Test
	void updateDepartmentSuccessTest() {
		DepartmentDTO requestDTO = getDepartmentDTOWithId();
		Department entity = getDepartmentEntity();

		when(departmentRepository.findById(any())).thenReturn(Optional.of(entity));
		when(departmentRepository.findByName(requestDTO.getName())).thenReturn(Optional.empty());
		when(departmentRepository.save(any())).thenReturn(entity);
		when(departmentMapper.mapToDTO(any())).thenReturn(getDepartmentDTOWithId());

		DepartmentDTO result = departmentService.updateDepartment(requestDTO);

		assertNotNull(result);
		assertEquals(requestDTO.getName(), result.getName());
	}

	@DisplayName("Update department with success 1")
	@Test
	void updateDepartmentSuccessTest1() {
		DepartmentDTO requestDTO = getDepartmentDTOWithId();
		Department entity = getDepartmentEntity();
		entity.setName("IT1");

		when(departmentRepository.findById(any())).thenReturn(Optional.of(entity));
		when(departmentRepository.findByName(requestDTO.getName())).thenReturn(Optional.empty());
		when(departmentRepository.save(any())).thenReturn(entity);
		when(departmentMapper.mapToDTO(any())).thenReturn(getDepartmentDTOWithId());

		DepartmentDTO result = departmentService.updateDepartment(requestDTO);

		assertNotNull(result);
	}

	@DisplayName("Update department with already exists exception")
	@Test
	void updateDepartmentWithAlreadyExistsExceptionTest() {
		DepartmentDTO requestDTO = getDepartmentDTOWithId();
		Department entity = getDepartmentEntity();
		entity.setName("IT1");

		when(departmentRepository.findById(any())).thenReturn(Optional.of(entity));
		when(departmentRepository.findByName(requestDTO.getName())).thenReturn(Optional.of(entity));

		ResourceAlreadyExistException exception = assertThrows(ResourceAlreadyExistException.class, () -> {
			departmentService.updateDepartment(requestDTO);
		});

		assertEquals("Department already exists with same name.", exception.getMessage());
	}

	@DisplayName("Update department with missing id exception")
	@Test
	void updateDepartmentWithMissingIdExceptionTest() {
		DepartmentDTO requestDTO = getDepartmentDTOWithOutId();

		InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
			departmentService.updateDepartment(requestDTO);
		});

		assertEquals("Department id is missing.", exception.getMessage());
	}

	@DisplayName("Update department with no department found exception")
	@Test
	void updateDepartmentWithNoDepartmentFoundExceptionTest() {
		DepartmentDTO requestDTO = getDepartmentDTOWithId();

		when(departmentRepository.findById(any())).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
			departmentService.updateDepartment(requestDTO);
		});

		assertEquals("Department not found with provided id.", exception.getMessage());
	}

	@DisplayName("Delete department with success")
	@Test
	void deleteDepartmentSuccessTest() {
		Department entity = getDepartmentEntity();

		when(departmentRepository.findById(any())).thenReturn(Optional.of(entity));

		boolean result = departmentService.deleteDepartmentById(1l);

		assertTrue(result);
	}

	@DisplayName("Delete department with no department found exception")
	@Test
	void deleteDepartmentWithNoDepartmentFoundExceptionTest() {
		when(departmentRepository.findById(any())).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
			departmentService.deleteDepartmentById(1l);
		});

		assertEquals("Department not found with provided id.", exception.getMessage());
	}

	@DisplayName("Get department with success")
	@Test
	void getDepartmentByIdSuccessTest() {
		Department entity = getDepartmentEntity();

		when(departmentRepository.findById(any())).thenReturn(Optional.of(entity));
		when(departmentMapper.mapToDTO(any())).thenReturn(getDepartmentDTOWithId());

		DepartmentDTO result = departmentService.getDepartmentById(1l);

		assertNotNull(result);
		assertEquals(1l, result.getId());
	}

	@DisplayName("Get department with no department found exception")
	@Test
	void getDepartmentByIdWithNoDepartmentFoundExceptionTest() {
		when(departmentRepository.findById(any())).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
			departmentService.getDepartmentById(1l);
		});

		assertEquals("Department not found with provided id.", exception.getMessage());
	}

	@DisplayName("Get all department with success")
	@Test
	void getAllDepartmentSuccessTest() {
		Department entity = getDepartmentEntity();

		List<Department> list = new ArrayList<>();
		list.add(entity);

		PageRequest pagination = PageRequest.of(0, 5);

		Page<Department> pageData = new PageImpl<>(list);

		when(departmentRepository.findAll(pagination)).thenReturn(pageData);

		departmentService.getAllDepartment(0, 5, false);
	}

	@DisplayName("Get all department with success 1")
	@Test
	void getAllDepartmentSuccessTest1() {
		PageRequest pagination = PageRequest.of(0, 5);

		Page<Department> pageData = new PageImpl<>(new ArrayList<>());

		when(departmentRepository.findAll(pagination)).thenReturn(pageData);

		departmentService.getAllDepartment(0, 5, false);
	}

}

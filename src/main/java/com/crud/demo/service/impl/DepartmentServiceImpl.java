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

import com.crud.demo.dto.DepartmentDTO;
import com.crud.demo.entity.Department;
import com.crud.demo.exception.InvalidDataException;
import com.crud.demo.exception.ResourceAlreadyExistException;
import com.crud.demo.exception.ResourceNotFoundException;
import com.crud.demo.mapper.DepartmentMapper;
import com.crud.demo.repository.DepartmentRepository;
import com.crud.demo.service.DepartmentService;
import com.crud.demo.utils.DateUtility;
import com.crud.demo.utils.PageUtility;

@Service
public class DepartmentServiceImpl implements DepartmentService {

	private final DepartmentRepository departmentRepository;
	private final DepartmentMapper departmentMapper;

	public DepartmentServiceImpl(DepartmentRepository departmentRepository, DepartmentMapper mapper) {
		this.departmentRepository = departmentRepository;
		this.departmentMapper = mapper;
	}

	@Override
	public DepartmentDTO createDepartment(DepartmentDTO requestDTO) {
		Optional<Department> dataEntity = departmentRepository.findByName(requestDTO.getName());
		if (dataEntity.isPresent()) {
			throw new ResourceAlreadyExistException("Department already exists with same name.");
		}

		ZonedDateTime currentUTCDate = DateUtility.getCurrentUTCDate();

		Department entity = new Department();
		entity.setName(requestDTO.getName());
		entity.setCreatedDate(currentUTCDate);
		entity.setModifiedDate(currentUTCDate);

		return departmentMapper.mapToDTO(departmentRepository.save(entity));
	}

	@Override
	public DepartmentDTO updateDepartment(DepartmentDTO requestDTO) {
		if (Objects.isNull(requestDTO.getId())) {
			throw new InvalidDataException("Department id is missing.");
		}
		Optional<Department> dataEntity = departmentRepository.findById(requestDTO.getId());
		if (!dataEntity.isPresent()) {
			throw new ResourceNotFoundException("Department not found with provided id.");
		}

		Department department = dataEntity.get();

		if (!department.getName().equals(requestDTO.getName())) {
			Optional<Department> dataEntityWithName = departmentRepository.findByName(requestDTO.getName());
			if (dataEntityWithName.isPresent()) {
				throw new ResourceAlreadyExistException("Department already exists with same name.");
			}
			department.setName(requestDTO.getName());
		}

		department.setModifiedDate(DateUtility.getCurrentUTCDate());

		return departmentMapper.mapToDTO(departmentRepository.save(department));
	}

	@Override
	public boolean deleteDepartmentById(Long id) {
		Optional<Department> dataEntity = departmentRepository.findById(id);
		if (!dataEntity.isPresent()) {
			throw new ResourceNotFoundException("Department not found with provided id.");
		}
		departmentRepository.delete(dataEntity.get());
		return true;
	}

	@Override
	public DepartmentDTO getDepartmentById(Long id) {
		Optional<Department> dataEntity = departmentRepository.findById(id);
		if (!dataEntity.isPresent()) {
			throw new ResourceNotFoundException("Department not found with provided id.");
		}
		return departmentMapper.mapToDTO(dataEntity.get());
	}

	@Override
	public Page<DepartmentDTO> getAllDepartment(int pageNumber, int pageSize, boolean isAllDataRequired) {
		PageRequest pagination = PageUtility.getPagination(pageNumber, pageSize, isAllDataRequired);
		Page<Department> pageData = departmentRepository.findAll(pagination);
		return convertToDTO(pageData);
	}

	private Page<DepartmentDTO> convertToDTO(Page<Department> page) {
		List<DepartmentDTO> dtoList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(page.getContent())) {
			dtoList = departmentMapper.mapToListDTO(page.getContent());
		}
		return new PageImpl<>(dtoList, page.getPageable(), page.getTotalElements());
	}

}

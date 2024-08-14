package com.crud.demo.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.crud.demo.dto.DepartmentDTO;
import com.crud.demo.entity.Department;

@Mapper
public interface DepartmentMapper {
	
	DepartmentDTO mapToDTO(Department entity);

	List<DepartmentDTO> mapToListDTO(List<Department> entity);

}

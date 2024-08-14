package com.crud.demo.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.crud.demo.dto.EmployeeDTO;
import com.crud.demo.entity.Employee;

@Mapper
public interface EmployeeMapper {

	EmployeeDTO mapToDTO(Employee entity);

	List<EmployeeDTO> mapToListDTO(List<Employee> entity);

}

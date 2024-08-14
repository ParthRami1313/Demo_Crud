package com.crud.demo.dto;

import lombok.Data;

@Data
public class EmployeeDTO {

    private Long id;
    
    private String email;
    
    private String name;
    
    private DepartmentDTO department;


}

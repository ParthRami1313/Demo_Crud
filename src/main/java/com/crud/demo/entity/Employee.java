package com.crud.demo.entity;


import com.crud.demo.entity.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "employee")
@Data
@EqualsAndHashCode(callSuper = false)
public class Employee extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, name = "email", unique = true)
    private String email;
    
    @Column(nullable = false, name = "name", unique = true)
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
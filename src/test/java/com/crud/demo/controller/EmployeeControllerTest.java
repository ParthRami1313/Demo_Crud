package com.crud.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.crud.demo.dto.DepartmentDTO;
import com.crud.demo.dto.EmployeeDTO;
import com.crud.demo.dto.EmployeeRequestDTO;
import com.crud.demo.dto.common.SuccessResponse;
import com.crud.demo.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EmployeeController.class)
@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EmployeeService employeeService;

	@DisplayName("Create employee")
	@Test
	public void createEmployeeTest() throws Exception {

		EmployeeDTO response = getResponse();

		when(employeeService.createEmployee(any(EmployeeRequestDTO.class))).thenReturn(response);

		ObjectMapper mapper = new ObjectMapper();
		String requestJson = mapper.writeValueAsString(getRequest());

		MvcResult result = this.mockMvc
				.perform(post("/v1/employee/create").contentType(MediaType.APPLICATION_JSON).content(requestJson))
				.andExpect(status().isOk()).andReturn();

		String jsonResponse = result.getResponse().getContentAsString();
		SuccessResponse<EmployeeDTO> successResponse = mapper.readValue(jsonResponse,
				mapper.getTypeFactory().constructParametricType(SuccessResponse.class, EmployeeDTO.class));

		assertEquals("OK", successResponse.getMessage());
		assertEquals(response.getId(), successResponse.getData().getId());
		assertEquals(response.getEmail(), successResponse.getData().getEmail());
	}

	@DisplayName("Update employee")
	@Test
	public void updateEmployeeTest() throws Exception {
		
		EmployeeDTO response = getResponse();

		when(employeeService.updateEmployee(any(EmployeeRequestDTO.class))).thenReturn(response);

		ObjectMapper mapper = new ObjectMapper();
		String requestJson = mapper.writeValueAsString(getRequest());

		MvcResult result = this.mockMvc
				.perform(put("/v1/employee/update").contentType(MediaType.APPLICATION_JSON).content(requestJson))
				.andExpect(status().isOk()).andReturn();

		String jsonResponse = result.getResponse().getContentAsString();
		SuccessResponse<EmployeeDTO> successResponse = mapper.readValue(jsonResponse,
				mapper.getTypeFactory().constructParametricType(SuccessResponse.class, EmployeeDTO.class));

		assertEquals("OK", successResponse.getMessage());
		assertEquals(response.getId(), successResponse.getData().getId());
		assertEquals(response.getEmail(), successResponse.getData().getEmail());
	}

	@DisplayName("Get employee by id")
	@Test
	public void getEmployeeByIdTest() throws Exception {
		
		EmployeeDTO response = getResponse();

		when(employeeService.getEmployeeById(any())).thenReturn(response);

		MvcResult result = this.mockMvc.perform(get("/v1/employee/get/1")).andExpect(status().isOk()).andReturn();

		ObjectMapper mapper = new ObjectMapper();
		String jsonResponse = result.getResponse().getContentAsString();
		SuccessResponse<EmployeeDTO> successResponse = mapper.readValue(jsonResponse,
				mapper.getTypeFactory().constructParametricType(SuccessResponse.class, EmployeeDTO.class));

		assertEquals("OK", successResponse.getMessage());
		assertEquals(response.getId(), successResponse.getData().getId());
		assertEquals(response.getEmail(), successResponse.getData().getEmail());
	}

	@DisplayName("Delete employee by id")
	@Test
	public void deleteEmployeeByIdTest() throws Exception {

		when(employeeService.deleteEmployeeById(any())).thenReturn(true);

		MvcResult result = this.mockMvc.perform(delete("/v1/employee/delete/1")).andExpect(status().isOk()).andReturn();

		ObjectMapper mapper = new ObjectMapper();
		String jsonResponse = result.getResponse().getContentAsString();
		SuccessResponse<Boolean> successResponse = mapper.readValue(jsonResponse,
				mapper.getTypeFactory().constructParametricType(SuccessResponse.class, Boolean.class));

		assertEquals("OK", successResponse.getMessage());
		assertEquals(true, successResponse.getData());
	}

	@DisplayName("Get all department")
	@Test
	public void getAllDepartmentTest() throws Exception {
		
		List<EmployeeDTO> list = new ArrayList<>();
		list.add(getResponse());

		PageRequest pageRequest = PageRequest.of(0, 5);
		Page<EmployeeDTO> pageData = new PageImpl<>(list, pageRequest, 1);

		when(employeeService.getAllEmployee(anyInt(), anyInt(), anyBoolean())).thenReturn(pageData);

		this.mockMvc.perform(get("/v1/employee/getAll")).andExpect(status().isOk()).andReturn();
	}

	private EmployeeDTO getResponse() {
		EmployeeDTO response = new EmployeeDTO();
		response.setId(1l);
		response.setName("Test");
		response.setEmail("test@gmail.com");

		DepartmentDTO department = new DepartmentDTO();
		department.setId(1l);
		department.setName("IT");
		response.setDepartment(department);
		return response;
	}

	private EmployeeRequestDTO getRequest() {
		EmployeeRequestDTO request = new EmployeeRequestDTO();
		request.setName("Test");
		request.setEmail("test@gmail.com");
		request.setDepartmentId(1l);
		return request;
	}
}

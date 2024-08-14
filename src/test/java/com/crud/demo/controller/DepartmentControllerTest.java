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
import com.crud.demo.dto.common.SuccessResponse;
import com.crud.demo.service.DepartmentService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(DepartmentController.class)
@ExtendWith(MockitoExtension.class)
public class DepartmentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private DepartmentService departmentService;

	@DisplayName("Create department")
	@Test
	public void createDepartmentTest() throws Exception {

		DepartmentDTO response = getResponse();

		when(departmentService.createDepartment(any(DepartmentDTO.class))).thenReturn(response);

		ObjectMapper mapper = new ObjectMapper();
		String requestJson = mapper.writeValueAsString(getRequest());

		MvcResult result = this.mockMvc
				.perform(post("/v1/department/create").contentType(MediaType.APPLICATION_JSON).content(requestJson))
				.andExpect(status().isOk()).andReturn();

		String jsonResponse = result.getResponse().getContentAsString();
		SuccessResponse<DepartmentDTO> successResponse = mapper.readValue(jsonResponse,
				mapper.getTypeFactory().constructParametricType(SuccessResponse.class, DepartmentDTO.class));

		assertEquals("OK", successResponse.getMessage());
		assertEquals(response.getId(), successResponse.getData().getId());
		assertEquals(response.getName(), successResponse.getData().getName());
	}

	@DisplayName("Update department")
	@Test
	public void updateDepartmentTest() throws Exception {
		
		DepartmentDTO response = getResponse();

		when(departmentService.updateDepartment(any(DepartmentDTO.class))).thenReturn(response);

		ObjectMapper mapper = new ObjectMapper();
		String requestJson = mapper.writeValueAsString(getRequest());

		MvcResult result = this.mockMvc
				.perform(put("/v1/department/update").contentType(MediaType.APPLICATION_JSON).content(requestJson))
				.andExpect(status().isOk()).andReturn();

		String jsonResponse = result.getResponse().getContentAsString();
		SuccessResponse<DepartmentDTO> successResponse = mapper.readValue(jsonResponse,
				mapper.getTypeFactory().constructParametricType(SuccessResponse.class, DepartmentDTO.class));

		assertEquals("OK", successResponse.getMessage());
		assertEquals(response.getId(), successResponse.getData().getId());
		assertEquals(response.getName(), successResponse.getData().getName());
	}

	@DisplayName("Get department by id")
	@Test
	public void getDepartmentByIdTest() throws Exception {
		
		DepartmentDTO response = getResponse();

		when(departmentService.getDepartmentById(any())).thenReturn(response);

		MvcResult result = this.mockMvc.perform(get("/v1/department/get/1")).andExpect(status().isOk()).andReturn();

		ObjectMapper mapper = new ObjectMapper();
		String jsonResponse = result.getResponse().getContentAsString();
		SuccessResponse<DepartmentDTO> successResponse = mapper.readValue(jsonResponse,
				mapper.getTypeFactory().constructParametricType(SuccessResponse.class, DepartmentDTO.class));

		assertEquals("OK", successResponse.getMessage());
		assertEquals(response.getId(), successResponse.getData().getId());
		assertEquals(response.getName(), successResponse.getData().getName());
	}

	@DisplayName("Delete department by id")
	@Test
	public void deleteDepartmentByIdTest() throws Exception {

		when(departmentService.deleteDepartmentById(any())).thenReturn(true);

		MvcResult result = this.mockMvc.perform(delete("/v1/department/delete/1")).andExpect(status().isOk())
				.andReturn();

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
		
		List<DepartmentDTO> list = new ArrayList<>();
		list.add(getResponse());
		
		PageRequest pageRequest = PageRequest.of(0, 5);
		Page<DepartmentDTO> pageData = new PageImpl<>(list, pageRequest, 1);

		when(departmentService.getAllDepartment(anyInt(), anyInt(), anyBoolean())).thenReturn(pageData);

		this.mockMvc.perform(get("/v1/department/getAll")).andExpect(status().isOk()).andReturn();
	}

	private DepartmentDTO getResponse() {
		DepartmentDTO response = new DepartmentDTO();
		response.setId(1l);
		response.setName("IT");
		return response;
	}

	private DepartmentDTO getRequest() {
		DepartmentDTO request = new DepartmentDTO();
		request.setName("IT");
		return request;
	}
}

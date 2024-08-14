package com.crud.demo.entity.common;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@MappedSuperclass
@Data
public class BaseEntity {

	@Column(name = "created_by")
	private Long createdBy;

	@Column(name = "created_date")
	private ZonedDateTime createdDate;

	@Column(name = "modified_by")
	private Long modifiedBy;

	@Column(name = "modified_date")
	private ZonedDateTime modifiedDate;

}

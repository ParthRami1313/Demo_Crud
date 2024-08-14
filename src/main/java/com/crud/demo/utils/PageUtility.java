package com.crud.demo.utils;

import org.springframework.data.domain.PageRequest;

public class PageUtility {

	public static PageRequest getPagination(int pageNumber, int pageSize, boolean isAllDataRequired) {
		if (isAllDataRequired) {
			return PageRequest.of(0, Integer.MAX_VALUE);
		} else {
			return PageRequest.of(pageNumber, pageSize);
		}
	}

}

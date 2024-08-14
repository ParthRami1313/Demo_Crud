package com.crud.demo.utils;

import java.io.Serializable;

public class StringLiterals implements Serializable {

	private static final long serialVersionUID = 1L;

	private StringLiterals() {
	}

	public static final String OK = "OK";

	public static final String EMAIL_REGEX = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

}

package com.crud.demo.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class DateUtility {

	public static ZonedDateTime getCurrentUTCDate() {
		return LocalDateTime.now().atZone(ZoneOffset.UTC);
	}
}

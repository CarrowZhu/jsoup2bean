package com.siyuan.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	public static String format(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}
	
	public static Date parse(String dateStr, String format) throws ParseException {
		return new SimpleDateFormat(format).parse(dateStr);
	}
 	
}

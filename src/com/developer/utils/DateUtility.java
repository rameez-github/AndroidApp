package com.developer.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtility {
	
	public static Date changeTimeStampToDate (String orgTimeStamp, String toFormat){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(Long.parseLong(orgTimeStamp) * 1000);
		return cal.getTime();
	}
	
	public static String changeTimeStampToString(String orgTimeStamp, String toFormat) {
		
		DateFormat objFormatter = new SimpleDateFormat(toFormat);
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(Long.parseLong(orgTimeStamp) * 1000);
		
		String result = objFormatter.format(cal.getTime());
		cal.clear();
		return result;
	}
	
	public static int calculateAgeForTimeStamp(String orgTimeStamp) {
		
		if (orgTimeStamp == null || orgTimeStamp.equals("")) return 0;
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis((long)(Double.parseDouble(orgTimeStamp) * 1000));
		
		Calendar today = Calendar.getInstance();
		
		int age = today.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
		
		if (today.get(Calendar.DAY_OF_YEAR) <= cal.get(Calendar.DAY_OF_YEAR)) {
			age--;
		}
		
		return age;
	}
	
	public static Date changeStringToDate(String orgDateString, String format) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		try {
			return simpleDateFormat.parse(orgDateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String changeDateToString(Date date, String format) {
		DateFormat objFormatter = new SimpleDateFormat(format);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		String result = objFormatter.format(cal.getTime());
		cal.clear();
		
		return result;
	}
	
	public static String changeDateStringToTimeStamp(String dateString, String orgFormat) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(orgFormat);
		try {
			Date date = simpleDateFormat.parse(dateString);
			return "" + (date.getTime() / 1000);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
}

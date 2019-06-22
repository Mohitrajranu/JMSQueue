package com.jms.epc;
/*
@Author:MohitRaj 2017
*/

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Date_Prog {
	
	//public static void main (String args[])
	//{
	//	Date_prog dp = new Date_prog();
	
	//	System.out.println(" Date : " + dp.getDate());
	//}

	public String getDate()
	{
		Calendar cal = Calendar.getInstance(); 
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	    String today=dateFormat.format(cal.getTime());
		return today;
	}
	public static String getYesterdayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);    
        return dateFormat.format(cal.getTime());
	}	
	public static String getYesterdayDateString2() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);    
        return dateFormat.format(cal.getTime());
	}	

	public String getDateonly()
	{
		Calendar cal = Calendar.getInstance(); 
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
	    String today=dateFormat.format(cal.getTime());
		return today;
	}
	public String getCurrentDateTime()
	{
		Date date = new Date();
		SimpleDateFormat simpledateformat = new SimpleDateFormat("hhmmssSSSSSS");
		String datetime = simpledateformat.format(date);
		datetime = datetime.substring(0,2)+":"+datetime.substring(2,4)+":"+datetime.substring(4,6)+"."+datetime.substring(6);
		return datetime;
	}
	
	public static String getCurrentDate()
	{
	 Date date = new Date();
	 DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	 String today = dateFormat.format(date);
	 return today;
	}
	public static String getCurrentDateYBL()
	{
	 Date date = new Date();
	 DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
	 String today = dateFormat.format(date);
	 return today;
	}
	public static String getBatch()
	{
	 Date date = new Date();
	 DateFormat dateFormat = new SimpleDateFormat("hh");
	 String today = dateFormat.format(date);
	 today = today+"00";
	 return today;
	}
	
	public static String getCurrentDate2()
	{
	 Date date = new Date();
	 DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
	 String today = dateFormat.format(date);
	 return today;
	}
	public static String getCurrentTimeStampStr() {
		String val="";
		Timestamp val2 = new Timestamp(System.currentTimeMillis());
		val = val2.toString();
		return val;
	}
	public static java.sql.Timestamp getCurrentTimeStamp() {
		 
		return new Timestamp(System.currentTimeMillis());
	 
	}
}

package com.jms.epc;
/*
@Author:MohitRaj 2017
*/

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Timestamp;

public class Details1
{
	
InputStream in;
String propertyfile_db="/DB_Details.properties";
String ftpprop="/ftp.properties";

public InputStream getPropertyfile()
{
	in=Details1.class.getResourceAsStream(propertyfile_db);
	return in;
}

public InputStream getFTPfile()
{
	in=Details1.class.getResourceAsStream(ftpprop);
	return in;
}

public static int dateDiffDays(java.util.Date d1, java.util.Date d2) {

    return (int) ((d2.getTime() / 86400000) - (d1.getTime() / 86400000));

}

public static Timestamp utilDateToSQLTimestamp(java.util.Date date){
	try {
		return new java.sql.Timestamp(date.getTime());
	}
	catch(Exception ex)	{}
	return null;
}


public static boolean isNullString (String text)
{
        if (null != text)
        {

                if (text.trim().length()==0) return true;
                return false;
        }
        else
        {
           return true;
        }
}//end of isNullString


public static String getCurrentTimeStampStr() {
	String val="";
	Timestamp val2 = new Timestamp(System.currentTimeMillis());
	val = val2.toString();
	return val;
}

	public static String printException(Exception e){
		try{
			Writer writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			e.printStackTrace(printWriter);
			String s = writer.toString();
			return s;
}
		catch(Exception e2){
			e2.printStackTrace();
			return "unable to Handle Exception";
		}
	}
}

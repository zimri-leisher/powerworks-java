package powerworks.io;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Calendar;

public class Logger {
    static PrintWriter writer = null;
    static Calendar cal = Calendar.getInstance();
    static {
	try {
	    writer = new PrintWriter(Logger.class.getResource("/logs/log.txt").getFile());
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
    }
    public static void log(String message) {
	message = "[LOG] : " + cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.MILLISECOND) + " - " + message;
	System.out.println(message);
	writer.println(message);
    }
    
    public static void error(String message) {
	message = "[ERROR] : " + cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.MILLISECOND) + " - " + message;
	writer.println(message);
	System.out.println(message);
    }
    
    public static void warning(String message) {
	message = "[WARNING] : " + cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.MILLISECOND) + " - " + message;
	writer.println(message);
	System.out.println(message);
    }
    
    public static void p(String message) {
	System.out.println(message);
    }
    
    public static void close() {
	writer.close();
    }
}

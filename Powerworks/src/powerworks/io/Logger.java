package powerworks.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.Map.Entry;

public class Logger {

    BufferedWriter writer = null;
    Calendar cal = Calendar.getInstance();
    HashMap<Statistic, IntSummaryStatistics> stats = new HashMap<Statistic, IntSummaryStatistics>();
    HashMap<Statistic, Integer> toAdd = new HashMap<Statistic, Integer>();
    ArrayList<Statistic> toLog = new ArrayList<Statistic>();
    File f;
    boolean used = false, closed;

    public Logger() {
	try {
	    String s = "/logs/log " + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR) + "-1.txt";
	    f = new File(s);
	    int count = 2;
	    while (f.exists()) {
		s = "/logs/log " + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR) + "-" + count + ".txt";
		f = new File(s);
		count++;
	    }
	    writer = new BufferedWriter(new FileWriter(s));
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void addData(Statistic s, int value) {
	if (stats.containsKey(s))
	    stats.get(s).accept(value);
	else {
	    IntSummaryStatistics stat = new IntSummaryStatistics();
	    stat.accept(value);
	    stats.put(s, stat);
	}
    }

    public void addAndLog(Statistic s, int value) {
	addData(s, value);
	logData(s);
    }

    public void addAndLog(Statistic s, int value, boolean wait) {
	if (wait) {
	    addData(s, value, true);
	    logData(s, true);
	} else
	    addAndLog(s, value);
    }

    /**
     * For performance sensitive loggings
     */
    public void addData(Statistic s, int value, boolean wait) {
	if (wait) {
	    toAdd.put(s, value);
	} else
	    addData(s, value);
    }

    public void logData(Statistic s) {
	IntSummaryStatistics stat = stats.get(s);
	log(s.name + ": Avg: " + stat.getAverage() + ", Max: " + stat.getMax() + ", Min: " + stat.getMin());
    }

    /**
     * For performance sensitive loggings
     */
    public void logData(Statistic s, boolean wait) {
	if (wait) {
	    if (!toLog.contains(s))
		toLog.add(s);
	} else
	    logData(s);
    }

    public void log(String message) {
	cal = Calendar.getInstance();
	message = "[LOG] : " + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + (cal.get(Calendar.YEAR) % 100) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":"
		+ cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.MILLISECOND) + " - " + message;
	System.out.println(message);
	try {
	    writer.append(message + "\n");
	    used = true;
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void error(String message) {
	cal = Calendar.getInstance();
	message = "[ERROR] : " + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + (cal.get(Calendar.YEAR) % 100) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":"
		+ cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.MILLISECOND) + " - " + message;
	System.out.println(message);
	try {
	    writer.append(message + "\n");
	    used = true;
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void warning(String message) {
	cal = Calendar.getInstance();
	message = "[WARNING] : " + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + (cal.get(Calendar.YEAR) % 100) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":"
		+ cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.MILLISECOND) + " - " + message;
	System.out.println(message);
	try {
	    writer.append(message + "\n");
	    used = true;
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void p(Object message) {
	System.out.println(message + " log");
    }

    public void close() {
	if(closed) return;
	closed = true;
	for (Entry<Statistic, Integer> e : toAdd.entrySet()) {
	    addData(e.getKey(), e.getValue());
	}
	if (toLog.size() != 0) {
	    log("--------------------------------------");
	    log("Writing all data that was told to wait");
	    log("--------------------------------------");
	    for (Statistic s : toLog) {
		logData(s);
	    }
	}
	try {
	    writer.close();
	    if (!used)
		f.delete();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}

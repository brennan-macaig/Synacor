package me.brennanmacaig.synacor.vm.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
	
	public static String filePath = "syslog";
	public static String logName = "";
	/** 
	 * Must be run the first time through 
	 */
	public static void init() {
		logName = buildLogName();
		log("# SYSTEM LOGGER INIT #", "LOGGER", LogTypes.INFO);
	}
	
	public static void log(String message, String fromApp, LogTypes logtype) {
			File f = new File(filePath);
			if (f.exists() && (!f.isDirectory())) {
				// File exists
				writeToLog(message, fromApp, logtype);
			} else if (!(f.exists() && (!f.isDirectory()))) {
				createLogFile();
				writeToLog(message, fromApp, logtype);
		}
	}
	public static void osOut(String message) {
		writeToOS(message);
	}
	
	private static String buildLogName() {
		return "" + filePath + "-" + getCurrentTime().toString() + ".log";
	}
	
	private static void createLogFile() {
		filePath = buildLogName();
		PrintWriter writer;
		try {
			File file = new File(filePath);
			writer = new PrintWriter(filePath, "UTF-8");
			writer.print("\n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		

		
	}
	
	private static String buildMessage(String message, LogTypes level, String fromApp) {
		return "[" + getCurrentTime() + "] [" + fromApp + "] [" + level.toString() + "] " + message + "\n";
	}
	private static void writeToOS(String message) {
		try {
			Files.write(Paths.get(logName), (message).getBytes(), StandardOpenOption.APPEND);
			System.out.write(message.getBytes());
		} catch (IOException e) {
			// oop
			e.printStackTrace();
		}
	}
	
	private static void writeToLog(String write, String fromApp, LogTypes level) {
		try {
		    Files.write(Paths.get(logName), buildMessage(write, level, fromApp).getBytes(), StandardOpenOption.APPEND);
		    System.out.println(buildMessage(write, level, fromApp));
		}catch (IOException e) {
			// oop
			e.printStackTrace();
		}
	}
	
	private static String getCurrentTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		Date date = new Date();
		return dateFormat.format(date).toString();
	}
}

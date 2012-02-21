package com.devcamp.logging;

import android.os.Debug;
import android.util.Log;

/**
 * Allows convenient methods to log messages to the output.
 * This helper has been modeled around the use of Log4j.
 * 
 * As of now, this class only serves to help debug messages. 
 * This class will need to extend methods to allow passing objects in
 * 
 * @author ntikku
 *
 */
public class Logger {
	
	String name = null; // the name of the logger instance
	int level = Log.INFO; // base log level
	
	/**
	 * Instantiate a LogHelper by providing the class
	 * @param clz
	 */
	private Logger(Class<?> clz){
		this.name = clz.getSimpleName();
		if(Debug.isDebuggerConnected()) {
			setLevel(Log.DEBUG);
		}
	}
	
	public static final Logger getLogger(Class<?> clz){
		return new Logger(clz);
	}

	/**
	 * A HARDCORE DEBUG level log statement
	 * @param message
	 */
	public void trace(String message){
		if(Log.isLoggable(this.name, Log.VERBOSE))
			Log.v(this.name, message);
	}

	/**
	 * A DEBUG level log statement
	 * @param message
	 */
	public void debug(String message){
		if(this.level <= Log.DEBUG
			|| Log.isLoggable(this.name, Log.DEBUG))
			Log.d(this.name, message);
	}
	
	/**
	 * An INFO level log statement
	 * @param message
	 */
	public void info(String message){
		if(this.level <= Log.INFO
			|| Log.isLoggable(this.name, Log.INFO))
			Log.i(this.name, message);
	}
	
	/**
	 * An ERROR log statement
	 * @param message
	 */
	public void error(String message){
		if(this.level <= Log.ERROR
			|| Log.isLoggable(this.name, Log.ERROR))
			Log.e(this.name, message);
	}
	
	/**
	 * A WARN level log statement
	 * @param message
	 */
	public void warn(String message){
		if(this.level <= Log.WARN
			|| Log.isLoggable(this.name, Log.WARN))
			Log.w(this.name, message);
	}
	
	/**
	 * SEVERE log level statement
	 * @param message
	 */
	public void fail(String message){
		Log.wtf(this.name, message);
	}
	
	/**
	 * set a particular level
	 * @param level
	 */
	public void setLevel(int level){
		this.level = level;
	}
	
	// LAME, System.setProperty doesn't work.
	// http://developer.android.com/reference/android/util/Log.html#isLoggable%28java.lang.String,%20int%29
	public static String resolveLogLevel(int level){
		switch(level){
			case Log.VERBOSE: return "VERBOSE";
			case Log.DEBUG: return "DEBUG";
			case Log.INFO: return "INFO";
			case Log.WARN: return "WARN";
			case Log.ERROR: return "ERROR";
			default: 
				return null;
		}
	}

}

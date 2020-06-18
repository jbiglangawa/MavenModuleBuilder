/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.util.constants;


public class Constants {
	/*
	 * Templates
	 */
	public static final String STATUS_TEMPLATE = "Last Build: %-8s Duration: %-10s Status: %s";

	/*
	 * Command Constants
	 */
	public static final String AND = "&&";
	public static final String CD = "CD";
	public static final String MVN = "mvn";
	public static final String CLEAN = "clean";
	public static final String ONLINE = "-o";
	public static final String SET = "set";
	public static final String EQUAL = "=";
	public static final String SRC = "src";
	public static final String ARBITRARY_ECHO = "echo . ";
	public static final String SPACE = " ";
	public static final String CONNECTOR = AND + SPACE;
	
	// System Environment command {0} = label, {1} = directory
	public static final String SYSENV_TEMPLATE = "SET {0}={1}";
	
	// Maven command {0} clean parameter, {1} = goal, {2} = online parameter, {3} = build parameters
	public static final String MAVEN_TEMPLATE = "MVN {0} {1} {2} {3}";
	
	// Get JAR Command {0} = Drive letter, {1} = ModulePath
	public static final String GETJAR_TEMPLATE = "{0} && CD {1}\\target && dir /b *.jar";
	
	// Overall replace command. {0} = Drive letter, {1} Destination, {2} = filename, {3} = source
	public static final String COPYJAR_TEMPLATE = "{0} && CD \"{1}\" && echo. && echo [INFO] Attempting to copy to {1} && (if exist {2} (xcopy /y {3}\\{2} {1}) else (echo file not found) )";
		
	
	/**
	 * Command to stop the running process
	 */
	public static final String STOP_COMMAND = "@for /f \"tokens=1\" %i in ('jps -m ^| find \"Launcher\"') do @(taskkill /F /PID %i)";
	
	
	/*
	 * Other Constants
	 */
	public static final String TARGET = "target";
	public static final String BUILD = "build";
	public static final String REPLACE = "replace";
	public static final String INSTALL = "install";
	public static final String COMPILE = "compile";
	public static final String PACKAGE = "package";
	public static final String SUCCESS = "Success";
	public static final String FAILED = "Failed";
	public static final String SETTINGS_XML = "settings.xml";
	public static final String FONT_DIALOG = "Dialog";
	
	
	/*
	 * Message Box message
	 */
	
}

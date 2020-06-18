/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.mmb.models.BuildCommand;
import com.mmb.models.execution.Execution;
import com.mmb.models.filters.Filter;
import com.mmb.models.sysenv.SysEnv;
import com.mmb.models.target.Target;
import com.mmb.util.constants.Constants;

public class CommandUtil {
	private static Logger logger = Logger.getLogger(CommandUtil.class);
	private static Logger commandLogger = Logger.getLogger("command");
	
	/**
	 * Construct command using buildCommand.
	 * 
	 * @param buildCommand This will contain the filter and execution for the corresponding module path
	 * @param isSingle Signifies whether the build is single or multi. If true single, else, multi. If multi, the command will skip
	 * 			setting up system environment variables
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static String constructCommand(BuildCommand buildCommand, Boolean isSingle) {
		commandLogger.debug("BuildCommand: " + buildCommand.toString() + ", isSingle: " + isSingle);
		
		String command = null;
		if(buildCommand.getBuildInd()) {
			command = mavenBuildCommand(buildCommand, isSingle);
		}
		if(buildCommand.getReplaceInd()) {
			command = makeCommand(command, constructReplaceJARCommand(buildCommand.getModulePath()));
		}
		
		commandLogger.debug("Constructed command: " + command);
		return command;
	}
	
	/**
	 * Construct Maven Build command using buildCommand
	 * 
	 * @param buildCommand This will contain the filter and execution for the corresponding module path
	 * @param isSingle Signifies whether the build is single or multi. If true single, else, multi. If multi, the command will skip
	 * 			setting up system environment variables
	 * @return
	 */
	public static String mavenBuildCommand(BuildCommand buildCommand, Boolean isSingle) {
		/*
		 * Defaults implementation
		 * Every field of maven opts must not be null to proceed
		 */
		String mavenOpts = XMLUtil.getInstance().getDefaults().getMavenOpts();
		String[] splitBuildDir = XMLUtil.getInstance().getDefaults().getProjectPath().split("\\\\");
		String driveDirectory = splitBuildDir[0];
		String sysEnvVariables = (isSingle) ? setupSystemEnvVariables() : null;
		
		/*
		 * Execution Module Implementation
		 * If beforeCommand is not null, before command will be executed
		 * If afterCommand is not null, after commnad will be executed
		 */
		String beforeCommand = "";
		String afterCommand = "";
		if(buildCommand.getExecution() != null) {
			Execution execution = buildCommand.getExecution();
			if(!Util.isNullOrEmpty(execution.getBeforeCommand().getName())) {
				beforeCommand = makeCommand(execution.getBeforeCommand().getCommand());
			}
			if(!Util.isNullOrEmpty(execution.getAfterCommand().getName())) {
				afterCommand = makeCommand(execution.getAfterCommand().getCommand());
			}
		}
		
		/*
		 * Filter Module Implementation
		 * If opts is not null, maven opts default will be replaced with the custom
		 */
		if(buildCommand.getFilter() != null && !Util.isNullOrEmpty(buildCommand.getFilter().getOpts().getName())) {
			mavenOpts = "SET MAVEN_OPTS=" + buildCommand.getFilter().getOpts().getName();
		}
		
		return makeCommand(
				beforeCommand,
				sysEnvVariables,
				mavenOpts,
				driveDirectory,
				gotoPathCommand(buildCommand.getModulePath()),
				constructMavenCommand(buildCommand, isSingle),
				afterCommand
		);
	}
	
	/**
	 * Will construct one line command. This will add && after every each command
	 * 
	 * @param commands
	 * @return
	 */
	public static String makeCommand(String... commands) {
		String allCommand = "";
		
		for(int i = 0; i < commands.length; i++) {
			if(!Util.isNullOrEmpty(commands[i])) {
				allCommand = allCommand.concat(commands[i]).concat(Constants.SPACE);
			}else {
				allCommand = allCommand.concat(Constants.ARBITRARY_ECHO);
			}
			
			if(i < commands.length - 1) {
				allCommand = allCommand.concat(Constants.CONNECTOR);
			}
		}
		
		return allCommand;
	}
	
	/**
	 * Setup system environment variables
	 * 
	 * @param sysEnvList
	 * @return
	 */
	public static String setupSystemEnvVariables() {
		String commandString = "";
		List<SysEnv> sysEnvList = XMLUtil.getInstance().getSysEnvList();
		for(int i = 0; i < sysEnvList.size(); i++) {
			SysEnv sysEnv = sysEnvList.get(i);
			commandString = commandString.concat(Util.formatString(Constants.SYSENV_TEMPLATE, sysEnv.getLabel(), sysEnv.getDirectory().getName()));
			
			if(i < sysEnvList.size() - 1) {
				commandString = commandString.concat(Constants.CONNECTOR);
			}
		}
		return commandString;
	}
	
	/**
	 * Construct Maven code
	 * 
	 * @param buildCommand
	 * @param isSingle
	 * @return
	 */
	public static String constructMavenCommand(BuildCommand buildCommand, Boolean isSingle) {
		StringBuilder command = new StringBuilder();
		String cleanParameter = Constants.CLEAN;
		String onlineParameter = Constants.ONLINE;
		String buildParameters = XMLUtil.getInstance().getDefaults().getBuildParameters();
		
		if(buildCommand.getFilter() != null) {
			Filter filter = buildCommand.getFilter();
			if(!filter.getIsClean()) cleanParameter = "";
			if(filter.getIsOnline()) onlineParameter = "";
		}
		
		if(!isSingle) {
			command.append("call").append(Constants.SPACE);
		}
		
		command.append(Util.formatString(Constants.MAVEN_TEMPLATE, cleanParameter, buildCommand.getGoal(), onlineParameter, buildParameters));
		
		return command.toString();
	}
	
	/**
	 * Construct cd command using given path
	 * 
	 * @param path
	 * @return
	 */
	public static String gotoPathCommand(String path) {
		return Constants.CD + " " + path;
	}
	
	/**
	 * Stops the build process
	 * 
	 * @return
	 */
	public static Process stopBuild() {	
		return ProcessUtil.runProcess(Constants.STOP_COMMAND);
	}
	
	/**
	 * Construct replace command. The command will check if the jar exists in the target folder first.
	 * If the jar is existing, it will replace the jar file.
	 * 
	 * @param modulePath
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static String constructReplaceJARCommand(String modulePath) {
		String driveLetter = modulePath.split("\\\\")[0];
		String source = modulePath + File.separator + Constants.TARGET;
		String fileName = getDirectoryJarFileName(modulePath);
		String command = "";
		
		List<Target> targetList = XMLUtil.getInstance().getTargetList();
		
		if(fileName.equalsIgnoreCase("File Not Found")) {
			JOptionPane.showMessageDialog(null, 
					"The JAR file in the target folder of your module is missing. Skipping replace in this build.");
			return "";
		}
		
		for(int i = 0; i < targetList.size(); i++) {
			String destination = targetList.get(i).getDirectory().getName();
			command += Util.formatString(Constants.COPYJAR_TEMPLATE, driveLetter, destination, fileName, source);
			
			if(i < targetList.size() - 1) {
				command += " && ";
			}
		}
		
		return command;
	}
	
	/**
	 * Gets the jar file located at the target folder of the module path.
	 * if the directory has 2 or more jar files, this method might throw an exception
	 * 
	 * @param modulePath
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static String getDirectoryJarFileName(String modulePath) {
		logger.debug("Invoked getDirectoryJarFileName: " + modulePath);
		
		String driveLetter = modulePath.split("\\\\")[0];
		String command = Util.formatString(Constants.GETJAR_TEMPLATE, driveLetter, modulePath);
		commandLogger.debug("Getting Jar fileName of modulePath " + modulePath + ": " + command);
		
		Process process = ProcessUtil.runProcess(command);
		BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
		
		String jarFileName = null;
		try {
			jarFileName = r.readLine();
		} catch (IOException e) {
			logger.error("IOException occurred when getting jar file name of modulePath: " + e.getMessage(), e);
		}
		logger.debug("JarFileName of modulePath: " + jarFileName);
		return jarFileName;
	}
	
}

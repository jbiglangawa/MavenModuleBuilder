/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.util;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.mmb.models.BuildCommand;

public class ProcessUtil {
	private static final Logger buildLogger = Logger.getLogger("build");
	
	/**
	 * Method will construct the command and run them. The method invokes
	 * runProcess(constructedCommand) after the command is constructed
	 * 
	 * @param buildCommand
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static final Process triggerBuild(BuildCommand buildCommand) {
		return runProcess(buildCommand.construct());
	}
	
	/**
	 * Directly runs the command and attach it to the process
	 * 
	 * @param command
	 * @return
	 */
	public static Process runProcess(String command) {
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
		builder.redirectErrorStream(true);
		Process process = null;
		
		try {
			process = builder.start();
		} catch (IOException e) {
			buildLogger.debug("IOException occured: " + e.getMessage(), e);
		}
		
		return process;
	}
	
}

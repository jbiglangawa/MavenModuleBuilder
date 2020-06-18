/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.mmb.models.BuildCommand;
import com.mmb.models.filters.Filter;
import com.mmb.util.constants.Constants;
import com.mmb.windows.MainWindow;
import com.mmb.windows.MultiModuleWindow;

public class MultiModuleHandler {
	private static Logger logger = Logger.getLogger(MultiModuleHandler.class);
	
	private static MultiModuleHandler multiModuleHandler;
	
	public static synchronized MultiModuleHandler getInstance() {
		if(multiModuleHandler == null) {
			multiModuleHandler = new MultiModuleHandler();
		}
		
		return multiModuleHandler;
	}
	
	public Process buildMultipleModules() {
		List<String> modulePathList = MultiModuleWindow.getInstance().getMultiModuleModel().getModulePathList();
		String projectPath = XMLUtil.getInstance().getDefaults().getProjectPath();
		List<String> commands = new ArrayList<String>();
		
		commands.add("@echo OFF");
		commands.add(CommandUtil.setupSystemEnvVariables());
		for(String modulePath : modulePathList) {
			BuildCommand buildCommand = new BuildCommand(MainWindow.getInstance());
			buildCommand.setModulePath(projectPath + File.separator + modulePath);
			
			// Override the buildInd, replaceInd and goal from the Filter of the modulePath
			Filter filter = buildCommand.getFilter();
			if(filter != null) {
				buildCommand.setBuildInd(filter.getAction().getName().equalsIgnoreCase(Constants.BUILD));
				buildCommand.setReplaceInd(filter.getAction().getName().equalsIgnoreCase(Constants.REPLACE));
				buildCommand.setGoal(filter.getGoal().getName());
			}
			
			String[] command = null;
			command = buildCommand.construct(false).split(" && ");
			
			for(int i = 0; i < command.length; i++) {
				commands.add(command[i]);
			}
		}
		
		constructBatFileHandler(commands);
		return ProcessUtil.runProcess(getBatFile().getAbsolutePath());
	}
	
	public void constructBatFileHandler(List<String> commandList) {
		File batFile = getBatFile();

		try {
			FileWriter writer = new FileWriter(batFile);
			PrintWriter printWriter = new PrintWriter(writer);

			for(String command : commandList) {
				printWriter.printf("%s" + "%n", command);
			}

			printWriter.close();
		} catch (IOException e) {
			logger.error("IOException occurred while construbting temporary .bat file", e);
		}
	}
	
	public File getBatFile() {
		String filePath = "temp";
		String batFilePath = filePath  + File.separatorChar + "temp.bat";

		File tempBatFile = new File(filePath);
		if(!tempBatFile.exists()) {
			tempBatFile.mkdir();
		}
		
		return new File(batFilePath);
	}
}

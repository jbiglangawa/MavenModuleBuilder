/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;

import com.mmb.models.POMDirectory;
import com.mmb.util.constants.Constants;

public class POMScanner {
	private static Logger logger = Logger.getLogger(POMScanner.class);
	private List<String> directoriesWithPOM = new ArrayList<String>();
	private static POMScanner pomFileScanner;
	
	public static synchronized POMScanner getInstance() {
		if(pomFileScanner == null) {
			pomFileScanner = new POMScanner();
		}
		return pomFileScanner;
	}
	
	public POMScanner() {
		pomFileScanner = this;
	}
	
	/**
	 * System will scan the src folder of the project path. System will add a node
	 * recursively as long as a directory inside the project path has pom.xml
	 * 
	 * @return
	 */
	public DefaultMutableTreeNode scan() {
		File file = new File(XMLUtil.getInstance().getDefaults().getProjectPath() + File.separator + Constants.SRC);

		logger.debug("Scanning Project Path: " + file.getAbsolutePath());
		long startTime = System.currentTimeMillis();
		
		File[] list = file.listFiles();
		scanForPOM(file, list);
		
		long endTime = System.currentTimeMillis() - startTime;
		logger.debug("scan took " + new Double(endTime)/new Double(1000) + " seconds");
		
		return processPOMDirectories();
	}
	
	/**
	 * Method to process the directories the scanForPOM method has scavenged.
	 * 
	 * @return
	 */
	private DefaultMutableTreeNode processPOMDirectories() {
		POMDirectory root = new POMDirectory();
		String projectPath = XMLUtil.getInstance().getDefaults().getProjectPath();
		logger.debug("[INFO] The following are directories with POM in its directory");
		for(String directory : directoriesWithPOM) {
			logger.debug(directory);
			directory = directory.replace(projectPath + File.separator, "");
			root.addDirectory(directory);
		}
		logger.debug("[INFO] ================================");
		return root.convert();
	}
	
	/**
	 * Scans directories with pom.xml file. All of it will be added to 
	 * directoriesWithPOM
	 * 
	 * @param currentDirectory
	 * @param fileList
	 */
	private void scanForPOM(File currentDirectory, File[] fileList) {
		if(fileList != null) {
			for(int i = 0; i < fileList.length; i++) {
				File fileFromList = fileList[i];
				
				if(fileFromList.isFile()) {
					String fileName = fileFromList.getName();
					String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
					
					if(extension.equalsIgnoreCase("XML")) {
						if(fileName.equalsIgnoreCase("pom.xml")) {
							directoriesWithPOM.add(currentDirectory.getAbsolutePath());
						}
					}
				}else {
					if(!fileFromList.getName().equalsIgnoreCase(".svn") && !fileFromList.getName().equalsIgnoreCase("target")) {
						scanForPOM(fileFromList, fileFromList.listFiles());
					}
				}
			}
		}
	}
	
}

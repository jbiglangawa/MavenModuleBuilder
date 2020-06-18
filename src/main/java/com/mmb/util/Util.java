/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.util;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import mdlaf.MaterialLookAndFeel;

public class Util {
	private static final Logger logger = Logger.getLogger(Util.class);
	
	public static String getSelectedModule(TreePath treePath) {
		Object[] objArray = treePath.getPath();
		StringBuilder path = new StringBuilder();
		
		for(int i = 0; i < objArray.length; i++) {
			path.append(objArray[i].toString());
			
			if(i < objArray.length - 1) {
				path.append("\\");
			}
		}
		
		return path.toString();
	}
	
	public static Boolean isNullOrEmpty(String string) {
		return (string == null || string.equalsIgnoreCase(""));
	}
	
	public static JFileChooser getWindowsJFileChooser() {
		return getWindowsJFileChooser(null);
	}
	
	public static JFileChooser getWindowsJFileChooser(String directory) {
		JFileChooser fileChooser = null;
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
			if(!isNullOrEmpty(directory) && new File(directory).exists()) {
				fileChooser = new JFileChooser(directory);
			}else {
				fileChooser = new JFileChooser();
			}
			
			UIManager.setLookAndFeel(new MaterialLookAndFeel());
		} catch (Exception e) {
			logger.error("Exception occurred while changing look and feel to system: " + e.getMessage(), e);
		}
		
		return fileChooser;
	}
	
	/**
	 * Will return a formatted string. The stringToFormat must contain the equvalent index of the variable
	 * in the replace array.
	 * <br>
	 * e.g. 
	 * <div style='margin-left:30'>
	 * 		stringToFormat="The cat {0} is {1} in the {2}"<br>
	 * 		replace[0]="tom"<br>
	 * 		replace[1]="running"<br>
	 * 		replace[2]="backyard"<br>
	 * </div><br>
	 * Will output the following:
	 * <div style='margin-left:30'>
	 * 		returns "The cat <b>tom</b> is <b>running</b> in the <b>backyard</b> 
	 * </div>
	 * <br>
	 * 
	 * @param stringToFormat
	 * @param replace
	 */
	public static String formatString(String stringToFormat, String... replace) {
		for(int i = 0; i < replace.length; i++) {
			stringToFormat = stringToFormat.replace("{" + i + "}", replace[i]);
		}
		return stringToFormat;
	}

}

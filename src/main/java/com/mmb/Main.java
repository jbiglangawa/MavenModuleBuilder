/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.mmb.windows.MainWindow;

import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.JMarsDarkTheme;

public class Main {
	private static Logger logger = Logger.getLogger(Main.class);
	private static Logger activityLogger = Logger.getLogger("activity");
	
	public static void main(String[] opts) {
		try {
			Long startTime = System.currentTimeMillis();
			
			BasicConfigurator.configure();
			UIManager.setLookAndFeel(new MaterialLookAndFeel(new JMarsDarkTheme()));
		
			MainWindow mainWindow = new MainWindow();
			mainWindow.setVisible(true);
			
			Long endTime = System.currentTimeMillis();
			Double totalExecutionTime = new Double(endTime - startTime) / new Double(1000);
			activityLogger.debug("Application opened in " + totalExecutionTime.toString() + " seconds. ");
		} catch (UnsupportedLookAndFeelException e) {
			JOptionPane.showMessageDialog(null, "Error in opening application: " + e.getMessage(), "Fatal error", JOptionPane.ERROR);
			logger.error("UnsupportedLookAndFeelException encountered: " + e.getMessage(), e);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error in opening application: " + e.getMessage(), "Fatal error", JOptionPane.ERROR);
			logger.error("Fatal error encountered in opening the application: " + e.getMessage(), e);
		}
	}
}

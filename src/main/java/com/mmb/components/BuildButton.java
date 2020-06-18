/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import com.mmb.models.BuildCommand;
import com.mmb.resources.Assets;
import com.mmb.util.CommandUtil;
import com.mmb.util.MultiModuleHandler;
import com.mmb.util.ProcessUtil;
import com.mmb.util.Util;
import com.mmb.util.constants.ColorConstants;
import com.mmb.windows.MainWindow;

public class BuildButton extends CircleButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9197005429779652380L;
	private static Logger activityLogger = Logger.getLogger("activity");
	
	private State state;
	
	private Assets assets;
	private MainWindow window;
	private SwingWorker<Boolean,Integer> backgroundBuild;
	private Boolean stopBuildInd = false;
	
	public BuildButton(String text, Color originalColor) {
		super(text, originalColor);
		setState(State.NO_ACTION);
	}
	
	public BuildButton(MainWindow window) {
		super(null, null);
		this.window = window;
		
		assets = new Assets();
		setupHandler();
		
		setState(State.NO_ACTION);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	
	private void setup() {
		if(state == State.NO_ACTION) {
			setImage(assets.getHammer());
			setOriginalColor(ColorConstants.NO_ACTION);
			setPressColor(ColorConstants.NO_ACTION_PRESS);
			setHoverColor(ColorConstants.NO_ACTION_HOVER);
			setToolTipText("Click to build module");
			
			window.updateLastBuildStatusNoActionYet();
		}else if(state == State.SUCCESS) {
			setImage(assets.getSuccess());
			setOriginalColor(ColorConstants.SUCCESS);
			setHoverColor(ColorConstants.SUCCESS_HOVER);
			setPressColor(ColorConstants.SUCCESS_PRESS);
			setToolTipText("Build is successful. Click to build again.");
			
		}else if(state == State.ERROR) {
			setImage(assets.getError());
			setOriginalColor(ColorConstants.ERROR);
			setHoverColor(ColorConstants.ERROR_HOVER);
			setPressColor(ColorConstants.ERROR_PRESS);
			setToolTipText("An error occurred in the build. Click to build again.");
		}else if(state == State.BUILDING) {
			setIcon(assets.getLoading());
			setImage(assets.getLoading().getImage());
			setOriginalColor(ColorConstants.BUILDING);
			
			// "Stop state" will be available on hover on Building
			setHoverColor(ColorConstants.BUILDING);
			setPressColor(ColorConstants.STOP_PRESS);
			setToolTipText("Module is currently building. Click to stop.");
			
			window.updateLastBuildStatusBuilding();
		}
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		if(this.state != state) {
			this.state = state;
			setup();
		}
	}
	
	public void setupHandler() {
		MouseAdapter mouseListener = new MouseAdapter(){
			@Override
			public void mouseExited(MouseEvent me){
				if(state == State.BUILDING) {
					setImage(assets.getLoading().getImage());
				}
			}
			
			@Override
			public void mouseMoved(MouseEvent me){
				if(state == State.BUILDING) {
					setImage(assets.getStop());
				}
			}
			
			@Override
			public void mousePressed(MouseEvent me){
				if(contains(me.getX(), me.getY())){
					triggerAction();
				}
			}
		};
		
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
	}
	
	public void triggerAction() {
		activityLogger.debug("triggerAction Invoked: State: " + this.state.toString());
		
		if(!state.equals(State.BUILDING)) {
			initiateBuild();
		}else {
			stopBuild();
		}
	}
	
	public void initiateBuild() {
		setState(State.BUILDING);
		
		Process buildProcess = null;
		if(!window.getModuleToggleButton().isSelected() && (!Util.isNullOrEmpty(window.getModuleTextField().getText()))) {
			activityLogger.info("Triggering single module build");
			buildProcess = ProcessUtil.triggerBuild(new BuildCommand(window));
		}else {
			activityLogger.info("Triggerring multi module build");
			buildProcess = MultiModuleHandler.getInstance().buildMultipleModules();
		}
		
		executeBackgroundTask(buildProcess);
	}
	
	public void stopBuild() {
		activityLogger.info("Build is now stopped");
		
		stopBuildInd = true;
		Process stopProcess = CommandUtil.stopBuild();
		new SwingWorker<Boolean, Integer>() {
			@Override
			protected Boolean doInBackground() throws Exception {
				Boolean exitValue = window.getBuildLogWindow().printToLog(stopProcess);
				
				String message = 
						"[INFO] ===============================\n" +
						"[INFO] Build is now successfully stopped at" + new Date().toString() + "\n" +
						"[INFO] =================================";
				
				window.getBuildLogWindow().printToLog(message);
				return exitValue;
			}
		}.execute();
	}
	
	public void executeBackgroundTask(final Process buildProcess) {
		if(buildProcess != null) {
			backgroundBuild = new SwingWorker<Boolean, Integer>() {
				Boolean isMulti = window.getModuleToggleButton().isSelected();
				String modulePath = (!isMulti) ? window.getModulePath() : "Multi Module Build";
				long startTime;
				long endTime;
				
				@Override
				protected Boolean doInBackground() throws Exception {
					startTime = System.currentTimeMillis();
					return window.getBuildLogWindow().printToLog(buildProcess);
				}
				
				@Override
				protected void done() {
					if(!stopBuildInd) {
						endTime = System.currentTimeMillis();
						Boolean isSuccess = (buildProcess.exitValue() == 0);
						long duration = endTime - startTime;
						
						// Set the state of the build window
						setState((isSuccess ? State.SUCCESS : State.ERROR));
						
						// Insert to build history
						window.getBuildHistoryWindow().insertNewBuildHistory(modulePath, duration, isSuccess);
						window.updateLastBuildStatusResults(duration, isSuccess);
					}else {
						setState(State.NO_ACTION);
					}
					
					stopBuildInd = false;
					buildProcess.destroyForcibly();
				}
			};
			
			backgroundBuild.execute();
		}
	}
	
}

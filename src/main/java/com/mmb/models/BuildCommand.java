/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.models;

import java.io.File;

import com.mmb.models.execution.Execution;
import com.mmb.models.filters.Filter;
import com.mmb.util.CommandUtil;
import com.mmb.util.ExecutionHandler;
import com.mmb.util.FilterHandler;
import com.mmb.util.XMLUtil;
import com.mmb.windows.MainWindow;

public class BuildCommand {
	private String modulePath;
	private String goal;
	private Boolean buildInd;
	private Boolean replaceInd;
	private Filter filter;
	private Execution execution;
	
	public BuildCommand() {
		
	}
	
	public BuildCommand(String modulePath, String goal, Boolean buildInd, Boolean replaceInd) {
		super();
		this.modulePath = modulePath;
		this.goal = goal;
		this.buildInd = buildInd;
		this.replaceInd = replaceInd;
	}

	public BuildCommand(String modulePath, String goal, Boolean buildInd, Boolean replaceInd, Filter filter) {
		this.modulePath = modulePath;
		this.goal = goal;
		this.buildInd = buildInd;
		this.replaceInd = replaceInd;
		this.filter = filter;
	}
	
	public BuildCommand(MainWindow mainWindow) {
		this.modulePath = mainWindow.getModulePath();
		this.goal = mainWindow.getGoal();
		this.buildInd = mainWindow.getBuildInd();
		this.replaceInd = mainWindow.getReplaceInd();
		this.filter = mainWindow.getFilterHandler().getFilter();
		this.execution = ExecutionHandler.getInstance().getExecution();
	}

	public String getModulePath() {
		return modulePath;
	}

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}

	public Boolean getBuildInd() {
		return buildInd;
	}

	public void setBuildInd(Boolean buildInd) {
		this.buildInd = buildInd;
	}

	public Boolean getReplaceInd() {
		return replaceInd;
	}

	public void setReplaceInd(Boolean replaceInd) {
		this.replaceInd = replaceInd;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}
	
	public Execution getExecution() {
		return execution;
	}

	public void setExecution(Execution execution) {
		this.execution = execution;
	}
	
	public void setModulePath(String modulePath) {
		this.modulePath = modulePath;
		this.setupHandlers(modulePath);
	}
	
	public void setupHandlers(String modulePath) {
		String modulePathWithoutProjectPath = modulePath.replace(XMLUtil.getInstance().getDefaults().getProjectPath() + File.separator, "");
		setFilter(FilterHandler.getInstance().findModulePathFilter(modulePathWithoutProjectPath));
		setExecution(ExecutionHandler.getInstance().findModulePathExecution(modulePathWithoutProjectPath));
	}
	
	public String construct(Boolean includeSysEnv) {
		return CommandUtil.constructCommand(this, includeSysEnv);
	}
	
	public String construct() {
		return construct(true);
	}

	@Override
	public String toString() {
		return "BuildCommand [modulePath=" + modulePath + ", goal=" + goal + ", buildInd=" + buildInd + ", replaceInd="
				+ replaceInd + ", filter=" + filter + ", execution=" + execution + "]";
	}

}

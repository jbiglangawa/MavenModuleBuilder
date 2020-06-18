/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.util;

import java.util.List;

import com.mmb.models.execution.Execution;

public class ExecutionHandler {
	private Execution execution;
	private static ExecutionHandler executionHandler;
	
	public static synchronized ExecutionHandler getInstance() {
		if(executionHandler == null) {
			executionHandler = new ExecutionHandler();
		}
		
		return executionHandler;
	}
	
	public ExecutionHandler() {}
	
	public void handleChangeModulePath(String modulePath) {
		Execution execution = findModulePathExecution(modulePath);
		if(execution == null) {
			return;
		}
		
		this.execution = execution;
	}
	
	public Execution findModulePathExecution(String modulePath) {
		List<Execution> executionList = XMLUtil.getInstance().getExecutionList();
		Execution result = null;
		for(Execution execution : executionList) {
			if(!Util.isNullOrEmpty(execution.getModulePath().getName()) && execution.getModulePath().getName().equalsIgnoreCase(modulePath)) {
				result = execution;
			}
		}
		return result;
	}
	
	public Execution getExecution() {
		return execution;
	}

	public void setExecution(Execution execution) {
		this.execution = execution;
	}
	
	
}

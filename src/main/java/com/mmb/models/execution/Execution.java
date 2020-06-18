/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.models.execution;

import com.mmb.models.ModulePath;

public class Execution {
	private ModulePath modulePath;
	private BeforeCommand beforeCommand;
	private AfterCommand afterCommand;
	
	public Execution() {
		this.modulePath = new ModulePath("");
		this.beforeCommand = new BeforeCommand("");
		this.afterCommand = new AfterCommand("");
	}
	
	public Execution(ModulePath modulePath) {
		this.modulePath = modulePath;
		this.beforeCommand = new BeforeCommand("");
		this.afterCommand = new AfterCommand("");
	}
	
	public Execution(ModulePath modulePath, BeforeCommand beforeComamnd, AfterCommand afterCommand) {
		this.modulePath = modulePath;
		this.beforeCommand = beforeComamnd;
		this.afterCommand = afterCommand;
	}
	
	public ModulePath getModulePath() {
		return modulePath;
	}
	
	public void setModulePath(ModulePath modulePath) {
		this.modulePath = modulePath;
	}
	
	public BeforeCommand getBeforeCommand() {
		return beforeCommand;
	}

	public void setBeforeCommand(BeforeCommand beforeCommand) {
		this.beforeCommand = beforeCommand;
	}

	public AfterCommand getAfterCommand() {
		return afterCommand;
	}
	
	public void setAfterCommand(AfterCommand afterCommand) {
		this.afterCommand = afterCommand;
	}
	
	
	/*
	 * String setters
	 */
	public void setModulePath(String modulePath) {
		this.modulePath = new ModulePath(modulePath);
	}
	
	public void setBeforeCommand(String beforeCommand) {
		this.beforeCommand = new BeforeCommand(beforeCommand);
	}
	
	public void setAfterCommand(String afterCommand) {
		this.afterCommand = new AfterCommand(afterCommand);
	}
	

	@Override
	public String toString() {
		return "Execution [modulePath=" + modulePath + ", beforeComamnd=" + beforeCommand + ", afterCommand="
				+ afterCommand + "]";
	}
}

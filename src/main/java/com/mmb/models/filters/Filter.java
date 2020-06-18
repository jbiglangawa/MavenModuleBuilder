/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.models.filters;

import com.mmb.models.ModulePath;
import com.mmb.util.Util;

public class Filter {
	private ModulePath modulePath;
	private Action action;
	private Goal goal;
	private Opts opts;
	private Boolean isClean;
	private Boolean isOnline;
	
	public Filter() {
		modulePath = new ModulePath("");
		action = new Action("");
		goal = new Goal("");
		opts = new Opts("");
		this.isClean = false;
		this.isOnline = false;
	}
	
	public Filter(String modulePath, String action, String goal, String opts, Boolean isClean, Boolean isOnline) {
		super();
		this.modulePath = new ModulePath(modulePath);
		this.action = new Action(action);
		this.goal = new Goal(goal);
		this.opts = new Opts(opts);
		this.isClean = isClean;
		this.isOnline = isOnline;
	}
	
	public ModulePath getModulePath() {
		return modulePath;
	}

	public void setModulePath(ModulePath modulePath) {
		this.modulePath = modulePath;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}
	
	public Goal getGoal() {
		return goal;
	}

	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	public Opts getOpts() {
		return opts;
	}

	public void setOpts(Opts opts) {
		this.opts = opts;
	}

	public Boolean getIsClean() {
		return isClean;
	}

	public void setIsClean(Boolean isClean) {
		this.isClean = isClean;
	}

	public Boolean getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(Boolean isOnline) {
		this.isOnline = isOnline;
	}
	
	/*
	 * String setters
	 */
	public void setModulePath(String modulePath) {
		this.modulePath = new ModulePath(modulePath);
	}
	
	public void setAction(String action) {
		this.action = new Action(action);
	}
	
	public void setGoal(String goal) {
		this.goal = new Goal(goal);
	}
	
	public void setOpts(String opts) {
		this.opts = new Opts(opts);
	}
	
	public void setIsClean(String isClean) {
		if(!Util.isNullOrEmpty(isClean)) {
			this.isClean = Boolean.parseBoolean(isClean);
		}
	}

	@Override
	public String toString() {
		return "Filter [modulePath=" + modulePath + ", action=" + action + ", goal=" + goal + ", opts=" + opts
				+ ", isClean=" + isClean + ", isOnline=" + isOnline + "]";
	}
}

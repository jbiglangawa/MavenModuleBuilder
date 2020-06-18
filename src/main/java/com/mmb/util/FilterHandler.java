/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.util;

import java.util.List;

import com.mmb.models.filters.Filter;
import com.mmb.models.filters.Goal;
import com.mmb.util.constants.Constants;
import com.mmb.windows.MainWindow;

public class FilterHandler {
	private MainWindow owner;
	private Filter filter;
	private static FilterHandler filterHandler;
	
	public static synchronized FilterHandler getInstance() {
		if(filterHandler != null) {
			filterHandler = new FilterHandler(null);
		}
		
		return filterHandler;
	} 
	
	public FilterHandler(MainWindow owner) {
		filterHandler = this;
		this.owner = owner;
	}
	
	public void handleChangeModulePath(String modulePath) {
		Filter filter = findModulePathFilter(modulePath);
		
		if(filter == null) {
			reenableAllFields();
			return;
		}
		
		if(!Util.isNullOrEmpty(filter.getAction().getName())) {
			if(filter.getAction().getName().equalsIgnoreCase(Constants.REPLACE)) {
				owner.getBuildActionButton().setEnabled(false);
			}else if(filter.getAction().getName().equalsIgnoreCase(Constants.BUILD)) {
				owner.getReplaceButton().setEnabled(false);
			}
		}

		if(!Util.isNullOrEmpty(filter.getGoal().getName())) {
			Goal goal = filter.getGoal();
			if(goal.getName().equalsIgnoreCase(Constants.INSTALL)) {
				owner.getGoalToggle().setSelected(false);
			}else if(goal.getName().equalsIgnoreCase(Constants.COMPILE)){
				owner.getGoalToggle().setSelected(true);
			}else if(goal.getName().equalsIgnoreCase(Constants.PACKAGE)) {
				owner.getGoalToggle().setEnabled(false);
			}
			
			owner.getGoalToggle().setEnabled(false);
		}
		
		this.filter = filter;
	}
	
	public void refresh() {
		handleChangeModulePath(owner.getModulePathWithoutDirectory());
	}
	
	public void reenableAllFields() {
		owner.getBuildActionButton().setEnabled(true);
		owner.getReplaceButton().setEnabled(true);
		owner.getGoalToggle().setEnabled(true);
	}
	
	public Filter findModulePathFilter(String modulePath) {
		List<Filter> filterList = XMLUtil.getInstance().getFilterList();
		Filter modulePathFilter = null;
		
		if(filterList != null && !Util.isNullOrEmpty(modulePath)) {
			for(Filter filter : filterList) {
				String filterModulePath = filter.getModulePath().getName();
				if(!Util.isNullOrEmpty(filter.getModulePath().getName()) && filterModulePath.equalsIgnoreCase(modulePath)) {
					modulePathFilter = filter;
					break;
				}
			}
		}
		
		return modulePathFilter;
	}

	public MainWindow getOwner() {
		return owner;
	}

	public void setOwner(MainWindow owner) {
		this.owner = owner;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}
	
}

/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.models;

import java.util.List;

import com.mmb.models.target.Target;

public class GeneralDefaultSettings {
	private String rootDirectory;
	private String buildParameters;
	private String mavenOpts;
	private String action;
	
	private Boolean buildInd;
	private Boolean replaceInd;
	
	private List<Target> targetList;

	public String getRootDirectory() {
		return rootDirectory;
	}
	
	public void setRootDirectory(String rootDirectory) {
		this.rootDirectory = rootDirectory;
	}
	
	public String getBuildParameters() {
		return buildParameters;
	}
	
	public void setBuildParameters(String buildParameters) {
		this.buildParameters = buildParameters;
	}
	
	public String getMavenOpts() {
		return mavenOpts;
	}
	
	public void setMavenOpts(String mavenOpts) {
		this.mavenOpts = mavenOpts;
	}

	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
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
	
	public List<Target> getTargetList() {
		return targetList;
	}

	public void setTargetList(List<Target> targetList) {
		this.targetList = targetList;
	}

	@Override
	public String toString() {
		return "GeneralDefaultSettings [rootDirectory=" + rootDirectory + ", buildParameters=" + buildParameters
				+ ", mavenOpts=" + mavenOpts + ", action=" + action + ", buildInd=" + buildInd + ", replaceInd="
				+ replaceInd + ", targetList=" + targetList + "]";
	}
	
}

/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.models;

import com.mmb.models.filters.Goal;

public class Defaults {
	private String projectPath;
	private String buildParameters;
	private String mavenOpts;
	private Boolean isBuild;
	private Boolean isReplace;
	private Goal goal;
	
	public Defaults() {
		isBuild = false;
		isReplace = false;
		goal = new Goal("");
	}
	
	public Defaults(String projectPath, String buildParameters, String mavenOpts, Boolean isBuild, Boolean isReplace,
			Goal goal) {
		this.projectPath = projectPath;
		this.buildParameters = buildParameters;
		this.mavenOpts = mavenOpts;
		this.isBuild = isBuild;
		this.isReplace = isReplace;
		this.goal = goal;
	}

	public String getProjectPath() {
		return projectPath;
	}
	
	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
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
	
	public Boolean getIsBuild() {
		return isBuild;
	}
	
	public void setIsBuild(Boolean isBuild) {
		this.isBuild = isBuild;
	}
	
	public Boolean getIsReplace() {
		return isReplace;
	}
	
	public void setIsReplace(Boolean isReplace) {
		this.isReplace = isReplace;
	}
	
	public Goal getGoal() {
		return goal;
	}
	
	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Defaults [projectPath=");
		builder.append(projectPath);
		builder.append(", buildParameters=");
		builder.append(buildParameters);
		builder.append(", mavenOpts=");
		builder.append(mavenOpts);
		builder.append(", isBuild=");
		builder.append(isBuild);
		builder.append(", isReplace=");
		builder.append(isReplace);
		builder.append(", goal=");
		builder.append(goal);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}

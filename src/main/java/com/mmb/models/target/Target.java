/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.models.target;

import com.mmb.models.Directory;

public class Target {
	private String label;
	private Directory directory;
	
	public Target() {
		this.directory = new Directory("");
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public Directory getDirectory() {
		return directory;
	}
	
	public void setDirectory(Directory directory) {
		this.directory = directory;
	}
	
	@Override
	public String toString() {
		return "Target [label=" + label + ", directory=" + directory + "]";
	}
	
}

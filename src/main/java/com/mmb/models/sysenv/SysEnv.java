/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.models.sysenv;

import com.mmb.models.Directory;

public class SysEnv {
	private String label;
	private Directory directory;
	
	public SysEnv() {
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
		return "SysEnv [label=" + label + ", directory=" + directory + "]";
	}
}

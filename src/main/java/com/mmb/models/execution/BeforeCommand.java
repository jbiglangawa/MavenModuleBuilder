/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.models.execution;

import java.util.List;

public class BeforeCommand {
	private String name;
	
	public BeforeCommand(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
	public String[] getCommand() {
		return name.split("\n");
	}
	
	public void setCommand(List<String> commandList) {
		this.name = "";
		for(String command : commandList) {
			this.name += command + "\n";
		}
		
	}
}

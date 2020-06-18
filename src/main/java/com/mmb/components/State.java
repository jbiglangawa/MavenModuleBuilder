/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.components;

public enum State {
	NO_ACTION("NA"), BUILDING("B"), SUCCESS("S"), ERROR("E");
	
	private String currentState;
	
	private State() {}
	
	State(String currentState) {
		this.currentState = currentState;
	}
	
	@Override
	public String toString() {
		String string = null;
		switch(currentState) {
		case "NA":
			string = "NO_ACTION";
			break;
		case "B":
			string = "BUILDING";
			break;
		case "S":
			string = "SUCCESS";
			break;
		case "E":
			string = "ERROR";
			break;
		}
		
		return string;
	}
}

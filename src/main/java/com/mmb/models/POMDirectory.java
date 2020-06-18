/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

public class POMDirectory {
	private POMDirectory parent;
	private List<POMDirectory> children = new ArrayList<POMDirectory>();
	private String directory;
	
	public POMDirectory() {
		
	}
	
	public POMDirectory(DefaultMutableTreeNode node) {
		this.directory = node.toString();
		
		for(int i = 0; i < node.getChildCount(); i++) {
			this.children.add( new POMDirectory((DefaultMutableTreeNode) node.getChildAt(i)) );
		}
	}
	
	public POMDirectory(String directory) {
		String[] directoryArray = directory.split("\\\\");
		this.directory = directoryArray[0];
		directoryArray = Arrays.copyOfRange(directoryArray, 1, directoryArray.length);
		addChild(directoryArray);
	}
	
	public POMDirectory(POMDirectory parent, String[] directoryArray) {
		this.directory = directoryArray[0];
		
		if(directoryArray.length > 0) {
			directoryArray = Arrays.copyOfRange(directoryArray, 1, directoryArray.length);
			addChild(directoryArray);
		}
	}
	
	/*
	 * Getters and setters for properties
	 */
	public POMDirectory getParent() {
		return parent;
	}

	public void setParent(POMDirectory parent) {
		this.parent = parent;
	}

	public List<POMDirectory> getChildren() {
		return children;
	}

	public void setChildren(List<POMDirectory> children) {
		this.children = children;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}
	
	
	/*
	 * Utiility methods
	 */
	
	/**
	 * Scans all children directory to search for the directory
	 * @param directory
	 * @return
	 */
	public POMDirectory getChild(String directory) {
		POMDirectory pomDirectoryChild = null;
		for(POMDirectory child : children) {
			if(child.getDirectory().equalsIgnoreCase(directory)) {
				pomDirectoryChild = child;
				break;
			}
		}
		return pomDirectoryChild;
	}
	
	/**
	 * Check if the child is existing in this current pomDirectory
	 * @param directory
	 * @return
	 */
	public Boolean isChildExisting(String directory) {
		for(POMDirectory child : children) {
			if(child.getDirectory().equalsIgnoreCase(directory)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Add the child to current pomDirectory
	 * @param childDir
	 */
	public void addChild(String[] childDir) {
		if(childDir.length > 0) {
			this.children.add(new POMDirectory(this, childDir));
		}
	}
	
	public void addChild(POMDirectory pomDirectory) {
		this.children.add(pomDirectory);
	}
	
	/**
	 * Add the directory to root pomDirectory. Will automatically
	 * adjust all the required modules
	 * @param directory
	 */
	public void addDirectory(String directory) {
		String[] directoryArray = directory.split("\\\\");
		if(this.directory == null) {
			this.directory = directoryArray[0];
		}
		
		POMDirectory parentPOM = this;
		for(int i = 1; i < directoryArray.length; i++) {
			String currentDirectory = directoryArray[i];
			if(parentPOM.isChildExisting(currentDirectory)) {
				parentPOM = parentPOM.getChild(currentDirectory);
			}else {
				directoryArray = Arrays.copyOfRange(directoryArray, i, directoryArray.length);
				parentPOM.addChild(directoryArray);
				break;
			}
		}
	}
	
	public DefaultMutableTreeNode convert() {
		DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(this.directory);
		
		for(POMDirectory child : children) {
			treeNode.add(child.convert());
		}
		
		return treeNode;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("POMDirectory [");
		builder.append("children=");
		builder.append(children);
		builder.append(", directory=");
		builder.append(directory);
		builder.append("]");
		return builder.toString();
	}
	
}
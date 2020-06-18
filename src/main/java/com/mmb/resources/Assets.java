/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.resources;

import java.awt.Image;

import javax.swing.ImageIcon;

import com.mmb.util.ImageUtil;


/**
 * Will contain resources that needs to load on launch of the application
 * @author 19036-JMEB
 *
 */
public class Assets {
	private Image hammer;
	private Image success;
	private Image error;
	private Image stop;
	
	private ImageIcon loading;
	
	public Assets() {
		// Images
		hammer = ImageUtil.getImage(Resources.HAMMER);
		success = ImageUtil.getImage(Resources.SUCCESS);
		error = ImageUtil.getImage(Resources.ERROR);
		stop = ImageUtil.getImage(Resources.STOP);
		
		// Image icons
		loading = ImageUtil.getImageIcon(Resources.LOADING);
	}

	public Image getHammer() {
		return hammer;
	}

	public Image getSuccess() {
		return success;
	}

	public Image getError() {
		return error;
	}

	public Image getStop() {
		return stop;
	}

	public ImageIcon getLoading() {
		return loading;
	}
	
}

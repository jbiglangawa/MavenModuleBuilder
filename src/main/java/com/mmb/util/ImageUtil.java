/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.util;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import com.mmb.resources.Resources;

public class ImageUtil {
	private static Logger logger = Logger.getLogger(ImageUtil.class);
	
	public static final String MAVEN = "maven-100x35.png";
	
	public static ImageIcon getImageIcon(String path) {
		return new ImageIcon(ImageUtil.class.getResource(Resources.ROOT_IMG_DIR + path));
	}
	
	public static Image getImage(String path) {
		Image image = null;
		try {
			image = ImageIO.read(ImageUtil.class.getResource(Resources.ROOT_IMG_DIR + path));
		} catch (IOException e) {
			logger.error("Issue with reading image with path: " + path + ": " + e.getMessage(), e);
		}
		
		return image;
	}
}

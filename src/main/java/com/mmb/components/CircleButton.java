/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * 
 * Will generate a circle button <br>
 * 
 * Source: <br>
 * https://happycoding.io/examples/java/swing/circle-button <br>
 * 
 */
public class CircleButton extends JButton{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 168952562935835131L;
	private boolean mouseOver = false;
	private boolean mousePressed = false;
	private Color pressColor;
	private Color originalColor;
	private Color hoverColor;
	
	private Image image;
	private ImageIcon imageIcon;

	public CircleButton(String text, Color originalColor){
		super(text);
		setOpaque(false);
		setFocusPainted(false);
		setBorderPainted(false);
		
		MouseAdapter mouseListener = new MouseAdapter(){
			
			@Override
			public void mousePressed(MouseEvent me){
				if(contains(me.getX(), me.getY())){
					mousePressed = true;
					repaint();
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent me){
				mousePressed = false;
				repaint();
			}
			
			@Override
			public void mouseExited(MouseEvent me){
				mouseOver = false;
				mousePressed = false;
				repaint();
			}
			
			@Override
			public void mouseMoved(MouseEvent me){
				mouseOver = contains(me.getX(), me.getY());
				repaint();
			}	
		};
		
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
		
		this.originalColor = originalColor;
	}

	
	@Override
	public Dimension getPreferredSize(){
		FontMetrics metrics = getGraphics().getFontMetrics(getFont());
		int minDiameter = 10 + Math.max(metrics.stringWidth(getText()), metrics.getHeight());
		return new Dimension(minDiameter, minDiameter);
	}
	
	@Override
	public boolean contains(int x, int y){
		int radius = getDiameter()/2;
		return Point2D.distance(x, y, getWidth()/2, getHeight()/2) < radius;
	}
	
	@Override
	public void paintComponent(Graphics g){
		int diameter = getDiameter();
		int radius = diameter/2;
		
		Color setColor = null;
		if(mousePressed) {
			setColor = pressColor;
		}else if(mouseOver) {
			setColor = hoverColor;
		}else {
			setColor = originalColor;
		}
		
		// Rendering the smooth circle button
		// Reference is below
		// http://www.java2s.com/Code/Java/2D-Graphics-GUI/FillRectangle2DDoubleandEllipse2DDouble.htm
		Ellipse2D ellipse = new Ellipse2D.Double(new Double(getWidth()/2 - radius), new Double(getHeight()/2 - radius), new Double(diameter), new Double(diameter));
		Graphics2D g2d = (Graphics2D) g.create();
	    g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
	    g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
	    g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
	    
	    g2d.setColor(setColor);
	    g2d.setPaint(setColor);
	    g2d.fill(ellipse);
	    g2d.draw(ellipse);
	    
	    if(image != null) {
			int x = (this.getWidth() - image.getWidth(null)) / 2;
			int y = (this.getHeight() - image.getHeight(null)) / 2;
			g2d.drawImage(image, x, y, this);
		}
	    
	    g2d.dispose();
	}
	
	private int getDiameter(){
		int diameter = Math.min(getWidth(), getHeight());
		return diameter;
	}
	
	public Color getPressColor() {
		return pressColor;
	}

	public void setPressColor(Color pressColor) {
		this.pressColor = pressColor;
	}

	public Color getOriginalColor() {
		return originalColor;
	}

	public void setOriginalColor(Color originalColor) {
		this.originalColor = originalColor;
	}

	public Color getHoverColor() {
		return hoverColor;
	}

	public void setHoverColor(Color hoverColor) {
		this.hoverColor = hoverColor;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public ImageIcon getImageIcon() {
		return imageIcon;
	}


	public void setImageIcon(ImageIcon imageIcon) {
		this.imageIcon = imageIcon;
	}
	
	
	
}

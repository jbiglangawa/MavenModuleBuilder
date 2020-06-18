/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.windows;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.mmb.resources.Resources;
import com.mmb.util.ImageUtil;
import com.mmb.util.constants.ColorConstants;
import com.mmb.util.constants.Constants;
import com.mmb.util.constants.LabelConstants;

public class AboutWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4328958641451390701L;
	private JPanel contentPane;
	
	private int xLoc;
	private int yLoc;

  	/**
	 * Create the frame.
	 */
	public AboutWindow() {
		/*
		 * UI setup
		 */
		setMinimumSize(new Dimension(200, 200));
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 350, 309);

		contentPane = new JPanel();
		contentPane.setBackground(ColorConstants.BLACK_37);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel headerPanel = new JPanel();
		headerPanel.setDoubleBuffered(false);
		headerPanel.setBorder(null);
		headerPanel.setBackground(ColorConstants.BLACK_37);
		
		JButton closeButton = new JButton();
		closeButton.setFocusable(false);
		closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		closeButton.setPressedIcon(ImageUtil.getImageIcon(Resources.CLOSE_PRESS));
		closeButton.setRolloverIcon(ImageUtil.getImageIcon(Resources.CLOSE_ROLLOVER));
		closeButton.setIcon(ImageUtil.getImageIcon(Resources.CLOSE));
		closeButton.setForeground(ColorConstants.BLACK_37);
		closeButton.setBackground(ColorConstants.BLACK_37);
		closeButton.setBorder(null);
		closeButton.setContentAreaFilled(false);
		closeButton.setBorderPainted(false);
		closeButton.setFocusPainted(false);
		
		JLabel aboutHeaderLabel = new JLabel(LabelConstants.ABOUT);
		aboutHeaderLabel.setFont(new Font(Constants.FONT_DIALOG, Font.BOLD, 14));
		aboutHeaderLabel.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		
		JLabel aboutBodyLabel = new JLabel(LabelConstants.ABOUT_BODY);
		aboutBodyLabel.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 12));
		aboutBodyLabel.setVerticalTextPosition(SwingConstants.TOP);
		aboutBodyLabel.setVerticalAlignment(SwingConstants.TOP);
		aboutBodyLabel.setHorizontalAlignment(SwingConstants.LEFT);
		aboutBodyLabel.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);

		
		
		/*
		 * Event handlers setup
		 */
		headerPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				xLoc = e.getX();
				yLoc = e.getY();
			}
		});
		
		headerPanel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				int x = arg0.getXOnScreen() - xLoc;
				int y = arg0.getYOnScreen() - yLoc;
				setLocation(x, y);
			}
		});
		
		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				dispose();
			}
		});
		
		
		
		/*
		 * Group layout setup
		 */
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(headerPanel, GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(10)
					.addComponent(aboutBodyLabel, GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(headerPanel, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(aboutBodyLabel, GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		GroupLayout gl_headerPanel = new GroupLayout(headerPanel);
		gl_headerPanel.setHorizontalGroup(
			gl_headerPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_headerPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(aboutHeaderLabel)
					.addPreferredGap(ComponentPlacement.RELATED, 251, Short.MAX_VALUE)
					.addComponent(closeButton, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
		);
		gl_headerPanel.setVerticalGroup(
			gl_headerPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_headerPanel.createSequentialGroup()
					.addGroup(gl_headerPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(closeButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_headerPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(aboutHeaderLabel)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		
		headerPanel.setLayout(gl_headerPanel);
		contentPane.setLayout(gl_contentPane);
	}
}

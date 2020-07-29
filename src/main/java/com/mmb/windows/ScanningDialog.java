/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.windows;

import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.mmb.util.constants.ColorConstants;
import com.mmb.util.constants.Constants;

public class ScanningDialog extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4328958641451390701L;
	private JPanel contentPane;

  	/**
	 * Create the frame.
	 */
	public ScanningDialog(SettingsWindow settingsWindow) {
		/*
		 * UI setup
		 */
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		int x = settingsWindow.getX() + ((settingsWindow.getWidth()/2) - 65);
		int y = settingsWindow.getY() + ((settingsWindow.getHeight()/2) - 31);
		setBounds(x, y, 130, 62);

		contentPane = new JPanel();
		contentPane.setBackground(ColorConstants.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel scanningLabel = new JLabel("Scanning...");
		scanningLabel.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 20));
		scanningLabel.setVerticalTextPosition(SwingConstants.TOP);
		scanningLabel.setVerticalAlignment(SwingConstants.TOP);
		scanningLabel.setHorizontalAlignment(SwingConstants.LEFT);
		scanningLabel.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		
		
		/*
		 * Group layout setup
		 */
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(scanningLabel, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(44, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(scanningLabel, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(43, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
}

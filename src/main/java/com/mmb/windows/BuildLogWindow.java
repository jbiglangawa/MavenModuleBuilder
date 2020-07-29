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
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import com.mmb.components.ComponentResizer;
import com.mmb.resources.Resources;
import com.mmb.util.ImageUtil;
import com.mmb.util.constants.ColorConstants;
import com.mmb.util.constants.Constants;
import com.mmb.util.constants.LabelConstants;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BuildLogWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4568398275089396448L;

	private static Logger buildLogger = Logger.getLogger("build");
	private JPanel contentPane;
	
	private int xLoc;
	private int yLoc;
	private TextArea logTextArea;
	
	/**
	 * Create the frame.
	 */
	public BuildLogWindow() {
		/*
		 * UI setup
		 */
		ComponentResizer cr = new ComponentResizer();
		cr.registerComponent(this);
		cr.setSnapSize(new Dimension(10, 10));
		
		setMinimumSize(new Dimension(200, 200));
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 500);
		
		contentPane = new JPanel();
		contentPane.setBackground(ColorConstants.BLACK_37);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JButton closeButton = new JButton("");
		closeButton.setBorder(null);
		closeButton.setFocusable(false);
		closeButton.setPressedIcon(ImageUtil.getImageIcon(Resources.CLOSE_PRESS));
		closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		closeButton.setRolloverIcon(ImageUtil.getImageIcon(Resources.CLOSE_ROLLOVER));
		closeButton.setContentAreaFilled(false);
		closeButton.setBorderPainted(false);
		closeButton.setFocusPainted(false);
		closeButton.setForeground(ColorConstants.BLACK_37);
		closeButton.setBackground(ColorConstants.BLACK_37);
		closeButton.setIcon(ImageUtil.getImageIcon(Resources.CLOSE));
		
		JButton clearButton = new JButton(LabelConstants.CLEAR);
		clearButton.setBorder(null);
		clearButton.setBorderPainted(false);
		clearButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		clearButton.setFocusable(false);
		clearButton.setBackground(ColorConstants.HIGHLIGHT);
		clearButton.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		clearButton.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 14));
		
		JPanel txtAreaPanel = new JPanel();
		logTextArea = new TextArea();
		logTextArea.setEditable(false);
		logTextArea.setFont(new Font("Consolas", Font.PLAIN, 12));
		logTextArea.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		logTextArea.setBackground(ColorConstants.BLACK_37);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(ColorConstants.BLACK_37);

		JPanel headerPanel = new JPanel();
		headerPanel.setDoubleBuffered(false);
		headerPanel.setBorder(null);
		headerPanel.setBackground(ColorConstants.BLACK_37);
		
		
		
		/*
		 * Event Handlers setup
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
		
		clearButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				logTextArea.setText("");
			}
		});
		
		
		
		/*
		 * Group layout setup
		 */
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(headerPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(txtAreaPanel, GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
					.addGap(13))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(buttonPanel, GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(headerPanel, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtAreaPanel, GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(buttonPanel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		GroupLayout gl_buttonPanel = new GroupLayout(buttonPanel);
		gl_buttonPanel.setHorizontalGroup(
			gl_buttonPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_buttonPanel.createSequentialGroup()
					.addContainerGap(478, Short.MAX_VALUE)
					.addComponent(clearButton, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_buttonPanel.setVerticalGroup(
			gl_buttonPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_buttonPanel.createSequentialGroup()
					.addGap(0, 0, Short.MAX_VALUE)
					.addComponent(clearButton, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		buttonPanel.setLayout(gl_buttonPanel);
		
		GroupLayout gl_txtAreaPanel = new GroupLayout(txtAreaPanel);
		gl_txtAreaPanel.setHorizontalGroup(
			gl_txtAreaPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(logTextArea, GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
		);
		gl_txtAreaPanel.setVerticalGroup(
			gl_txtAreaPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(logTextArea, GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
		);
		txtAreaPanel.setLayout(gl_txtAreaPanel);
		
		JButton minimizeButton = new JButton("");
		minimizeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setState(Frame.ICONIFIED);
			}
		});
		minimizeButton.setForeground(new Color(37, 37, 37));
		minimizeButton.setFocusable(false);
		minimizeButton.setFocusPainted(false);
		minimizeButton.setContentAreaFilled(false);
		minimizeButton.setBorderPainted(false);
		minimizeButton.setBorder(null);
		minimizeButton.setBackground(new Color(37, 37, 37));
		minimizeButton.setIcon(ImageUtil.getImageIcon(Resources.MINIMIZE));
		
		GroupLayout gl_headerPanel = new GroupLayout(headerPanel);
		gl_headerPanel.setHorizontalGroup(
			gl_headerPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_headerPanel.createSequentialGroup()
					.addContainerGap(516, Short.MAX_VALUE)
					.addComponent(minimizeButton, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(closeButton, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
		);
		gl_headerPanel.setVerticalGroup(
			gl_headerPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_headerPanel.createSequentialGroup()
					.addGroup(gl_headerPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(closeButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
						.addComponent(minimizeButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		headerPanel.setLayout(gl_headerPanel);
		contentPane.setLayout(gl_contentPane);
	}
	
	public Boolean printToLog(Process buildProcess) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(buildProcess.getInputStream()));
		
		while (true) {
			String line = reader.readLine();
			if (line == null) {
				break;
			}else {
				printToLog(line);
			}
		}
		
		return buildProcess.exitValue() == 0;
	}
	
	public void printToLog(String string) {
		buildLogger.debug(string);
		logTextArea.append(string + "\n");
	}
	
	public TextArea getLogTextArea() {
		return logTextArea;
	}
}

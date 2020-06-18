/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.dialog;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import com.mmb.resources.Resources;
import com.mmb.util.ImageUtil;
import com.mmb.util.XMLUtil;
import com.mmb.util.constants.ColorConstants;
import com.mmb.util.constants.Constants;
import com.mmb.util.constants.LabelConstants;
import com.mmb.windows.MainWindow;

public class EditLabelDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4328958641451390701L;
	private JPanel contentPane;
	
	private int xLoc;
	private int yLoc;
	private JTextField labelTextField;

  	/**
	 * Create the frame.
	 */
	public EditLabelDialog(String buttonNumber) {
		/*
		 * UI setup
		 */
  		setModalityType(ModalityType.APPLICATION_MODAL);
  		setType(Type.POPUP);
  		setSize(new Dimension(264, 127));
  		setMaximumSize(new Dimension(200, 180));
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 264, 148);

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
		
		JLabel aboutHeaderLabel = new JLabel(LabelConstants.EDIT_LABEL);
		aboutHeaderLabel.setFont(new Font(Constants.FONT_DIALOG, Font.BOLD, 14));
		aboutHeaderLabel.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		
		labelTextField = new JTextField();
		labelTextField.setColumns(10);
		
		JButton saveButton = new JButton(LabelConstants.SAVE);
		saveButton.setBounds(478, 0, 76, 27);
		saveButton.setBorder(null);
		saveButton.setBorderPainted(false);
		saveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		saveButton.setFocusable(false);
		saveButton.setBackground(ColorConstants.HIGHLIGHT);
		saveButton.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		saveButton.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 14));
		
		
		
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
		
		saveButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(buttonNumber == "1") {
					XMLUtil.getInstance().getShortcut1().setLabel(getNewLabel());
				}else if(buttonNumber == "2") {
					XMLUtil.getInstance().getShortcut2().setLabel(getNewLabel());
				}else if(buttonNumber == "3") {
					XMLUtil.getInstance().getShortcut3().setLabel(getNewLabel());
				}
				
				MainWindow.getInstance().refreshShortcutLabels();
				dispose();
			}
		});
		
		
		
		/*
		 * Group layout setup
		 */
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addComponent(headerPanel, GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(10)
					.addComponent(labelTextField, GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(163, Short.MAX_VALUE)
					.addComponent(saveButton, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(headerPanel, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(labelTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(saveButton, GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
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
	
	public String getNewLabel() {
		return this.labelTextField.getText();
	}
}

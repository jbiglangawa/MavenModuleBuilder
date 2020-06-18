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
import java.awt.Rectangle;
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
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.mmb.components.ComponentResizer;
import com.mmb.models.execution.AfterCommandCellEditor;
import com.mmb.models.execution.BeforeCommandCellEditor;
import com.mmb.resources.Resources;
import com.mmb.util.ImageUtil;
import com.mmb.util.Util;
import com.mmb.util.constants.ColorConstants;
import com.mmb.util.constants.Constants;
import com.mmb.util.constants.LabelConstants;

public class CommandEditorWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7885527126005607396L;
	
	private int xLoc;
	private int yLoc;
	private String mode;
	
	private JPanel contentPane;
	private JButton saveButton;
	private RSyntaxTextArea editingTextArea;
	
	
	/**
	 * Create the frame.
	 * @throws UnsupportedLookAndFeelException 
	 */
	public CommandEditorWindow() {
		/*
		 * UI setup
		 */
		ComponentResizer cr = new ComponentResizer();
		cr.registerComponent(this);
		cr.setSnapSize(new Dimension(10, 10));
		cr.setMaximumSize(new Dimension(1800,600));
		
		setMinimumSize(new Dimension(200, 200));
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 400, 350);
		
		contentPane = new JPanel();
		contentPane.setBackground(ColorConstants.BLACK_37);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel headerPanel = new JPanel();
		headerPanel.setDoubleBuffered(false);
		headerPanel.setBorder(null);
		headerPanel.setBackground(ColorConstants.BLACK_37);
		
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
		
		JLabel lblCommandEditor = new JLabel(LabelConstants.COMMAND_EDITOR);
		lblCommandEditor.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		
		editingTextArea = new RSyntaxTextArea(20, 60);
		editingTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_WINDOWS_BATCH);
		editingTextArea.setCodeFoldingEnabled(true);
		
		RTextScrollPane editorScrollPanel = new RTextScrollPane(editingTextArea);
		editorScrollPanel.setBounds(new Rectangle(30, 60, 300, 300));
		contentPane.add(editorScrollPanel);
		
		saveButton = new JButton("Save");
		saveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		saveButton.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		saveButton.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 14));
		saveButton.setFocusable(false);
		saveButton.setBorderPainted(false);
		saveButton.setBorder(new EmptyBorder(5, 5, 5, 5));
		saveButton.setBackground(ColorConstants.HIGHLIGHT);
		
		
		
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
		GroupLayout gl_headerPanel = new GroupLayout(headerPanel);
		gl_headerPanel.setHorizontalGroup(
			gl_headerPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_headerPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblCommandEditor)
					.addPreferredGap(ComponentPlacement.RELATED, 427, Short.MAX_VALUE)
					.addComponent(closeButton, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
		);
		gl_headerPanel.setVerticalGroup(
			gl_headerPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_headerPanel.createSequentialGroup()
					.addGroup(gl_headerPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(closeButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCommandEditor, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		headerPanel.setLayout(gl_headerPanel);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(headerPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(saveButton, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(30)
							.addComponent(editorScrollPanel, GroupLayout.DEFAULT_SIZE, 535, Short.MAX_VALUE)))
					.addGap(25))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(headerPanel, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(editorScrollPanel, GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
					.addGap(19)
					.addComponent(saveButton, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	public void setupForCellEditor(AfterCommandCellEditor afterCommandCellEditor) {
		saveButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(mode.equalsIgnoreCase("AfterCommand")) {
					String textArea = editingTextArea.getText();
					if(!Util.isNullOrEmpty(textArea)) {
						afterCommandCellEditor.setAfterCommand(textArea);
						editingTextArea.setText(null);
						setVisible(false);
					}
				}
			}
		});
	}
	
	public void setupForCellEditor(BeforeCommandCellEditor beforeCommandCellEditor) {
		saveButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(mode.equalsIgnoreCase("BeforeCommand")) {
					String textArea = editingTextArea.getText();
					if(!Util.isNullOrEmpty(textArea)) {
						beforeCommandCellEditor.setBeforeCommand(textArea);
						editingTextArea.setText(null);
						setVisible(false);
					}
				}
			}
		});
	}
	
	public void popup(String cellValue, String mode) {
		this.mode = mode;
		setVisible(true);
		editingTextArea.setText(cellValue);
	}
	
	public JButton getSaveButton() {
		return saveButton;
	}
}

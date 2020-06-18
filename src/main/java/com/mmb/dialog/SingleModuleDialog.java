/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import com.mmb.models.ModulePathCellEditor;
import com.mmb.util.Util;
import com.mmb.util.XMLUtil;
import com.mmb.util.constants.ColorConstants;
import com.mmb.util.constants.Constants;
import com.mmb.windows.MainWindow;
import com.mmb.windows.MultiModuleWindow;
import com.mmb.windows.SettingsWindow;

public class SingleModuleDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3862691519948711259L;
	private final JPanel contentPanel = new JPanel();
	private JTree moduleTree;
	
	private WindowFocusListener windowFocusListener;
	private MouseAdapter mouseAdapter;
	private SettingsWindow settingsWindow;

	/**
	 * Create the dialog.
	 * @throws UnsupportedLookAndFeelException 
	 */
	public SingleModuleDialog(MainWindow owner) {
		/*
		 * UI setup
		 */
		super(owner);
		setSize(new Dimension(300, 300));
		setUndecorated(true);
		setLocation(new Point(300, 0));
		setResizable(false);
		setBounds(100, 100, 450, 300);
		
		contentPanel.setBackground(new Color(70, 70, 70));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane();
		
		moduleTree = new JTree(XMLUtil.getInstance().getModuleDirectory().convert());
		scrollPane.setViewportView(moduleTree);
		moduleTree.setAutoscrolls(true);
		moduleTree.setToggleClickCount(1);
		moduleTree.setRowHeight(20);
		moduleTree.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 13));
		moduleTree.setBackground(ColorConstants.BLACK_37);
		moduleTree.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		
		
		
		/*
		 * Event handler setup
		 */
		moduleTree.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// Event for ESC Key
				if(e.getKeyCode() == 27) {
					dispose();
				}
			}
			
		});
		
		windowFocusListener = new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent e) {
				int x = owner.getContentPanel().getX() + owner.getX() + 39;
				int y = owner.getContentPanel().getY() + owner.getY() + owner.getModuleTextField().getHeight() + 74;
				setBounds(x, y, owner.getModuleTextField().getWidth(), 300);
				setMaximumSize(new Dimension(owner.getModuleTextField().getWidth(), 2147483647));
			}
			public void windowLostFocus(WindowEvent e) {
				dispose();
			}
		};

		mouseAdapter = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getClickCount() == 2 && moduleTree.getSelectionPath() != null) {
					owner.setModulePath(Util.getSelectedModule(moduleTree.getSelectionPath()));
					dispose();
				}
			}
		};
		
		addWindowFocusListener(windowFocusListener);
		moduleTree.addMouseListener(mouseAdapter);
		
		
		
		/**
		 * Group layout setup
		 */
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.TRAILING)
				.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
		);
		contentPanel.setLayout(gl_contentPanel);
	}
	
	public void setupForCellEditor(ModulePathCellEditor modulePathCellEditor, SettingsWindow settingsWindow) {
		this.settingsWindow = settingsWindow;
		
		moduleTree.removeMouseListener(mouseAdapter);
		removeWindowFocusListener(windowFocusListener);
		addWindowFocusListener(new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent e) {
				
			}
			public void windowLostFocus(WindowEvent e) {
				modulePathCellEditor.cancelCellEditing();
				dispose();
			}
		});
		
		moduleTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getClickCount() == 2 && moduleTree.getSelectionPath() != null) {
					modulePathCellEditor.setModulePath(Util.getSelectedModule(moduleTree.getSelectionPath()));
					dispose();
				}
			}
		});
		
		moduleTree.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// Event for ESC Key
				if(e.getKeyCode() == 27) {
					dispose();
				}
			}
			
		});
	}
	
	public void setupForMultiModuleWindow(MultiModuleWindow multiModuleWindow) {
		moduleTree.removeMouseListener(mouseAdapter);
		removeWindowFocusListener(windowFocusListener);
		
		addWindowFocusListener(new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent e) {
				int x = multiModuleWindow.getX() + 30;
				int y = multiModuleWindow.getY() + multiModuleWindow.getModulePathTextField().getHeight() + 74;
				setBounds(x, y, multiModuleWindow.getModulePathTextField().getWidth(), 300);
				setMaximumSize(new Dimension(multiModuleWindow.getModulePathTextField().getWidth(), 2147483647));
			}
			
			public void windowLostFocus(WindowEvent e) {
				dispose();
			}
		});
		
		moduleTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getClickCount() == 2 && moduleTree.getSelectionPath() != null) {
					multiModuleWindow.getModulePathTextField().setText(Util.getSelectedModule(moduleTree.getSelectionPath()));
					dispose();
				}
			}
		});
		
		moduleTree.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// Event for ESC Key
				if(e.getKeyCode() == 27) {
					dispose();
				}
			}
			
		});
		
	}
	
	public void setVisible(Boolean visibility, JTable jTable, int row) {
		int x = settingsWindow.getX() + 42;
		int y = settingsWindow.getY() + (jTable.getRowHeight() * (row + 1)) + 140;
		setBounds(x,y, 300, 250);
		setMaximumSize(new Dimension(300, 2147483647));
		setVisible(visibility);
	}
	
	public JTree getModuleTree() {
		return moduleTree;
	}
}

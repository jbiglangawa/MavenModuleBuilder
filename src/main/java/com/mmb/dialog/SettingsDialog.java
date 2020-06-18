/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.mmb.util.constants.ColorConstants;
import com.mmb.util.constants.Constants;
import com.mmb.windows.MainWindow;

public class SettingsDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2362676441958138567L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public SettingsDialog(MainWindow owner) {
		/*
		 * UI setup
		 */
		super(owner);
		setResizable(false);
		setUndecorated(true);
		setBounds(100, 100, 150, 120);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setBackground(new Color(115,115,115));
		contentPanel.setLayout(null);
		JButton buildLogButton = new JButton("  Build Log");
		buildLogButton.setFocusable(false);
		buildLogButton.setRolloverEnabled(false);
		buildLogButton.setHorizontalAlignment(SwingConstants.LEFT);
		buildLogButton.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 12));
		buildLogButton.setHorizontalTextPosition(SwingConstants.LEFT);
		buildLogButton.setBorderPainted(false);
		buildLogButton.setBorder(null);
		buildLogButton.setContentAreaFilled(false);
		buildLogButton.setBackground(new Color(115,115,115));
		buildLogButton.setBounds(5, 10, 135, 25);
		buildLogButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		buildLogButton.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		contentPanel.add(buildLogButton);
		
		JButton buildHistoryButton = new JButton("  Build History");
		buildHistoryButton.setFocusable(false);
		buildHistoryButton.setRolloverEnabled(false);
		buildHistoryButton.setHorizontalAlignment(SwingConstants.LEFT);
		buildHistoryButton.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 12));
		buildHistoryButton.setHorizontalTextPosition(SwingConstants.LEFT);
		buildHistoryButton.setContentAreaFilled(false);
		buildHistoryButton.setBorder(null);
		buildHistoryButton.setBorderPainted(false);
		buildHistoryButton.setBackground(new Color(115,115,115));
		buildHistoryButton.setBounds(5, 35, 135, 25);
		buildHistoryButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		buildHistoryButton.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		contentPanel.add(buildHistoryButton);
		
		JButton settingsButton = new JButton("  Settings");
		settingsButton.setFocusable(false);
		settingsButton.setRolloverEnabled(false);
		settingsButton.setMargin(new Insets(2, 20, 2, 14));
		settingsButton.setHorizontalAlignment(SwingConstants.LEFT);
		settingsButton.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 12));
		settingsButton.setHorizontalTextPosition(SwingConstants.LEFT);
		settingsButton.setContentAreaFilled(false);
		settingsButton.setBorderPainted(false);
		settingsButton.setBorder(null);
		settingsButton.setBackground(new Color(115,115,115));
		settingsButton.setBounds(5, 60, 135, 25);
		settingsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		settingsButton.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		contentPanel.add(settingsButton);
		
		JButton aboutButton = new JButton("  About");
		aboutButton.setFocusable(false);
		aboutButton.setRolloverEnabled(false);
		aboutButton.setHorizontalAlignment(SwingConstants.LEFT);
		aboutButton.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 12));
		aboutButton.setHorizontalTextPosition(SwingConstants.LEFT);
		aboutButton.setBorder(null);
		aboutButton.setBorderPainted(false);
		aboutButton.setContentAreaFilled(false);
		aboutButton.setForeground(ColorConstants.BLACK_37);
		aboutButton.setBackground(new Color(115,115,115));
		aboutButton.setBounds(5, 85, 135, 25);
		aboutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		aboutButton.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		contentPanel.add(aboutButton);
		
		
		/*
		 * Event handler setup
		 */
		buildLogButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				owner.getBuildLogWindow().setVisible(true);
			}
		});
		
		buildHistoryButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				owner.getBuildHistoryWindow().setVisible(true);
			}
		});
		
		settingsButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				owner.getSettingsWindow().setVisible(true);
			}
		});
		
		
		aboutButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				owner.getAboutWindow().setVisible(true);
			}
		});
		
		addWindowFocusListener(new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent e) {
				int x = owner.getContentPanel().getX() + owner.getContentPanel().getWidth() + owner.getX() - 175;
				int y = owner.getContentPanel().getY() + owner.getY() + owner.getSettings().getHeight();
				setBounds(x, y, 150, 120);
			}
			
			public void windowLostFocus(WindowEvent e) {
				dispose();
			}
		});
		
	}
}

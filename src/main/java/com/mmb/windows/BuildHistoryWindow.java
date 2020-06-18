/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.windows;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.apache.log4j.Logger;

import com.mmb.components.ComponentResizer;
import com.mmb.components.JTable;
import com.mmb.models.DefaultCellRenderer;
import com.mmb.resources.Resources;
import com.mmb.util.ImageUtil;
import com.mmb.util.constants.ColorConstants;
import com.mmb.util.constants.Constants;
import com.mmb.util.constants.LabelConstants;

public class BuildHistoryWindow extends JFrame {

	private static Logger activityLogger = Logger.getLogger("activity");
	private static final long serialVersionUID = 4148412282326716630L;
	
	private int xLoc;
	private int yLoc;
	
	private JTable historyTable;
	private JPanel contentPane;
	private DefaultTableModel historyTableModel;

	/**
	 * Create the frame.
	 */
	public BuildHistoryWindow() {
		/*
		 * UI setup
		 */
		ComponentResizer cr = new ComponentResizer();
		cr.registerComponent(this);
		cr.setSnapSize(new Dimension(10, 10));
		cr.setMaximumSize(new Dimension(1800,600));
		
		setMinimumSize(new Dimension(500, 300));
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 703, 479);
		
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
		closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		closeButton.setContentAreaFilled(false);
		closeButton.setBorderPainted(false);
		closeButton.setFocusPainted(false);
		closeButton.setForeground(ColorConstants.BLACK_37);
		closeButton.setBackground(ColorConstants.BLACK_37);
		closeButton.setRolloverIcon(ImageUtil.getImageIcon(Resources.CLOSE_ROLLOVER));
		closeButton.setPressedIcon(ImageUtil.getImageIcon(Resources.CLOSE_PRESS));
		closeButton.setIcon(ImageUtil.getImageIcon(Resources.CLOSE));
		
		JPanel historyPanel = new JPanel();
		historyPanel.setBorder(null);
		historyPanel.setBackground(Color.BLACK);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setBackground(ColorConstants.BLACK_37);
		
		historyTableModel = new DefaultTableModel();
		historyTableModel.addColumn(LabelConstants.HEADER_MODULE);
		historyTableModel.addColumn(LabelConstants.HEADER_BUILD_TIME);
		historyTableModel.addColumn(LabelConstants.HEADER_DURATION);
		historyTableModel.addColumn(LabelConstants.HEADER_RESULT);
		
		historyTable = new JTable(historyTableModel, ColorConstants.TABLE_ROW, ColorConstants.BLACK_37);
		historyTable.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 10));
		historyTable.getTableHeader().setPreferredSize(new Dimension(50, 50));
		historyTable.setOpaque(true);
		historyTable.setBorder(null);
		historyTable.setShowHorizontalLines(false);
		historyTable.setShowVerticalLines(false);
		historyTable.setShowGrid(false);
		historyTable.setEnabled(false);
		historyTable.setRowSelectionAllowed(false);
		historyTable.setRowMargin(0);
		historyTable.setRowHeight(30);
		historyTable.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		historyTable.setFillsViewportHeight(true);
		historyTable.setBackground(ColorConstants.BLACK_37);
		scrollPane.setViewportView(historyTable);
		
		TableColumnModel historyTableColumnModel = historyTable.getColumnModel();
		historyTableColumnModel.getColumn(0).setPreferredWidth(500);
		historyTable.setDefaultRenderer(String.class, new DefaultCellRenderer());
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
		
		
		
		/*
		 * Group layout setup
		 */
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(headerPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(32)
					.addComponent(historyPanel, GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE)
					.addGap(32))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(headerPanel, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(historyPanel, GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
					.addGap(33))
		);
		
		GroupLayout gl_historyPanel = new GroupLayout(historyPanel);
		gl_historyPanel.setHorizontalGroup(
			gl_historyPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)
		);
		gl_historyPanel.setVerticalGroup(
			gl_historyPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
		);
		
		GroupLayout gl_headerPanel = new GroupLayout(headerPanel);
		gl_headerPanel.setHorizontalGroup(
			gl_headerPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_headerPanel.createSequentialGroup()
					.addContainerGap(556, Short.MAX_VALUE)
					.addComponent(closeButton, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
		);
		gl_headerPanel.setVerticalGroup(
			gl_headerPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_headerPanel.createSequentialGroup()
					.addComponent(closeButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(17, Short.MAX_VALUE))
		);
		
		historyPanel.setLayout(gl_historyPanel);
		headerPanel.setLayout(gl_headerPanel);
		contentPane.setLayout(gl_contentPane);
		
	}
	
	public void insertNewBuildHistory(String modulePath, Long duration, Boolean isSuccess) {
		Double totalDuration = new Double(duration);
		Double totalDurationSec = totalDuration / 1000;
		String totalDurationString = "";
		
		if(totalDuration > 60) {
			totalDurationString = totalDurationSec/60 + "m";
		}else {
			totalDurationString = totalDurationSec + "s";
		}
		
		activityLogger.debug("Build us done executing: modulePath" + modulePath + ", totalDurationString = " + totalDurationString + ", isSuccess = " + isSuccess);
		historyTableModel.insertRow(historyTableModel.getRowCount(), 
			new Object[] {
					modulePath, 
					new SimpleDateFormat("hh:mm").format(new Date()), 
					totalDurationString,
					(isSuccess) ? Constants.SUCCESS : Constants.FAILED
			}
		);
		
	}
}

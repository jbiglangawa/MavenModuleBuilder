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
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.DropMode;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import com.mmb.components.ComponentResizer;
import com.mmb.components.JTable;
import com.mmb.components.RoundTextField;
import com.mmb.components.TableRowTransferHandler;
import com.mmb.dialog.SingleModuleDialog;
import com.mmb.models.MultiModuleModel;
import com.mmb.resources.Resources;
import com.mmb.util.ImageUtil;
import com.mmb.util.Util;
import com.mmb.util.constants.ColorConstants;
import com.mmb.util.constants.Constants;
import com.mmb.util.constants.LabelConstants;

public class MultiModuleWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2771153077150775800L;

	private JPanel contentPane;
	
	private int xLoc;
	private int yLoc;
	private RoundTextField modulePathTextField;
	private MultiModuleModel multiModuleModel;
	private JTable multiModuleTable;
	
	private static MultiModuleWindow multiModuleWindow;
	
	public static synchronized MultiModuleWindow getInstance() {
		if(multiModuleWindow == null) {
			multiModuleWindow = new MultiModuleWindow();
		}
		
		return multiModuleWindow;
	}
	
	/**
	 * Create the frame.
	 */
	public MultiModuleWindow() {
		/*
		 * UI setup
		 */
		multiModuleWindow = this;
		
		ComponentResizer cr = new ComponentResizer();
		cr.registerComponent(this);
		cr.setSnapSize(new Dimension(10, 10));
		cr.setMaximumSize(new Dimension(1800,600));
		
		setMinimumSize(new Dimension(400, 400));
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 474, 421);
		
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
		
		modulePathTextField = new RoundTextField(30);
		modulePathTextField.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 12));
		modulePathTextField.setEditable(false);
		modulePathTextField.setColumns(10);
		
		JButton addButton = new JButton("Add");
		addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		addButton.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		addButton.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 14));
		addButton.setFocusable(false);
		addButton.setBorderPainted(false);
		addButton.setBorder(new EmptyBorder(5, 5, 5, 5));
		addButton.setBackground(ColorConstants.HIGHLIGHT);
		
		multiModuleModel = new MultiModuleModel();
		multiModuleTable = new JTable(multiModuleModel, ColorConstants.BLACK_60, ColorConstants.BLACK_37);
		multiModuleTable.setBorder(new EmptyBorder(5, 5, 5, 5));
		multiModuleTable.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 12));
		multiModuleTable.setFillsViewportHeight(true);
		multiModuleTable.setDropMode(DropMode.INSERT_ROWS);
		multiModuleTable.setDragEnabled(true);
		multiModuleTable.setBackground(ColorConstants.BLACK_37);
		multiModuleTable.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		multiModuleTable.setSelectionBackground(ColorConstants.LIGHT_GRAY);
		multiModuleTable.setSelectionForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		multiModuleTable.setDragEnabled(true);
		multiModuleTable.setDropMode(DropMode.INSERT_ROWS);
		multiModuleTable.setTransferHandler(new TableRowTransferHandler(multiModuleTable));
		
		TableColumnModel colModel = multiModuleTable.getColumnModel();
		colModel.getColumn(0).setWidth(20);
		colModel.getColumn(1).setPreferredWidth(500);
		
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem deleteItem = new JMenuItem(LabelConstants.DELETE);
        popupMenu.add(deleteItem);
        multiModuleTable.setComponentPopupMenu(popupMenu);
		
		// Aligning table cells : https://stackoverflow.com/questions/2408541/align-the-values-of-the-cells-in-jtable/2410843
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(SwingConstants.LEFT);
		colModel.getColumn(1).setCellRenderer(rightRenderer);
		
		JLabel multiModuleBuilderLabel = new JLabel("Multi Module Builder");
		multiModuleBuilderLabel.setBackground(ColorConstants.DEFAULT_TEXT_COLOR);
		multiModuleBuilderLabel.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		
		
		
		/*
		 * Event handler setup
		 */
		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				dispose();
			}
		});
		
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
		
		modulePathTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				SingleModuleDialog singleModuleWindow = new SingleModuleDialog(null);
				singleModuleWindow.setupForMultiModuleWindow(MultiModuleWindow.this);
				singleModuleWindow.setVisible(true);
			}
		});
		
		addButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(!Util.isNullOrEmpty(modulePathTextField.getText())) {
					multiModuleModel.addNewRow(modulePathTextField.getText());
				}
			}
		});
		
		popupMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = multiModuleTable.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), multiModuleTable));
                        if (rowAtPoint > -1) {
                            multiModuleTable.setRowSelectionInterval(rowAtPoint, rowAtPoint);
                            deleteItem.setEnabled(true);
                        }else {
                        	deleteItem.setEnabled(false);
                        }
                    }
                });
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
            
            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
        
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	int selectedRow = multiModuleTable.getSelectedRow();
            	if(selectedRow >= 0) {
            		multiModuleModel.deleteRowAt(selectedRow);
            	}
            }
        });
		
		
		
		/*
		 * Group layout setup
		 */
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(headerPanel, GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(24)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(multiModuleTable, GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(modulePathTextField, GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(addButton, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)))
					.addGap(24))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(headerPanel, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
					.addGap(32)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(addButton, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(modulePathTextField, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(multiModuleTable, GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
					.addGap(65))
		);
		
		
		
		GroupLayout gl_headerPanel = new GroupLayout(headerPanel);
		gl_headerPanel.setHorizontalGroup(
			gl_headerPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_headerPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(multiModuleBuilderLabel)
					.addPreferredGap(ComponentPlacement.RELATED, 500, Short.MAX_VALUE)
					.addComponent(closeButton, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
		);
		gl_headerPanel.setVerticalGroup(
			gl_headerPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_headerPanel.createSequentialGroup()
					.addGroup(gl_headerPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(closeButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_headerPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(multiModuleBuilderLabel)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		headerPanel.setLayout(gl_headerPanel);
		contentPane.setLayout(gl_contentPane);
	}

	public RoundTextField getModulePathTextField() {
		return modulePathTextField;
	}

	public JPanel getContentPane() {
		return contentPane;
	}
	
	public MultiModuleModel getMultiModuleModel() {
		return multiModuleModel;
	}

	@Override
	public void dispose() {
		MainWindow.getInstance().getModuleToggleButton().setSelected(false);
		super.dispose();
	}
}

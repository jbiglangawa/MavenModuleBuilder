/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.models;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractCellEditor;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import com.mmb.util.Util;

public class DirectoryCellEditor extends AbstractCellEditor implements TableCellEditor {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -2391407063570550905L;
	
	private JTextField directoryTextField;
	
	@Override
	public Object getCellEditorValue() {
		return new Directory(directoryTextField.getText());
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		directoryTextField = new JTextField();
		directoryTextField.setText( ((Directory) value).toString() );
		directoryTextField.setToolTipText( ((Directory) value).toString() );
		directoryTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getClickCount() == 2) {
					JFileChooser fileChooser = Util.getWindowsJFileChooser(((Directory) value).toString());
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					fileChooser.showDialog(directoryTextField, "Select");
					if(fileChooser.getSelectedFile() != null) {
						String filePath = fileChooser.getSelectedFile().getAbsolutePath();
						directoryTextField.setText(filePath);
					}
				}
			}
		});
		return directoryTextField;
	}

}
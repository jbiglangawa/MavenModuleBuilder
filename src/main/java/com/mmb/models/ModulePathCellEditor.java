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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import com.mmb.dialog.SingleModuleDialog;
import com.mmb.windows.SettingsWindow;

public class ModulePathCellEditor extends AbstractCellEditor implements TableCellEditor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2391407063570550905L;
	
	private SettingsWindow settingsWindow;
	private SingleModuleDialog singleModuleDialog;
	
	private ModulePath modulePath;
	
	public ModulePathCellEditor() {
		
	}
	
	public ModulePathCellEditor(SettingsWindow settingsWindow) {
		this.settingsWindow = settingsWindow;
		singleModuleDialog = new SingleModuleDialog(null);
		singleModuleDialog.setupForCellEditor(this, settingsWindow);
	}
	
	@Override
	public Object getCellEditorValue() {
		return this.modulePath;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		if(value instanceof ModulePath) {
			this.modulePath = (ModulePath) value;
		}
		
		JTextField modulePathTextField = new JTextField();
		modulePathTextField.setText( ((ModulePath) value).toString() );
		modulePathTextField.setToolTipText( ((ModulePath) value).toString() );
		modulePathTextField.setBackground(table.getBackground());
		modulePathTextField.setEditable(false);
		modulePathTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				singleModuleDialog.setVisible(true, table, row);
			}
		});
		
		return modulePathTextField;
	}

	public ModulePath getModulePath() {
		return modulePath;
	}

	public void setModulePath(String modulePath) {
		this.modulePath.setName(modulePath);
	}

	public SettingsWindow getSettingsWindow() {
		return settingsWindow;
	}

	public void setSettingsWindow(SettingsWindow settingsWindow) {
		this.settingsWindow = settingsWindow;
	}
	
	
}
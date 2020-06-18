/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.models.execution;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import com.mmb.windows.CommandEditorWindow;

public class BeforeCommandCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4977598861242655454L;
	private BeforeCommand beforeCommand;
	private CommandEditorWindow commandEditorWindow;
	
	public BeforeCommandCellEditor(CommandEditorWindow commandEditorWindow) {
		this.commandEditorWindow = commandEditorWindow;
		commandEditorWindow.setupForCellEditor(this);
	}
	
	@Override
	public Object getCellEditorValue() {
		return this.beforeCommand;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		if(value instanceof BeforeCommand) {
			this.beforeCommand = (BeforeCommand) value;
		}
		
		JTextField beforeCommandTextField = new JTextField();
		beforeCommandTextField.setText( ((BeforeCommand) value).toString() );
		beforeCommandTextField.setToolTipText( ((BeforeCommand) value).toString() );
		beforeCommandTextField.setEditable(false);
		beforeCommandTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				commandEditorWindow.popup( ((BeforeCommand) value).toString() , "BeforeCommand");
			}
		});
		
		return beforeCommandTextField;
	}

	public void setBeforeCommand(String command) {
		this.beforeCommand.setName(command);
		stopCellEditing();
	}

	public CommandEditorWindow getCommandEditorWindow() {
		return commandEditorWindow;
	}

	public void setCommandEditorWindow(CommandEditorWindow commandEditorWindow) {
		this.commandEditorWindow = commandEditorWindow;
	}
}
/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.models.filters;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.mmb.util.constants.Constants;

public class ActionCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2391407063570550905L;
	private Action action;
	private List<Action> listAction;

	public ActionCellEditor() {
		listAction = new ArrayList<Action>();
		listAction.add(new Action(""));
		listAction.add(new Action(Constants.REPLACE));
		listAction.add(new Action(Constants.BUILD));
	}

	@Override
	public Object getCellEditorValue() {
		return this.action;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		if (value instanceof Action) {
			this.action = (Action) value;
		}

		JComboBox<Action> comboAction = new JComboBox<Action>();
		
		for (Action action : listAction) {
			comboAction.addItem(action);
		}

		comboAction.setSelectedItem(action);
		comboAction.addActionListener(this);

		if (isSelected) {
			comboAction.setBackground(table.getSelectionBackground());
		} else {
			comboAction.setBackground(table.getSelectionForeground());
		}

		return comboAction;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		@SuppressWarnings("unchecked")
		JComboBox<Action> comboCountry = (JComboBox<Action>) event.getSource();
		this.action = (Action) comboCountry.getSelectedItem();
	}
}
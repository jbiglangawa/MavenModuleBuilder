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

public class GoalCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2391407063570550905L;
	private Goal goal;
	private List<Goal> listGoal;

	public GoalCellEditor() {
		listGoal = new ArrayList<Goal>();
		listGoal.add(new Goal(Constants.INSTALL));
		listGoal.add(new Goal(Constants.COMPILE));
		listGoal.add(new Goal(Constants.PACKAGE));
	}

	@Override
	public Object getCellEditorValue() {
		return this.goal;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		if (value instanceof Action) {
			this.goal = (Goal) value;
		}

		JComboBox<Goal> comboGoal = new JComboBox<Goal>();
		
		for (Goal action : listGoal) {
			comboGoal.addItem(action);
		}

		comboGoal.setSelectedItem(goal);
		comboGoal.addActionListener(this);

		if (isSelected) {
			comboGoal.setBackground(table.getSelectionBackground());
		} else {
			comboGoal.setBackground(table.getSelectionForeground());
		}

		return comboGoal;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		@SuppressWarnings("unchecked")
		JComboBox<Goal> comboGoal = (JComboBox<Goal>) event.getSource();
		this.goal = (Goal) comboGoal.getSelectedItem();
	}
}
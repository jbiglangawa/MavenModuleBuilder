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

@Deprecated
public class OptsCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2391407063570550905L;
	private Opts Opts;
	private List<Opts> listOpts;

	public OptsCellEditor() {
		listOpts = new ArrayList<Opts>();
	}

	@Override
	public Object getCellEditorValue() {
		return this.Opts;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		if (value instanceof Action) {
			this.Opts = (Opts) value;
		}

		JComboBox<Opts> comboOpts = new JComboBox<Opts>();
		
		for (Opts action : listOpts) {
			comboOpts.addItem(action);
		}

		comboOpts.setSelectedItem(Opts);
		comboOpts.addActionListener(this);

		if (isSelected) {
			comboOpts.setBackground(table.getSelectionBackground());
		} else {
			comboOpts.setBackground(table.getSelectionForeground());
		}

		return comboOpts;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		@SuppressWarnings("unchecked")
		JComboBox<Opts> comboOpts = (JComboBox<Opts>) event.getSource();
		this.Opts = (Opts) comboOpts.getSelectedItem();
	}
}
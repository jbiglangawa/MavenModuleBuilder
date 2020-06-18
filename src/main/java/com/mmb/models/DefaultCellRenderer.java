/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.models;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.mmb.models.execution.AfterCommand;
import com.mmb.models.execution.BeforeCommand;
import com.mmb.models.filters.Action;
import com.mmb.models.filters.Opts;
import com.mmb.models.filters.Goal;

public class DefaultCellRenderer extends DefaultTableCellRenderer {
    
   /**
	 * 
	 */
	private static final long serialVersionUID = 8026770995966077859L;

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
       if (value instanceof Action) {
           Action action = (Action) value;
           setText(action.getName());
       }else if(value instanceof Goal) {
    	   Goal goal = (Goal) value;
    	   setText(goal.getName());
       }else if(value instanceof Opts) {
    	   Opts opts = (Opts) value;
    	   setText(opts.getName());
       }else if(value instanceof ModulePath) {
    	   ModulePath modulePath = (ModulePath) value;
    	   setText(modulePath.getName());
       }else if(value instanceof AfterCommand) {
    	   AfterCommand afterCommand = (AfterCommand) value;
    	   setText(afterCommand.getName());
       }else if(value instanceof BeforeCommand) {
    	   BeforeCommand beforeCommand = (BeforeCommand) value;
    	   setText(beforeCommand.getName());
       }else if(value instanceof Directory) {
    	   Directory directory = (Directory) value;
    	   setText(directory.getName());
       }
       setHorizontalAlignment(JLabel.CENTER);
       setFont(table.getFont());
       setToolTipText(getText());
       
       if (isSelected) {
           setBackground(table.getSelectionBackground());
       } else {
           setBackground(table.getBackground());
       }
        
       return this;
   }
    
}
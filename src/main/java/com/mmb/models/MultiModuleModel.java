/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.mmb.components.Reorderable;

public class MultiModuleModel extends AbstractTableModel implements Reorderable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6556305191670208154L;
	private List<String> modulePathList = new ArrayList<String>();
	
	private final String[] columnNames = new String[] {
	    	"#", "Module Path"
    };
	
    @SuppressWarnings("rawtypes")
	private final Class[] columnClass = new Class[] {
        String.class, String.class
    };
     
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
 
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnClass[columnIndex];
    }
 
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
 
    @Override
    public int getRowCount() {
        return modulePathList.size();
    }
 
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
    	String row = modulePathList.get(rowIndex);
        
        if(0 == columnIndex) {
            return rowIndex + 1;
        }else if(1 == columnIndex) {
        	return row;
        }else {
        	return null;
        }
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    	
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
    }
    
    public void addNewRow(String modulePath) {
    	int indexChange = getRowCount();
    	modulePathList.add(modulePath);
    	super.fireTableRowsInserted(indexChange, indexChange);
    }
    
    /**
     * Delete an object via index
     * 
     * @param row
     */
    public void deleteRowAt(int rowIndex) {
    	modulePathList.remove(rowIndex);
    	super.fireTableDataChanged();
    }

	@Override
	public void reorder(int fromIndex, int toIndex) {
		if(toIndex == modulePathList.size()) {
			toIndex--;
		}
		String from = modulePathList.get(fromIndex);
		String to = modulePathList.get(toIndex);
		
		modulePathList.set(toIndex, from);
		modulePathList.set(fromIndex, to);
	}

	public List<String> getModulePathList() {
		return modulePathList;
	}
	
}
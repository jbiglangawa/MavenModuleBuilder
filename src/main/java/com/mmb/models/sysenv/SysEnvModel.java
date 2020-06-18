/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.models.sysenv;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.mmb.components.Reorderable;
import com.mmb.models.Directory;
import com.mmb.util.XMLUtil;

public class SysEnvModel extends AbstractTableModel implements Reorderable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6156020873390454802L;
	private List<SysEnv> sysEnvList = new ArrayList<SysEnv>();
	
	private final String[] columnNames = new String[] {
			"", "Label", "Directory"
	};
	
	@SuppressWarnings("rawtypes")
	private final Class[] columnClass = new Class[] {
			String.class, String.class, Directory.class
	};
	
	public SysEnvModel() {
		this.sysEnvList = XMLUtil.getInstance().getSysEnvList();
	}
	
	
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
        return sysEnvList.size();
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if(columnIndex > 0) {
        	return true;
        }else {
        	return false;
        }
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
    	SysEnv row = sysEnvList.get(rowIndex);
        if(0 == columnIndex) {
        	return rowIndex + 1;
        }else if(1 == columnIndex) {
            return row.getLabel();
        }else if(2 == columnIndex) {
            return row.getDirectory();
        }else {
        	return null;
        }
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    	SysEnv row = sysEnvList.get(rowIndex);
        if(1 == columnIndex) {
            row.setLabel((String) aValue);
        }else if(2 == columnIndex) {
            row.setDirectory((Directory) aValue);
        }
    }
    
    public void addNewRow() {
    	int indexChange = getRowCount();
    	this.sysEnvList.add(new SysEnv());
    	this.fireTableRowsInserted(indexChange, indexChange);
    }
    
    public void deleteRowAt(int row) {
    	this.sysEnvList.remove(row);
    	this.fireTableRowsDeleted(row, row);
    }

	public List<SysEnv> getSysEnvList() {
		return sysEnvList;
	}

	public void setSysEnvList(List<SysEnv> sysEnvList) {
		this.sysEnvList = sysEnvList;
	}


	@Override
	public void reorder(int fromIndex, int toIndex) {
		if(toIndex == sysEnvList.size()) {
			toIndex--;
		}
		SysEnv from = sysEnvList.get(fromIndex);
		SysEnv to = sysEnvList.get(toIndex);
		
		sysEnvList.set(toIndex, from);
		sysEnvList.set(fromIndex, to);
	}


}
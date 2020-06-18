/*
 * Copyright (c) 20John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.models.execution;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.mmb.models.ModulePath;
import com.mmb.util.XMLUtil;

public class ExecutionTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6556305191670208154L;
	private List<Execution> executionList = new ArrayList<Execution>();
	
	private final String[] columnNames = new String[] {
	    	"#", "Module Path", "Before Command", "After Command"
    };
	
	public ExecutionTableModel() {
		this.executionList = XMLUtil.getInstance().getExecutionList();
	}
	
    @SuppressWarnings("rawtypes")
	private final Class[] columnClass = new Class[] {
        String.class, ModulePath.class, BeforeCommand.class, AfterCommand.class
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
        return executionList.size();
    }
 
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
    	Execution row = executionList.get(rowIndex);
        
        if(0 == columnIndex) {
            return rowIndex + 1;
        }else if(1 == columnIndex) {
        	return row.getModulePath();
        }else if(2 == columnIndex) {
        	return row.getBeforeCommand();
        }else if(3 == columnIndex) {
        	return row.getAfterCommand();
        }else {
        	return null;
        }
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    	Execution row = executionList.get(rowIndex);
        
        if(1 == columnIndex) {
            row.setModulePath((ModulePath) aValue);
        }else if(2 == columnIndex) {
            row.setBeforeCommand((BeforeCommand) aValue);
        }else if(3 == columnIndex) {
            row.setAfterCommand((AfterCommand) aValue);
        }
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
    	if(columnIndex > 0) {
    		return true;
    	}else {
    		return false;
    	}
    }
    
    public void addNewRow() {
    	int indexChange = getRowCount();
    	executionList.add(new Execution());
    	super.fireTableRowsInserted(indexChange, indexChange);
    }
    
    /**
     * Delete an object via index
     * 
     * @param rowIndex index for the table
     */
    public void deleteRowAt(int rowIndex) {
    	executionList.remove(rowIndex);
    	super.fireTableDataChanged();
    }
    
    public void loadXMLData() {
    	
    }
    
    public void saveToXML() {
    	XMLUtil.getInstance().setExecutionList(executionList);
    }
}

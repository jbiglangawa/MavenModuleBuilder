/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.models.filters;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.mmb.models.ModulePath;
import com.mmb.util.XMLUtil;

/**
 * Kudos to the master from the site below for sharing this superb example of using JTable
 * with editable function.
 * https://www.codejava.net/java-se/swing/editable-jtable-example
 * 
 * @author 19036-JMEB
 *
 */
public class FilterTableModel extends AbstractTableModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8930664304921811152L;
	private List<Filter> filterList = new ArrayList<Filter>();
    
    private final String[] columnNames = new String[] {
    	"#", "Module Path", "Action", "Goal", "Maven Opts", "Clean"
    };
    
    @SuppressWarnings("rawtypes")
	private final Class[] columnClass = new Class[] {
        String.class, ModulePath.class, Action.class, Goal.class, String.class, Boolean.class
    };
    
    public FilterTableModel() {
    	loadXMLData();
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
        return filterList.size();
    }
 
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Filter row = filterList.get(rowIndex);
        if(0 == columnIndex) {
        	return rowIndex + 1;
        }else if(1 == columnIndex) {
            return row.getModulePath();
        }else if(2 == columnIndex) {
            return row.getAction();
        }else if(3 == columnIndex) {
            return row.getGoal();
        }else if(4 == columnIndex) {
            return row.getOpts().getName();
        }else if(5 == columnIndex){
        	return row.getIsClean();
        }else {
        	return null;
        }
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    	Filter row = filterList.get(rowIndex);
        
        if(1 == columnIndex) {
            row.setModulePath((ModulePath) aValue);
        }else if(2 == columnIndex) {
            row.setAction((Action) aValue);
        }else if(3 == columnIndex) {
            row.setGoal((Goal) aValue);
        }else if(4 == columnIndex) {
            row.setOpts(new Opts((String) aValue));
        }else if(5 == columnIndex){
        	row.setIsClean((Boolean) aValue);
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
    
    /**
     * Get the filter object at the given rowindex
     * 
     * @param rowIndex
     * @return
     */
    public Filter getRow(int rowIndex) {
    	return filterList.get(rowIndex);
    }
    
    /**
     * Add filter object to the table
     * 
     * @param filter
     */
    public void addNewRow() {
    	int indexChange = getRowCount();
    	filterList.add(new Filter());
    	super.fireTableRowsInserted(indexChange, indexChange);
    }
    
    /**
     * Delete an object via index
     * 
     * @param row
     */
    public void deleteRowAt(int rowIndex) {
    	filterList.remove(rowIndex);
    	super.fireTableDataChanged();
    }
    
    /**
     * Save the filters to an XML file
     * 
     */
    public void saveData() {
    	saveToXML();
    }
    
    public void saveToXML() {
    	XMLUtil.getInstance().setFilterList(filterList);
    }
    
    public void loadXMLData() {
    	List<Filter> filterList = XMLUtil.getInstance().getFilterList();
    	if(filterList != null) {
    		this.filterList = filterList;
    	}
    }

}

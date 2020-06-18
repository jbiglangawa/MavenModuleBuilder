/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/*
 * Kudos to this guy at this blog for the alternate row background
 * https://blog.marcnuri.com/jtable-alternate-row-background/
 */
public class JTable extends javax.swing.JTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 118078393694689360L;
	
	private Color backgroundColor;
	private Color alternateColor;
	
	public JTable(TableModel dm, Color backgroundColor, Color alternateColor) {
		super(dm);
		setupRowColor(backgroundColor, alternateColor);
	}

	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
        Component returnComp = super.prepareRenderer(renderer, row, column);
        Color alternateColor = this.alternateColor;
        Color backgroundColor = this.backgroundColor;
        if (!returnComp.getBackground().equals(getSelectionBackground())){
            Color bg = (row % 2 == 0 ? alternateColor : backgroundColor);
            returnComp .setBackground(bg);
            bg = null;
        }
        return returnComp;
    };
    
    public void setupRowColor(Color backgroundColor, Color alternateColor) {
    	this.backgroundColor = backgroundColor;
    	this.alternateColor = alternateColor;
    }
    
    public String getToolTipText(MouseEvent e) {
    	Point p = e.getPoint();
        int rowIndex = rowAtPoint(p);
        int colIndex = columnAtPoint(p);

        String tipString = null;
        Object tip = getValueAt(rowIndex, colIndex);
        if(tip != null) {
        	tipString = tip.toString();
        }
        
        return tipString;
    }
}

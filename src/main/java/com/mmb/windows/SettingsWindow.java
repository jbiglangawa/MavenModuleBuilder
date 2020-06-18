/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.windows;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.DropMode;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.mmb.components.ComponentResizer;
import com.mmb.components.JTable;
import com.mmb.components.TableRowTransferHandler;
import com.mmb.models.DefaultCellRenderer;
import com.mmb.models.Defaults;
import com.mmb.models.Directory;
import com.mmb.models.DirectoryCellEditor;
import com.mmb.models.ModulePath;
import com.mmb.models.ModulePathCellEditor;
import com.mmb.models.POMDirectory;
import com.mmb.models.execution.AfterCommand;
import com.mmb.models.execution.AfterCommandCellEditor;
import com.mmb.models.execution.BeforeCommand;
import com.mmb.models.execution.BeforeCommandCellEditor;
import com.mmb.models.execution.ExecutionTableModel;
import com.mmb.models.filters.Action;
import com.mmb.models.filters.ActionCellEditor;
import com.mmb.models.filters.FilterTableModel;
import com.mmb.models.filters.Goal;
import com.mmb.models.filters.GoalCellEditor;
import com.mmb.models.sysenv.SysEnv;
import com.mmb.models.sysenv.SysEnvModel;
import com.mmb.models.target.Target;
import com.mmb.models.target.TargetModel;
import com.mmb.resources.Resources;
import com.mmb.util.ImageUtil;
import com.mmb.util.POMScanner;
import com.mmb.util.Util;
import com.mmb.util.XMLUtil;
import com.mmb.util.constants.ColorConstants;
import com.mmb.util.constants.Constants;
import com.mmb.util.constants.LabelConstants;
import com.mmb.util.constants.TooltipsConstants;

public class SettingsWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 746272140631234725L;
	
	private int xLoc;
	private int yLoc;
	
	private JPanel contentPane;
	private JTable filterTable;
	private JTable executionTable;
	private JTable targetTable;
	private JTable sysEnvTable;
	private FilterTableModel filterModel;
	private ExecutionTableModel executionModel;
	private TargetModel targetModel;
	private SysEnvModel sysEnvModel;
	private JTextField projectPathTextField;
	private JTextField buildParamsTextField;
	private JTextField mavenOptsTextField;
	private JTree moduleDirectoryTree;
	private JCheckBox buildCheckbox;
	private JCheckBox replaceCheckBox;
	private JComboBox<Goal> goalComboBox;
	
	private CommandEditorWindow commandEditorWindow;
	
	/**
	 * Create the frame.
	 */
	public SettingsWindow(MainWindow owner) {
		/*
		 * UI setup
		 */
		ComponentResizer cr = new ComponentResizer();
		cr.registerComponent(this);
		cr.setSnapSize(new Dimension(10, 10));
		
		setMinimumSize(new Dimension(200, 200));
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 642, 455);

		contentPane = new JPanel();
		contentPane.setBackground(ColorConstants.BLACK_37);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel headerPanel = new JPanel();
		headerPanel.setDoubleBuffered(false);
		headerPanel.setBorder(null);
		headerPanel.setBackground(ColorConstants.BLACK_37);
		
		JButton closeButton = new JButton();
		closeButton.setBorder(null);
		closeButton.setFocusable(false);
		closeButton.setPressedIcon(ImageUtil.getImageIcon(Resources.CLOSE_PRESS));
		closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		closeButton.setRolloverIcon(ImageUtil.getImageIcon(Resources.CLOSE_ROLLOVER));
		closeButton.setContentAreaFilled(false);
		closeButton.setBorderPainted(false);
		closeButton.setFocusPainted(false);
		closeButton.setForeground(ColorConstants.BLACK_37);
		closeButton.setBackground(ColorConstants.BLACK_37);
		closeButton.setIcon(ImageUtil.getImageIcon(Resources.CLOSE));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(ColorConstants.BLACK_60);
		
		
		/*
		 * General Tab components
		 */
		JPanel generalTab = new JPanel();
		generalTab.setBackground(ColorConstants.BLACK_37);
		tabbedPane.addTab(LabelConstants.GENERAL, null, generalTab, TooltipsConstants.GENERAL_TAB);
		
		JScrollPane generalScrollPanel = new JScrollPane();
		generalScrollPanel.setAutoscrolls(true);
		generalScrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		generalScrollPanel.setBackground(ColorConstants.BLACK_37);
		generalScrollPanel.getVerticalScrollBar().setUnitIncrement(16);
		
		JButton generalSaveButton = new JButton(LabelConstants.SAVE);
		generalSaveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		generalSaveButton.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		generalSaveButton.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 14));
		generalSaveButton.setFocusable(false);
		generalSaveButton.setBorderPainted(false);
		generalSaveButton.setBorder(new EmptyBorder(5, 5, 5, 5));
		generalSaveButton.setBackground(ColorConstants.HIGHLIGHT);
		
		JButton generalApplyButton = new JButton("Apply");
		generalApplyButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		generalApplyButton.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		generalApplyButton.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 14));
		generalApplyButton.setFocusable(false);
		generalApplyButton.setBorderPainted(false);
		generalApplyButton.setBorder(new EmptyBorder(5, 5, 5, 5));
		generalApplyButton.setBackground(ColorConstants.HIGHLIGHT);
		
		JPanel generalWrapper = new JPanel();
		generalWrapper.setBackground(ColorConstants.BLACK_37);
		generalScrollPanel.setViewportView(generalWrapper);
		
		JPanel defaultsPanel = new JPanel();
		defaultsPanel.setBorder(new TitledBorder(new LineBorder(ColorConstants.BLACK_37), LabelConstants.DEFAULTS, TitledBorder.LEADING, TitledBorder.TOP, null, ColorConstants.LIGHT_GRAY));
		defaultsPanel.setBackground(ColorConstants.BLACK_60);
		
		JPanel targetWrapper = new JPanel();
		targetWrapper.setBorder(new TitledBorder(new LineBorder(ColorConstants.BLACK_37), LabelConstants.TARGET_DIR, TitledBorder.LEADING, TitledBorder.TOP, null, ColorConstants.LIGHT_GRAY));
		targetWrapper.setBackground(ColorConstants.BLACK_60);
		
		JPanel directoryWrapper = new JPanel();
		directoryWrapper.setBorder(new TitledBorder(new LineBorder(ColorConstants.BLACK_37), LabelConstants.MODULE_DIR, TitledBorder.LEADING, TitledBorder.TOP, null, ColorConstants.LIGHT_GRAY));
		directoryWrapper.setBackground(ColorConstants.BLACK_60);
		
		JPanel sysEnvWrapper = new JPanel();
		sysEnvWrapper.setBorder(new TitledBorder(new LineBorder(ColorConstants.BLACK_37), LabelConstants.SYSENV_VAR, TitledBorder.LEADING, TitledBorder.TOP, null, new Color(100, 100, 100)));
		sysEnvWrapper.setBackground(new Color(60, 60, 60));

		JScrollPane sysEnvScrollPanel = new JScrollPane();
		sysEnvScrollPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		sysEnvScrollPanel.setBackground(new Color(60, 60, 60));
		
		sysEnvModel = new SysEnvModel();
		sysEnvTable = new JTable(sysEnvModel, ColorConstants.BLACK_60, ColorConstants.BLACK_37);
		sysEnvTable.setSelectionForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		sysEnvTable.setSelectionBackground(SystemColor.windowBorder);
		sysEnvTable.setRowHeight(25);
		sysEnvTable.setOpaque(true);
		sysEnvTable.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		sysEnvTable.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 10));
		sysEnvTable.setFillsViewportHeight(true);
		sysEnvTable.setBorder(new EmptyBorder(0, 0, 0, 0));
		sysEnvTable.setBackground(ColorConstants.BLACK_60);
		sysEnvTable.setDefaultRenderer(Directory.class, new DefaultCellRenderer());
		sysEnvTable.setDefaultEditor(Directory.class, new DirectoryCellEditor());
		sysEnvTable.setDragEnabled(true);
		sysEnvTable.setDropMode(DropMode.INSERT_ROWS);
		sysEnvTable.setTransferHandler(new TableRowTransferHandler(sysEnvTable));
		
		TableColumnModel sysEnvColModel = sysEnvTable.getColumnModel();
		sysEnvColModel.getColumn(0).setMaxWidth(20);
		sysEnvColModel.getColumn(1).setPreferredWidth(30);
		sysEnvColModel.getColumn(2).setPreferredWidth(60);
		sysEnvScrollPanel.setViewportView(sysEnvTable);
		
		JPopupMenu popupSysEnvMenu = new JPopupMenu();
		JMenuItem addSysEnvItem = new JMenuItem(LabelConstants.ADD);
		JMenuItem deleteSysEnvItem = new JMenuItem(LabelConstants.DELETE);
		popupSysEnvMenu.add(addSysEnvItem);
		popupSysEnvMenu.add(deleteSysEnvItem);
        sysEnvTable.setComponentPopupMenu(popupSysEnvMenu);
		
		JButton directoryAddButton = new JButton(LabelConstants.PLUS);
		directoryAddButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		directoryAddButton.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		directoryAddButton.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 14));
		directoryAddButton.setFocusable(false);
		directoryAddButton.setBorderPainted(false);
		directoryAddButton.setBorder(new EmptyBorder(5, 5, 5, 5));
		directoryAddButton.setBackground(ColorConstants.HIGHLIGHT);
		
		JButton directoryRemoveButton = new JButton(LabelConstants.MINUS);
		directoryRemoveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		directoryRemoveButton.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		directoryRemoveButton.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 14));
		directoryRemoveButton.setFocusable(false);
		directoryRemoveButton.setBorderPainted(false);
		directoryRemoveButton.setBorder(new EmptyBorder(5, 5, 5, 5));
		directoryRemoveButton.setBackground(ColorConstants.HIGHLIGHT);
		
		JButton scanRootButton = new JButton(LabelConstants.SCAN_ROOT);
		scanRootButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		scanRootButton.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		scanRootButton.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 14));
		scanRootButton.setFocusable(false);
		scanRootButton.setBorderPainted(false);
		scanRootButton.setBorder(new EmptyBorder(5, 5, 5, 5));
		scanRootButton.setBackground(ColorConstants.HIGHLIGHT);
		
		moduleDirectoryTree = new JTree(XMLUtil.getInstance().getModuleDirectory().convert());
		moduleDirectoryTree.setToggleClickCount(1);
		moduleDirectoryTree.setRowHeight(20);
		moduleDirectoryTree.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 13));
		moduleDirectoryTree.setBackground(ColorConstants.BLACK_37);
		moduleDirectoryTree.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		
		TreeCellEditor moduleDirectoryCellEditor = new DefaultCellEditor(new JTextField());
		moduleDirectoryTree.setEditable(true);
		moduleDirectoryTree.setCellEditor(moduleDirectoryCellEditor);
		
		JScrollPane moduleDirectoryScrollPanel = new JScrollPane();
		moduleDirectoryScrollPanel.setViewportView(moduleDirectoryTree);
		
		JScrollPane targetScrollPane = new JScrollPane();
		targetScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		targetScrollPane.setBackground(new Color(60, 60, 60));
		
		targetModel = new TargetModel();
		targetTable = new JTable(targetModel, ColorConstants.BLACK_60, ColorConstants.BLACK_37);
		targetTable.setBorder(new EmptyBorder(0, 0, 0, 0));
		targetTable.setFillsViewportHeight(true);
		targetTable.setOpaque(true);
		targetTable.setBackground(ColorConstants.BLACK_60);
		targetTable.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		targetTable.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 10));
		targetTable.setRowHeight(25);
		targetTable.setSelectionBackground(ColorConstants.LIGHT_GRAY);
		targetTable.setSelectionForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		targetTable.setDefaultRenderer(Directory.class, new DefaultCellRenderer());
		targetTable.setDefaultEditor(Directory.class, new DirectoryCellEditor());
		targetScrollPane.setViewportView(targetTable);
		
		TableColumnModel targetColIndex = targetTable.getColumnModel();
		targetColIndex.getColumn(0).setMaxWidth(20);
		targetColIndex.getColumn(1).setPreferredWidth(30);
		targetColIndex.getColumn(2).setPreferredWidth(60);
		
		JPopupMenu popupTargetMenu = new JPopupMenu();
		JMenuItem addTargetItem = new JMenuItem(LabelConstants.ADD);
		JMenuItem deleteTargetItem = new JMenuItem(LabelConstants.DELETE);
		popupTargetMenu.add(addTargetItem);
		popupTargetMenu.add(deleteTargetItem);
        targetTable.setComponentPopupMenu(popupTargetMenu);
		
		JLabel rootPathLabel = new JLabel(LabelConstants.PROJECT_PATH);
		rootPathLabel.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		rootPathLabel.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 12));
		
		JLabel buildParametersLabel = new JLabel(LabelConstants.BUILD_PARAMS);
		buildParametersLabel.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		buildParametersLabel.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 12));
		
		JLabel mavenOptsLabel = new JLabel(LabelConstants.MAVEN_OPTS);
		mavenOptsLabel.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		mavenOptsLabel.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 12));
		
		buildCheckbox = new JCheckBox(LabelConstants.BUILD);
		buildCheckbox.setBackground(ColorConstants.BLACK_60);
		buildCheckbox.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		buildCheckbox.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 12));
		
		replaceCheckBox = new JCheckBox(LabelConstants.REPLACE);
		replaceCheckBox.setBackground(ColorConstants.BLACK_60);
		replaceCheckBox.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		replaceCheckBox.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 12));
		
		JLabel actionLabel = new JLabel(LabelConstants.ACTION_COLON);
		actionLabel.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		actionLabel.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 12));
		
		JLabel goalLabel = new JLabel(LabelConstants.GOAL_COLON);
		goalLabel.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		goalLabel.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 12));
		
		projectPathTextField = new JTextField();
		projectPathTextField.setColumns(10);
		
		buildParamsTextField = new JTextField();
		buildParamsTextField.setColumns(10);
		
		mavenOptsTextField = new JTextField();
		mavenOptsTextField.setColumns(10);
		
		goalComboBox = new JComboBox<Goal>();
		goalComboBox.addItem(new Goal(""));
		goalComboBox.addItem(new Goal(Constants.INSTALL));
		goalComboBox.addItem(new Goal(Constants.COMPILE));
		
		/*
		 * Filter Tab components
		 */
		JPanel filterTab = new JPanel();
		filterTab.setBackground(ColorConstants.BLACK_37);
		tabbedPane.addTab(LabelConstants.FILTERING, null, filterTab, TooltipsConstants.FILTER_TAB);
		
		JScrollPane filterScrollPane = new JScrollPane();
		filterScrollPane.setAutoscrolls(true);
		filterScrollPane.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 10));
		filterScrollPane.setBackground(ColorConstants.BLACK_37);
		filterScrollPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JButton saveButtonFilter = new JButton(LabelConstants.SAVE);
		saveButtonFilter.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		saveButtonFilter.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		saveButtonFilter.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 14));
		saveButtonFilter.setFocusable(false);
		saveButtonFilter.setBorderPainted(false);
		saveButtonFilter.setBorder(new EmptyBorder(5, 5, 5, 5));
		saveButtonFilter.setBackground(ColorConstants.HIGHLIGHT);
		
		JButton applyFilterButton = new JButton("Apply");
		applyFilterButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		applyFilterButton.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 14));
		applyFilterButton.setFocusable(false);
		applyFilterButton.setBorder(null);
		applyFilterButton.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		applyFilterButton.setBackground(ColorConstants.HIGHLIGHT);
		
		filterModel = new FilterTableModel();
		filterTable = new JTable(filterModel, ColorConstants.TABLE_ROW, ColorConstants.BLACK_37);
		filterTable.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 10));
		filterTable.setFillsViewportHeight(true);
		filterTable.setShowGrid(false);
		filterTable.setOpaque(true);
		filterTable.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		filterTable.setBackground(ColorConstants.BLACK_37);
		filterTable.setBorder(null);
		filterTable.setShowHorizontalLines(false);
		filterTable.setShowVerticalLines(false);
		filterTable.setRowMargin(0);
		filterTable.setRowHeight(30);
		filterTable.setSelectionBackground(ColorConstants.LIGHT_GRAY);
		filterTable.setSelectionForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		filterTable.setDefaultRenderer(Action.class, new DefaultCellRenderer());
		filterTable.setDefaultRenderer(Goal.class, new DefaultCellRenderer());
		filterTable.setDefaultRenderer(ModulePath.class, new DefaultCellRenderer());
		filterTable.setDefaultEditor(Action.class, new ActionCellEditor());
		filterTable.setDefaultEditor(Goal.class, new GoalCellEditor());
		filterTable.setDefaultEditor(ModulePath.class, new ModulePathCellEditor(this));
		
		TableColumnModel filterColumnIndex = filterTable.getColumnModel();
		filterColumnIndex.getColumn(0).setMaxWidth(20);
		filterColumnIndex.getColumn(1).setPreferredWidth(120);
		filterColumnIndex.getColumn(2).setPreferredWidth(40);
		filterColumnIndex.getColumn(3).setPreferredWidth(40);
		filterColumnIndex.getColumn(4).setPreferredWidth(100);
		filterColumnIndex.getColumn(5).setPreferredWidth(30);
		filterScrollPane.setViewportView(filterTable);
		
		JPopupMenu popupFilterMenu = new JPopupMenu();
		JMenuItem addFilterItem = new JMenuItem(LabelConstants.ADD);
		JMenuItem deleteFilterItem = new JMenuItem(LabelConstants.DELETE);
		popupFilterMenu.add(addFilterItem);
		popupFilterMenu.add(deleteFilterItem);
        filterTable.setComponentPopupMenu(popupFilterMenu);
		
        
		/*
		 * Execution Tab components
		 */
		JPanel executionTab = new JPanel();
		tabbedPane.addTab(LabelConstants.EXECUTION, null, executionTab, TooltipsConstants.EXECUTION_TAB);
		executionTab.setBackground(ColorConstants.BLACK_37);
		
		this.commandEditorWindow = new CommandEditorWindow();
		
		JScrollPane executionScrollPane = new JScrollPane();
		executionScrollPane.setOpaque(false);
		executionScrollPane.setBackground(ColorConstants.BLACK_37);
		executionScrollPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		executionModel = new ExecutionTableModel();
		executionTable = new JTable(executionModel, ColorConstants.TABLE_ROW, ColorConstants.BLACK_37);
		executionTable.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 10));
		executionTable.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		executionTable.setOpaque(true);
		executionTable.setShowGrid(false);
		executionTable.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		executionTable.setBackground(ColorConstants.BLACK_37);
		executionTable.setBorder(new EmptyBorder(5, 5, 5, 5));
		executionTable.setFillsViewportHeight(true);
		executionTable.setShowHorizontalLines(false);
		executionTable.setShowVerticalLines(false);
		executionTable.setRowMargin(0);
		executionTable.setRowHeight(30);
		executionTable.setSelectionBackground(ColorConstants.LIGHT_GRAY);
		executionTable.setSelectionForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		executionTable.setDefaultRenderer(ModulePath.class, new DefaultCellRenderer());
		executionTable.setDefaultEditor(ModulePath.class, new ModulePathCellEditor(this));
		executionTable.setDefaultRenderer(AfterCommand.class, new DefaultCellRenderer());
		executionTable.setDefaultEditor(AfterCommand.class, new AfterCommandCellEditor(commandEditorWindow));
		executionTable.setDefaultRenderer(BeforeCommand.class, new DefaultCellRenderer());
		executionTable.setDefaultEditor(BeforeCommand.class, new BeforeCommandCellEditor(commandEditorWindow));
		
		TableColumnModel executionColumnModel = executionTable.getColumnModel();
		executionColumnModel.getColumn(0).setMaxWidth(20);
		executionColumnModel.getColumn(1).setPreferredWidth(120);
		executionColumnModel.getColumn(2).setPreferredWidth(40);
		executionColumnModel.getColumn(3).setPreferredWidth(40);
		executionScrollPane.setViewportView(executionTable);
		
		JPopupMenu popupExecutionMenu = new JPopupMenu();
		JMenuItem addExecutionItem = new JMenuItem(LabelConstants.ADD);
		JMenuItem deleteExecutionItem = new JMenuItem(LabelConstants.DELETE);
		popupExecutionMenu.add(addExecutionItem);
		popupExecutionMenu.add(deleteExecutionItem);
		executionTable.setComponentPopupMenu(popupExecutionMenu);
		
		JButton applyExecutionButton = new JButton("Apply");
		applyExecutionButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		applyExecutionButton.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		applyExecutionButton.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 14));
		applyExecutionButton.setFocusable(false);
		applyExecutionButton.setBorder(null);
		applyExecutionButton.setBackground(ColorConstants.HIGHLIGHT);
		
		JButton saveButtonExecution = new JButton(LabelConstants.SAVE);
		saveButtonExecution.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		saveButtonExecution.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		saveButtonExecution.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 14));
		saveButtonExecution.setFocusable(false);
		saveButtonExecution.setBorderPainted(false);
		saveButtonExecution.setBorder(new EmptyBorder(5, 5, 5, 5));
		saveButtonExecution.setBackground(ColorConstants.HIGHLIGHT);
		
		loadDefaults();
		
		
		
		/*
		 * Event handler setup
		 */
		headerPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				xLoc = e.getX();
				yLoc = e.getY();
			}
		});
		
		headerPanel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				int x = arg0.getXOnScreen() - xLoc;
				int y = arg0.getYOnScreen() - yLoc;
				setLocation(x, y);
			}
		});
		
		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				dispose();
			}
		});
		
		
		generalSaveButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				saveGeneralTab();
				dispose();
			}
		});
		
		generalApplyButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				saveGeneralTab();
			}
		});
		
		directoryAddButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				TreeSelectionModel selected = moduleDirectoryTree.getSelectionModel();
				if(selected.getSelectionCount() > 0) {
					DefaultTreeModel model = (DefaultTreeModel) moduleDirectoryTree.getModel();
				    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) moduleDirectoryTree.getSelectionPath().getLastPathComponent();
		            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("        ");
	                model.insertNodeInto(newNode, selectedNode, selectedNode.getChildCount());
	                model.reload();
	                moduleDirectoryTree.scrollPathToVisible(new TreePath(newNode.getPath()));
				}
				
			}
		});
		
		directoryRemoveButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TreeSelectionModel selected = moduleDirectoryTree.getSelectionModel();
				if(selected.getSelectionCount() > 0) {
					DefaultTreeModel model = (DefaultTreeModel) moduleDirectoryTree.getModel();
				    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) moduleDirectoryTree.getSelectionPath().getLastPathComponent();
	                model.removeNodeFromParent(selectedNode);
				}
			}
		});
		
		scanRootButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				DefaultTreeModel model = (DefaultTreeModel) moduleDirectoryTree.getModel();
				DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		        root.removeAllChildren();
		        model.reload();
		        model.setRoot(POMScanner.getInstance().scan());
			}
		});
		
		projectPathTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getClickCount() == 2) {
					JFileChooser fileChooser = Util.getWindowsJFileChooser(projectPathTextField.getText());
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					fileChooser.showDialog(projectPathTextField, "Select");
					if(fileChooser.getSelectedFile() != null) {
						String filePath = fileChooser.getSelectedFile().getAbsolutePath();
						projectPathTextField.setText(filePath);
					}
				}
			}
		});
		
		popupSysEnvMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = sysEnvTable.rowAtPoint(SwingUtilities.convertPoint(popupSysEnvMenu, new Point(0, 0), sysEnvTable));
                        if (rowAtPoint > -1) {
                        	sysEnvTable.setRowSelectionInterval(rowAtPoint, rowAtPoint);
                        	deleteSysEnvItem.setEnabled(true);
                        }else {
                        	deleteSysEnvItem.setEnabled(false);
                        }
                    }
                });
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
		
		deleteSysEnvItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int rowIndex = sysEnvTable.getSelectedRow();
				if(rowIndex >= 0) {
					sysEnvModel.deleteRowAt(rowIndex);
				}
			}
		});
		
		addSysEnvItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sysEnvModel.addNewRow();
			}
		});
		
		popupTargetMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = targetTable.rowAtPoint(SwingUtilities.convertPoint(popupTargetMenu, new Point(0, 0), targetTable));
                        if (rowAtPoint > -1) {
                        	targetTable.setRowSelectionInterval(rowAtPoint, rowAtPoint);
                        	deleteTargetItem.setEnabled(true);
                        }else {
                        	deleteTargetItem.setEnabled(false);
                        }
                    }
                });
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
		
		deleteTargetItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int rowIndex = targetTable.getSelectedRow();
				if(rowIndex >= 0) {
					targetModel.deleteRowAt(rowIndex);
				}
			}
		});
		
		addTargetItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				targetModel.addNewRow();
			}
		});
		
		saveButtonFilter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				saveFilterTab();
				dispose();
			}
		});
		
		popupFilterMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = filterTable.rowAtPoint(SwingUtilities.convertPoint(popupFilterMenu, new Point(0, 0), filterTable));
                        if (rowAtPoint > -1) {
                        	filterTable.setRowSelectionInterval(rowAtPoint, rowAtPoint);
                        	deleteFilterItem.setEnabled(true);
                        }else {
                        	deleteFilterItem.setEnabled(false);
                        }
                    }
                });
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
		
		deleteFilterItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int rowIndex = filterTable.getSelectedRow();
				if(rowIndex >= 0) {
					filterModel.deleteRowAt(rowIndex);
				}
			}
		});
		
		addFilterItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				filterModel.addNewRow();
			}
		});
		
		applyFilterButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				saveFilterTab();
			}
		});
		
		popupExecutionMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = executionTable.rowAtPoint(SwingUtilities.convertPoint(popupExecutionMenu, new Point(0, 0), executionTable));
                        if (rowAtPoint > -1) {
                        	executionTable.setRowSelectionInterval(rowAtPoint, rowAtPoint);
                        	deleteExecutionItem.setEnabled(true);
                        }else {
                        	deleteExecutionItem.setEnabled(false);
                        }
                    }
                });
            }
            
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
		
		deleteExecutionItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int rowIndex = executionTable.getSelectedRow();
				if(rowIndex >= 0) {
					executionModel.deleteRowAt(rowIndex);
				}
			}
		});
		
		addExecutionItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				executionModel.addNewRow();
			}
		});
		
		applyExecutionButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				saveExecutionTab();
			}
		});
		
		saveButtonExecution.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				saveExecutionTab();
				dispose();
			}
		});
		
		
		
		/*
		 * Group layout setup
		 */
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(12)
							.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE))
						.addComponent(headerPanel, GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(headerPanel, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		GroupLayout gl_sysEnvWrapper = new GroupLayout(sysEnvWrapper);
		gl_sysEnvWrapper.setHorizontalGroup(
			gl_sysEnvWrapper.createParallelGroup(Alignment.TRAILING)
				.addComponent(sysEnvScrollPanel, GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
		);
		gl_sysEnvWrapper.setVerticalGroup(
			gl_sysEnvWrapper.createParallelGroup(Alignment.LEADING)
				.addComponent(sysEnvScrollPanel, GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
		);
		sysEnvWrapper.setLayout(gl_sysEnvWrapper);
		
		GroupLayout gl_generalTab = new GroupLayout(generalTab);
		gl_generalTab.setHorizontalGroup(
			gl_generalTab.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_generalTab.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_generalTab.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_generalTab.createSequentialGroup()
							.addComponent(generalApplyButton, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(generalSaveButton, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE))
						.addComponent(generalScrollPanel, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
		);
		gl_generalTab.setVerticalGroup(
			gl_generalTab.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_generalTab.createSequentialGroup()
					.addComponent(generalScrollPanel, GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
					.addGap(18)
					.addGroup(gl_generalTab.createParallelGroup(Alignment.TRAILING)
						.addComponent(generalSaveButton, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(generalApplyButton, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)))
		);
		
		GroupLayout gl_generalWrapper = new GroupLayout(generalWrapper);
		gl_generalWrapper.setHorizontalGroup(
			gl_generalWrapper.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_generalWrapper.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_generalWrapper.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_generalWrapper.createSequentialGroup()
							.addGroup(gl_generalWrapper.createParallelGroup(Alignment.TRAILING)
								.addComponent(targetWrapper, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
								.addComponent(defaultsPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addGap(23))
						.addGroup(gl_generalWrapper.createSequentialGroup()
							.addGroup(gl_generalWrapper.createParallelGroup(Alignment.TRAILING)
								.addComponent(directoryWrapper, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(sysEnvWrapper, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE))
							.addGap(23))))
		);
		gl_generalWrapper.setVerticalGroup(
			gl_generalWrapper.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_generalWrapper.createSequentialGroup()
					.addContainerGap()
					.addComponent(defaultsPanel, GroupLayout.PREFERRED_SIZE, 258, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(targetWrapper, GroupLayout.PREFERRED_SIZE, 244, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(sysEnvWrapper, GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(directoryWrapper, GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		GroupLayout gl_directoryWrapper = new GroupLayout(directoryWrapper);
		gl_directoryWrapper.setHorizontalGroup(
			gl_directoryWrapper.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_directoryWrapper.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_directoryWrapper.createParallelGroup(Alignment.TRAILING)
						.addComponent(moduleDirectoryScrollPanel, GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
						.addGroup(gl_directoryWrapper.createSequentialGroup()
							.addComponent(directoryAddButton, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
							.addGap(12)
							.addComponent(directoryRemoveButton, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(scanRootButton, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_directoryWrapper.setVerticalGroup(
			gl_directoryWrapper.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_directoryWrapper.createSequentialGroup()
					.addComponent(moduleDirectoryScrollPanel, GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_directoryWrapper.createParallelGroup(Alignment.LEADING)
						.addComponent(directoryAddButton, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_directoryWrapper.createParallelGroup(Alignment.BASELINE)
							.addComponent(directoryRemoveButton, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
							.addComponent(scanRootButton, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)))
					.addGap(16))
		);
		directoryWrapper.setLayout(gl_directoryWrapper);
		
		GroupLayout gl_targetWrapper = new GroupLayout(targetWrapper);
		gl_targetWrapper.setHorizontalGroup(
			gl_targetWrapper.createParallelGroup(Alignment.TRAILING)
				.addComponent(targetScrollPane, GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
		);
		gl_targetWrapper.setVerticalGroup(
			gl_targetWrapper.createParallelGroup(Alignment.LEADING)
				.addComponent(targetScrollPane, GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
		);
		targetWrapper.setLayout(gl_targetWrapper);
		
		GroupLayout gl_defaultsPanel = new GroupLayout(defaultsPanel);
		gl_defaultsPanel.setHorizontalGroup(
			gl_defaultsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_defaultsPanel.createSequentialGroup()
					.addGap(17)
					.addGroup(gl_defaultsPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_defaultsPanel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_defaultsPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(mavenOptsLabel)
								.addGroup(gl_defaultsPanel.createSequentialGroup()
									.addComponent(actionLabel)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(buildCheckbox)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(replaceCheckBox)
									.addGap(44)
									.addComponent(goalLabel)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(goalComboBox, 0, 176, Short.MAX_VALUE))
								.addComponent(buildParametersLabel)
								.addComponent(rootPathLabel))
							.addPreferredGap(ComponentPlacement.RELATED))
						.addGroup(gl_defaultsPanel.createSequentialGroup()
							.addGap(27)
							.addComponent(buildParamsTextField, GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE))
						.addGroup(gl_defaultsPanel.createSequentialGroup()
							.addGap(27)
							.addComponent(projectPathTextField, GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE))
						.addGroup(gl_defaultsPanel.createSequentialGroup()
							.addGap(27)
							.addComponent(mavenOptsTextField, GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)))
					.addGap(31))
		);
		gl_defaultsPanel.setVerticalGroup(
			gl_defaultsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_defaultsPanel.createSequentialGroup()
					.addComponent(rootPathLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(projectPathTextField, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
					.addGap(12)
					.addComponent(buildParametersLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(buildParamsTextField, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(mavenOptsLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(mavenOptsTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(14)
					.addGroup(gl_defaultsPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(actionLabel)
						.addComponent(goalLabel)
						.addComponent(buildCheckbox)
						.addComponent(replaceCheckBox)
						.addComponent(goalComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(365))
		);
		defaultsPanel.setLayout(gl_defaultsPanel);
		generalWrapper.setLayout(gl_generalWrapper);
		generalTab.setLayout(gl_generalTab);
		
		GroupLayout gl_filterTab = new GroupLayout(filterTab);
		gl_filterTab.setHorizontalGroup(
			gl_filterTab.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_filterTab.createSequentialGroup()
					.addContainerGap(404, Short.MAX_VALUE)
					.addComponent(applyFilterButton, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(saveButtonFilter, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
					.addGap(6))
				.addComponent(filterScrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
		);
		gl_filterTab.setVerticalGroup(
			gl_filterTab.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_filterTab.createSequentialGroup()
					.addComponent(filterScrollPane, GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_filterTab.createParallelGroup(Alignment.BASELINE)
						.addComponent(saveButtonFilter, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(applyFilterButton, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		filterTab.setLayout(gl_filterTab);
		
		GroupLayout gl_executionTab = new GroupLayout(executionTab);
		gl_executionTab.setHorizontalGroup(
			gl_executionTab.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_executionTab.createSequentialGroup()
					.addContainerGap(404, Short.MAX_VALUE)
					.addComponent(applyExecutionButton, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(saveButtonExecution, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
					.addGap(6))
				.addComponent(executionScrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
		);
		gl_executionTab.setVerticalGroup(
			gl_executionTab.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_executionTab.createSequentialGroup()
					.addComponent(executionScrollPane, GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_executionTab.createParallelGroup(Alignment.BASELINE)
						.addComponent(saveButtonExecution, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(applyExecutionButton, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		executionTab.setLayout(gl_executionTab);
		
		JLabel settingsLabel = new JLabel("Settings");
		settingsLabel.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		
		GroupLayout gl_headerPanel = new GroupLayout(headerPanel);
		gl_headerPanel.setHorizontalGroup(
			gl_headerPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_headerPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(settingsLabel)
					.addPreferredGap(ComponentPlacement.RELATED, 476, Short.MAX_VALUE)
					.addComponent(closeButton, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
		);
		gl_headerPanel.setVerticalGroup(
			gl_headerPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_headerPanel.createSequentialGroup()
					.addGroup(gl_headerPanel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(closeButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
						.addComponent(settingsLabel, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		headerPanel.setLayout(gl_headerPanel);
		contentPane.setLayout(gl_contentPane);
		
	}
	
	public void loadDefaults() {
		Defaults defaults = XMLUtil.getInstance().getDefaults();
		projectPathTextField.setText(defaults.getProjectPath());
		buildParamsTextField.setText(defaults.getBuildParameters());
		mavenOptsTextField.setText(defaults.getMavenOpts());
		buildCheckbox.setSelected(defaults.getIsBuild());
		replaceCheckBox.setSelected(defaults.getIsReplace());
		goalComboBox.setSelectedItem(defaults.getGoal());
	}
	
	public void saveGeneralTab() {
		/*
		 * Saving defaults in XML file
		 */
		String projectPath = projectPathTextField.getText();
		String buildParameters = buildParamsTextField.getText();
		String mavenOpts = mavenOptsTextField.getText();
		Boolean isBuild = buildCheckbox.isSelected();
		Boolean isReplace = replaceCheckBox.isSelected();
		Goal goal = (Goal) goalComboBox.getSelectedItem();
		Defaults defaults = new Defaults(projectPath, buildParameters, mavenOpts, isBuild, isReplace, goal);
		
		/*
		 * Saving module directory tree in XML file
		 */
		DefaultTreeModel model = (DefaultTreeModel) moduleDirectoryTree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		POMDirectory pomDirectory = new POMDirectory(root);
		
		/*
		 * Saving target lists and sysenv lists in XML file
		 */
		List<Target> targetList = targetModel.getTargetList();
		List<SysEnv> sysEnvList = sysEnvModel.getSysEnvList();
		
		XMLUtil.getInstance().setGeneralSettings(pomDirectory, defaults, targetList, sysEnvList);
		MainWindow.getInstance().refreshHandlers();
		
		JOptionPane.showMessageDialog(SettingsWindow.this,
				"Saved successfully!", "General Tab", JOptionPane.DEFAULT_OPTION);
	}
	
	public void saveFilterTab() {
		filterModel.saveData();
		MainWindow.getInstance().refreshHandlers();
		
		JOptionPane.showMessageDialog(SettingsWindow.this,
				"Saved successfully!", "Filter Tab", JOptionPane.DEFAULT_OPTION);
	}
	
	public void saveExecutionTab() {
		executionModel.saveToXML();
		
		JOptionPane.showMessageDialog(SettingsWindow.this,
				"Saved successfully!", "Execution Tab", JOptionPane.DEFAULT_OPTION);
	}
	
	@Override
	public void dispose() {
		commandEditorWindow.dispose();
		super.dispose();
	}
}

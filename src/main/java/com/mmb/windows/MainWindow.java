/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.windows;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import com.mmb.components.BuildButton;
import com.mmb.components.CircleButton;
import com.mmb.components.RoundBorder;
import com.mmb.components.RoundTextField;
import com.mmb.dialog.EditLabelDialog;
import com.mmb.dialog.SettingsDialog;
import com.mmb.dialog.SingleModuleDialog;
import com.mmb.models.Defaults;
import com.mmb.models.Shortcut;
import com.mmb.models.filters.Goal;
import com.mmb.resources.Resources;
import com.mmb.util.ExecutionHandler;
import com.mmb.util.FilterHandler;
import com.mmb.util.ImageUtil;
import com.mmb.util.Util;
import com.mmb.util.XMLUtil;
import com.mmb.util.constants.ColorConstants;
import com.mmb.util.constants.Constants;
import com.mmb.util.constants.LabelConstants;

public class MainWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6394818958047639210L;
	
	private Integer xLoc;
	private Integer yLoc;
	private String modulePath;
	
	private BuildLogWindow buildLogWindow = new BuildLogWindow();
	private BuildHistoryWindow buildHistoryWindow = new BuildHistoryWindow();
	private SettingsWindow settingsWindow = new SettingsWindow(this);
	private MultiModuleWindow multiModuleWindow = new MultiModuleWindow();
	private AboutWindow aboutWindow = new AboutWindow();
	private SettingsDialog settingsDialog = null;
	
	private CircleButton settingsButton;
	private BuildButton buildButton;
	private RoundTextField moduleTextField;
	private JPanel mainPanel;
	private JPanel contentPanel;
	private JToggleButton goalToggle;
	private JToggleButton buildActionButton;
	private JToggleButton replaceButton;
	private JToggleButton moduleToggleButton;
	private JLabel lastBuildLabel;
	private FilterHandler filterHandler;
	private JLabel shortcut1Label;
	private JLabel shortcut2Label;
	private JLabel shortcut3Label;
	private static MainWindow mainWindow;
	
	public static synchronized MainWindow getInstance() {
		if(mainWindow == null) {
			mainWindow = new MainWindow();
		}
		
		return mainWindow;
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 * @throws UnsupportedLookAndFeelException 
	 */
	public MainWindow() {
		MainWindow.mainWindow = this;
		filterHandler = new FilterHandler(this);
		
		setResizable(false);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(37, 37, 500, 600);

		mainPanel = new JPanel();
		mainPanel.setBackground(ColorConstants.BLACK_37);
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPanel.setLayout(null);
		setContentPane(mainPanel);
		
		contentPanel = new JPanel();
		contentPanel.setBackground(ColorConstants.BLACK_60);
		contentPanel.setBounds(0, 316, 500, 250);
		mainPanel.add(contentPanel);
		
		JLabel moduleLabel = new JLabel(LabelConstants.MODULE);
		moduleLabel.setBounds(43, 12, 59, 34);
		moduleLabel.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 18));
		moduleLabel.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		
		JLabel moduleDivision = new JLabel("");
		moduleDivision.setBounds(40, 54, 419, 4);
		moduleDivision.setBorder(new MatteBorder(1, 0, 0, 0, ColorConstants.BLACK_37));
		
		moduleToggleButton = new JToggleButton("");
		moduleToggleButton.setBounds(109, 12, 64, 35);
		moduleToggleButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		moduleToggleButton.setContentAreaFilled(false);
		moduleToggleButton.setBorderPainted(false);
		moduleToggleButton.setFocusPainted(false);
		moduleToggleButton.setBorder(null);
		moduleToggleButton.setRolloverEnabled(true);
		moduleToggleButton.setMargin(new Insets(0, 0, 0, 0));
		moduleToggleButton.setBackground(ColorConstants.BLACK_60);
		moduleToggleButton.setForeground(ColorConstants.BLACK_60);
		moduleToggleButton.setFocusable(false);
		moduleToggleButton.setIcon(ImageUtil.getImageIcon(Resources.TOGGLE_DEFAULT));
		moduleToggleButton.setSelectedIcon(ImageUtil.getImageIcon(Resources.TOGGLE_SELECTED));
		
		JLabel actionLabel = new JLabel(LabelConstants.ACTION);
		actionLabel.setBounds(69, 120, 64, 34);
		actionLabel.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		actionLabel.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 18));
		
		JLabel goalLabel = new JLabel(LabelConstants.GOAL);
		goalLabel.setBounds(225, 120, 64, 34);
		goalLabel.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		goalLabel.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 18));
		
		JLabel shortcutsLabel = new JLabel(LabelConstants.SHORTCUTS);
		shortcutsLabel.setBounds(351, 120, 76, 34);
		shortcutsLabel.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		shortcutsLabel.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 18));
		
		replaceButton = new JToggleButton("");
		replaceButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		replaceButton.setContentAreaFilled(false);
		replaceButton.setBorderPainted(false);
		replaceButton.setBackground(ColorConstants.BLACK_60);
		replaceButton.setDisabledIcon(ImageUtil.getImageIcon(Resources.DISABLED_REPLACE));
		replaceButton.setIcon(ImageUtil.getImageIcon(Resources.DEFAULT_REPLACE));
		replaceButton.setSelectedIcon(ImageUtil.getImageIcon(Resources.SELECTED_REPLACE));
		replaceButton.setLocation(52, 166);
		replaceButton.setSize(new Dimension(35, 35));
		replaceButton.setPreferredSize(new Dimension(35, 35));
		replaceButton.setDisabledSelectedIcon(ImageUtil.getImageIcon(Resources.DISABLED_REPLACE));
		
		buildActionButton = new JToggleButton("");
		buildActionButton.setSelected(true);
		buildActionButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		buildActionButton.setLocation(104, 166);
		buildActionButton.setSize(new Dimension(35, 35));
		buildActionButton.setPreferredSize(new Dimension(35, 35));
		buildActionButton.setContentAreaFilled(false);
		buildActionButton.setBorderPainted(false);
		buildActionButton.setBackground(ColorConstants.BLACK_60);
		buildActionButton.setDisabledIcon(ImageUtil.getImageIcon(Resources.DISABLED_BUILD));
		buildActionButton.setIcon(ImageUtil.getImageIcon(Resources.DEFAULT_BUILD));
		buildActionButton.setSelectedIcon(ImageUtil.getImageIcon(Resources.SELECTED_BUILD));
		buildActionButton.setDisabledSelectedIcon(ImageUtil.getImageIcon(Resources.DISABLED_BUILD));
		
		goalToggle = new JToggleButton("");
		goalToggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		goalToggle.setBounds(212, 166, 64, 35);
		goalToggle.setRolloverEnabled(true);
		goalToggle.setMargin(new Insets(0, 0, 0, 0));
		goalToggle.setForeground(ColorConstants.BLACK_60);
		goalToggle.setFocusable(false);
		goalToggle.setFocusPainted(false);
		goalToggle.setContentAreaFilled(false);
		goalToggle.setBorder(null);
		goalToggle.setBackground(ColorConstants.BLACK_60);
		goalToggle.setIcon(ImageUtil.getImageIcon(Resources.TOGGLE_DEFAULT));
		goalToggle.setSelectedIcon(ImageUtil.getImageIcon(Resources.TOGGLE_SELECTED));
		goalToggle.setDisabledIcon(ImageUtil.getImageIcon(Resources.TOGGLE_DEFAULT_DISABLED));
		goalToggle.setDisabledSelectedIcon(ImageUtil.getImageIcon(Resources.TOGGLE_SELECTED_DISABLED));
		
		JToggleButton shortcut1Button = new JToggleButton("");
		shortcut1Button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		shortcut1Button.setLocation(332, 166);
		shortcut1Button.setSize(new Dimension(35, 35));
		shortcut1Button.setPreferredSize(new Dimension(35, 35));
		shortcut1Button.setContentAreaFilled(false);
		shortcut1Button.setBorderPainted(false);
		shortcut1Button.setBackground(ColorConstants.BLACK_60);
		shortcut1Button.setDisabledIcon(ImageUtil.getImageIcon(Resources.DISABLED_1));
		shortcut1Button.setIcon(ImageUtil.getImageIcon(Resources.DEFAULT_1));
		shortcut1Button.setSelectedIcon(ImageUtil.getImageIcon(Resources.SELECTED_1));
		
		JPopupMenu shortcut1PopupMenu = new JPopupMenu();
		JMenuItem setLabel1MenuItem = new JMenuItem("Set Label");
		JMenuItem selectFile1MenuItem = new JMenuItem("Select File");
		shortcut1PopupMenu.add(setLabel1MenuItem);
        shortcut1PopupMenu.add(selectFile1MenuItem);
        shortcut1Button.setComponentPopupMenu(shortcut1PopupMenu);
		
		JToggleButton shortcut2Button = new JToggleButton("");
		shortcut2Button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		shortcut2Button.setLocation(373, 166);
		shortcut2Button.setSize(new Dimension(35, 35));
		shortcut2Button.setPreferredSize(new Dimension(35, 35));
		shortcut2Button.setContentAreaFilled(false);
		shortcut2Button.setBorderPainted(false);
		shortcut2Button.setBackground(ColorConstants.BLACK_60);
		shortcut2Button.setDisabledIcon(ImageUtil.getImageIcon(Resources.DISABLED_2));
		shortcut2Button.setIcon(ImageUtil.getImageIcon(Resources.DEFAULT_2));
		shortcut2Button.setSelectedIcon(ImageUtil.getImageIcon(Resources.SELECTED_2));
		
		JPopupMenu shortcut2PopupMenu = new JPopupMenu();
		JMenuItem setLabel2MenuItem = new JMenuItem("Set Label");
		JMenuItem selectFile2MenuItem = new JMenuItem("Select File");
		shortcut2PopupMenu.add(setLabel2MenuItem);
        shortcut2PopupMenu.add(selectFile2MenuItem);
        shortcut2Button.setComponentPopupMenu(shortcut2PopupMenu);
        
		JToggleButton shortcut3Button = new JToggleButton("");
		shortcut3Button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		shortcut3Button.setLocation(414, 166);
		shortcut3Button.setSize(new Dimension(35, 35));
		shortcut3Button.setPreferredSize(new Dimension(35, 35));
		shortcut3Button.setContentAreaFilled(false);
		shortcut3Button.setBorderPainted(false);
		shortcut3Button.setBackground(ColorConstants.BLACK_60);
		shortcut3Button.setDisabledIcon(ImageUtil.getImageIcon(Resources.DISABLED_3));
		shortcut3Button.setIcon(ImageUtil.getImageIcon(Resources.DEFAULT_3));
		shortcut3Button.setSelectedIcon(ImageUtil.getImageIcon(Resources.SELECTED_3));
		
		JPopupMenu shortcut3PopupMenu = new JPopupMenu();
		JMenuItem setLabel3MenuItem = new JMenuItem("Set Label");
		JMenuItem selectFile3MenuItem = new JMenuItem("Select File");
		shortcut3PopupMenu.add(setLabel3MenuItem);
        shortcut3PopupMenu.add(selectFile3MenuItem);
        shortcut3Button.setComponentPopupMenu(shortcut3PopupMenu);
        
		contentPanel.setLayout(null);
		contentPanel.add(actionLabel);
		contentPanel.add(goalLabel);
		contentPanel.add(shortcutsLabel);
		contentPanel.add(moduleDivision);
		
		JLabel actionDivision = new JLabel("");
		actionDivision.setBounds(42, 154, 107, 1);
		actionDivision.setBorder(new MatteBorder(1, 0, 0, 0, ColorConstants.BLACK_37));
		contentPanel.add(actionDivision);
		
		JLabel shortcutsDivision = new JLabel("");
		shortcutsDivision.setBounds(320, 154, 139, 1);
		shortcutsDivision.setBorder(new MatteBorder(1, 0, 0, 0, ColorConstants.BLACK_37));
		contentPanel.add(shortcutsDivision);
		
		JLabel goalDivision = new JLabel("");
		goalDivision.setBounds(191, 154, 107, 1);
		goalDivision.setBorder(new MatteBorder(1, 0, 0, 0, ColorConstants.BLACK_37));
		contentPanel.add(goalDivision);
		contentPanel.add(moduleLabel);
		contentPanel.add(moduleToggleButton);
		contentPanel.add(replaceButton);
		contentPanel.add(buildActionButton);
		contentPanel.add(goalToggle);
		contentPanel.add(shortcut1Button);
		contentPanel.add(shortcut2Button);
		contentPanel.add(shortcut3Button);
		
		JLabel replaceLabel = new JLabel(LabelConstants.REPLACE);
		replaceLabel.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		replaceLabel.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 12));
		replaceLabel.setBounds(46, 205, 46, 19);
		contentPanel.add(replaceLabel);
		
		JLabel buildLabel = new JLabel(LabelConstants.BUILD);
		buildLabel.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		buildLabel.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 12));
		buildLabel.setBounds(106, 205, 30, 19);
		contentPanel.add(buildLabel);
		
		JLabel installLabel = new JLabel(LabelConstants.INSTALL);
		installLabel.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		installLabel.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 12));
		installLabel.setBounds(191, 205, 35, 19);
		contentPanel.add(installLabel);
		
		JLabel compileLabel = new JLabel(LabelConstants.COMPILE);
		compileLabel.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		compileLabel.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 12));
		compileLabel.setBounds(259, 205, 49, 19);
		contentPanel.add(compileLabel);
		
		shortcut1Label = new JLabel("1");
		shortcut1Label.setHorizontalAlignment(SwingConstants.CENTER);
		shortcut1Label.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		shortcut1Label.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 12));
		shortcut1Label.setBounds(332, 205, 35, 19);
		contentPanel.add(shortcut1Label);
		
		shortcut2Label = new JLabel("2");
		shortcut2Label.setHorizontalAlignment(SwingConstants.CENTER);
		shortcut2Label.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		shortcut2Label.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 12));
		shortcut2Label.setBounds(373, 205, 35, 19);
		contentPanel.add(shortcut2Label);
		
		shortcut3Label = new JLabel("3");
		shortcut3Label.setHorizontalAlignment(SwingConstants.CENTER);
		shortcut3Label.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		shortcut3Label.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 12));
		shortcut3Label.setBounds(414, 205, 35, 19);
		contentPanel.add(shortcut3Label);
		
		moduleTextField = new RoundTextField(30);
		moduleTextField.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 12));
		moduleTextField.setEditable(false);
		moduleTextField.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		moduleTextField.setBounds(40, 70, 419, 27);
		moduleTextField.setColumns(10);
		contentPanel.add(moduleTextField);
		
		JPanel headerPanel = new JPanel();
		headerPanel.setDoubleBuffered(false);
		headerPanel.setBorder(null);
		headerPanel.setBounds(0, 0, 500, 52);
		headerPanel.setBackground(ColorConstants.BLACK_37);
		headerPanel.setLayout(null);
		mainPanel.add(headerPanel);
		
		JButton closeButton = new JButton("");
		closeButton.setBorder(null);
		closeButton.setFocusable(false);
		closeButton.setBounds(454, 12, 34, 35);
		closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		closeButton.setContentAreaFilled(false);
		closeButton.setBorderPainted(false);
		closeButton.setFocusPainted(false);
		closeButton.setForeground(ColorConstants.BLACK_37);
		closeButton.setBackground(ColorConstants.BLACK_37);
		closeButton.setPressedIcon(ImageUtil.getImageIcon(Resources.CLOSE_PRESS));
		closeButton.setRolloverIcon(ImageUtil.getImageIcon(Resources.CLOSE_ROLLOVER));
		closeButton.setIcon(ImageUtil.getImageIcon(Resources.CLOSE));
		headerPanel.add(closeButton);
		
		buildButton = new BuildButton(this);
		buildButton.setBounds(162, 78, 160, 160);
		mainPanel.add(buildButton);
		
		JLabel moduleBuilderLabel = new JLabel(LabelConstants.MODULE_BUILDER);
		moduleBuilderLabel.setFont(new Font(Constants.FONT_DIALOG, Font.BOLD | Font.ITALIC, 24));
		moduleBuilderLabel.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
		moduleBuilderLabel.setBounds(208, 256, 177, 35);
		mainPanel.add(moduleBuilderLabel);
		
		JLabel mavenLogo = new JLabel("");
		mavenLogo.setIcon(ImageUtil.getImageIcon(Resources.MAVEN));
		mavenLogo.setBounds(111, 258, 100, 30);
		mainPanel.add(mavenLogo);
		
		JLabel backgroundLabel = new JLabel("");
		backgroundLabel.setBorder(new RoundBorder(new Color(21, 21, 21), 20, true));
		backgroundLabel.setOpaque(true);
		backgroundLabel.setBackground(ColorConstants.BLACK_37);
		backgroundLabel.setBounds(100, 256, 300, 35);
		mainPanel.add(backgroundLabel);
		
		settingsButton = new CircleButton("", ColorConstants.BLACK_60);
		settingsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		settingsButton.setBounds(463, 12, 25, 25);
		settingsButton.setImage(ImageUtil.getImage(Resources.SETTINGS));
		settingsButton.setPressColor(new Color(20,20,20));
		settingsButton.setHoverColor(new Color(90,90,90));
		contentPanel.add(settingsButton);
		
		setupLastBuildStatusLabel();
		refreshHandlers();
		
		
		
		/*
		 * Event handlers setup
		 */
		moduleToggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(moduleToggleButton.isSelected()) {
					multiModuleWindow.setVisible(true);
				}else {
					multiModuleWindow.dispose();
				}
			}
		});
		
		shortcut1Button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				openShortcutFile(XMLUtil.getInstance().getShortcut1().getFile());
				shortcut1Button.setSelected(false);
			}
		});
		
		shortcut2Button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				openShortcutFile(XMLUtil.getInstance().getShortcut2().getFile());
				shortcut2Button.setSelected(false);
			}
		});
		
		shortcut3Button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					openShortcutFile(XMLUtil.getInstance().getShortcut3().getFile());
					shortcut3Button.setSelected(false);
				}
			}
		});
		
		moduleTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				SingleModuleDialog singleModuleWindow = new SingleModuleDialog(MainWindow.this);
				singleModuleWindow.setVisible(true);
			}
		});
		
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
				buildLogWindow.dispose();
				buildHistoryWindow.dispose();
				settingsWindow.dispose();
				multiModuleWindow.dispose();
				aboutWindow.dispose();
				dispose();
			}
		});
		
		settingsButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(settingsDialog == null) {
					settingsDialog = new SettingsDialog(MainWindow.this);
				}
				
				settingsDialog.setVisible(true);
			}
		});
		
        setLabel1MenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	new EditLabelDialog("1").setVisible(true);
            	XMLUtil.getInstance().saveXML();
            }
        });
        
        selectFile1MenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	JFileChooser jFileChooser = Util.getWindowsJFileChooser(XMLUtil.getInstance().getShortcut1().getFile());
            	jFileChooser.showDialog(jFileChooser, "Select File");
				if(jFileChooser.getSelectedFile() != null) {
					String filePath = jFileChooser.getSelectedFile().getAbsolutePath();
					XMLUtil.getInstance().getShortcut1().setFile(filePath);
					XMLUtil.getInstance().saveXML();
				}
            }
        });
		
        setLabel2MenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	new EditLabelDialog("2").setVisible(true);
            	XMLUtil.getInstance().saveXML();
            }
        });
        
        selectFile2MenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	JFileChooser jFileChooser = Util.getWindowsJFileChooser(XMLUtil.getInstance().getShortcut2().getFile());
            	jFileChooser.showDialog(jFileChooser, "Select File");
				if(jFileChooser.getSelectedFile() != null) {
					String filePath = jFileChooser.getSelectedFile().getAbsolutePath();
					XMLUtil.getInstance().getShortcut2().setFile(filePath);
					XMLUtil.getInstance().saveXML();
				}
            }
        });
		
        setLabel3MenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	new EditLabelDialog("3").setVisible(true);
            	XMLUtil.getInstance().saveXML();
            }
        });
        
        selectFile3MenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	JFileChooser jFileChooser = Util.getWindowsJFileChooser(XMLUtil.getInstance().getShortcut3().getFile());
            	jFileChooser.showDialog(jFileChooser, "Select File");
				if(jFileChooser.getSelectedFile() != null) {
					String filePath = jFileChooser.getSelectedFile().getAbsolutePath();
					XMLUtil.getInstance().getShortcut3().setFile(filePath);
					XMLUtil.getInstance().saveXML();
				}
            }
        });
	}
	
	public void setModulePath(String modulePath) {
		this.modulePath = modulePath;
		this.moduleTextField.setText(modulePath);
		
		filterHandler.handleChangeModulePath(modulePath);
		ExecutionHandler.getInstance().handleChangeModulePath(modulePath);
	}
	
	public String getModulePath() {
		String modulePath = (!Util.isNullOrEmpty(this.modulePath)) ? this.modulePath : "";
		return XMLUtil.getInstance().getDefaults().getProjectPath() + File.separator +  modulePath;
	}
	
	public String getModulePathWithoutDirectory() {
		return modulePath;
	}
	
	public String getGoal() {
		return goalToggle.isSelected() ? Constants.COMPILE : Constants.INSTALL;
	}
	
	public void updateLastBuildStatusResults(Long duration, Boolean isSuccess) {
		String status = String.format(
			Constants.STATUS_TEMPLATE,
			new SimpleDateFormat("hh:mm").format(new Date()), 
			new Double(duration) / new Double(1000) + "s",
			(isSuccess) ? Constants.SUCCESS : Constants.FAILED
		);
		
		lastBuildLabel.setText(status);
	}
	
	public void updateLastBuildStatusBuilding() {
		String status = "Building...";
		lastBuildLabel.setText(status);
	}
	
	public void updateLastBuildStatusNoActionYet() {
		if(lastBuildLabel == null) setupLastBuildStatusLabel();
		lastBuildLabel.setText("No Action Yet");
	}
	
	public void setupLastBuildStatusLabel() {
		if(lastBuildLabel == null) {
			lastBuildLabel = new JLabel("");
			lastBuildLabel.setForeground(ColorConstants.DEFAULT_TEXT_COLOR);
			lastBuildLabel.setFont(new Font(Constants.FONT_DIALOG, Font.PLAIN, 12));
			lastBuildLabel.setBounds(22, 565, 450, 35);
			mainPanel.add(lastBuildLabel);
		}
	}
	
	public void openShortcutFile(String filePath) {
		if(filePath == null) return;
		
		try {
			Desktop.getDesktop().open(new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void reenableAllFields() {
		getBuildActionButton().setEnabled(true);
		getReplaceButton().setEnabled(true);
		getGoalToggle().setEnabled(true);
	}
	
	public void refreshDefaults() {
		Defaults defaults = XMLUtil.getInstance().getDefaults();
		getBuildActionButton().setSelected(defaults.getIsBuild());
		getReplaceButton().setSelected(defaults.getIsReplace());
		
		Goal defaultGoal = defaults.getGoal();
		if(defaultGoal != null) {
			getGoalToggle().setSelected((defaults.getGoal().getName().equals(Constants.COMPILE)));
		}
	}
	
	public void refreshHandlers() {
		reenableAllFields();
		refreshDefaults();
		this.filterHandler.refresh();
		refreshShortcutLabels();
	}
	
	public void refreshShortcutLabels() {
		Shortcut shortcut1 = XMLUtil.getInstance().getShortcut1();
		Shortcut shortcut2 = XMLUtil.getInstance().getShortcut2();
		Shortcut shortcut3 = XMLUtil.getInstance().getShortcut3();
		
		String label1 = (!Util.isNullOrEmpty(shortcut1.getLabel())) ? shortcut1.getLabel() : "1";
		String label2 = (!Util.isNullOrEmpty(shortcut2.getLabel())) ? shortcut2.getLabel() : "2";
		String label3 = (!Util.isNullOrEmpty(shortcut3.getLabel())) ? shortcut3.getLabel() : "3";
		
		shortcut1Label.setText(label1);
		shortcut2Label.setText(label2);
		shortcut3Label.setText(label3);
	}
	
	
	/*
	 * Getters and setters of class properties
	 * 
	 */
	
	public BuildLogWindow getBuildLogWindow() {
		return buildLogWindow;
	}

	public RoundTextField getModuleTextField() {
		return moduleTextField;
	}
	
	public JPanel getContentPanel() {
		return contentPanel;
	}

	public CircleButton getSettings() {
		return settingsButton;
	}
	
	public BuildButton getBuildButton() {
		return buildButton;
	}
	
	public JToggleButton getGoalToggle() {
		return goalToggle;
	}
	
	public JToggleButton getBuildActionButton() {
		return buildActionButton;
	}
	
	public JToggleButton getReplaceButton() {
		return replaceButton;
	}
	
	public Boolean getBuildInd() {
		return buildActionButton.isSelected();
	}
	
	public Boolean getReplaceInd() {
		return replaceButton.isSelected();
	}

	public BuildHistoryWindow getBuildHistoryWindow() {
		return buildHistoryWindow;
	}

	public JLabel getLastBuildLabel() {
		return lastBuildLabel;
	}

	public SettingsWindow getSettingsWindow() {
		return settingsWindow;
	}

	public FilterHandler getFilterHandler() {
		return filterHandler;
	}

	public JToggleButton getModuleToggleButton() {
		return moduleToggleButton;
	}

	public AboutWindow getAboutWindow() {
		return aboutWindow;
	}
}

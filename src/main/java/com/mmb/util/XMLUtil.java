/*
 \* Copyright (c) 2020 John Marvie Biglang-awa. Distributed under GNU General Public 
 * License v3.0. This is free software. You can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation.
 */
package com.mmb.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.mmb.models.Defaults;
import com.mmb.models.Directory;
import com.mmb.models.POMDirectory;
import com.mmb.models.Shortcut;
import com.mmb.models.execution.Execution;
import com.mmb.models.filters.Filter;
import com.mmb.models.filters.Goal;
import com.mmb.models.sysenv.SysEnv;
import com.mmb.models.target.Target;
import com.mmb.util.constants.Constants;
import com.mmb.util.constants.XMLConstants;

/**
 * Kudos to this guy for this wonderful XML Sample codes
 * https://stackoverflow.com/questions/7373567/how-to-read-and-write-xml-files
 * 
 * @author 19036-JMEB
 *
 */
public class XMLUtil {
	
	private static Logger logger = Logger.getLogger(XMLUtil.class);
	/*
	 * Essentials when reading and creating XML nodes
	 */
	private Document dom;
	private DocumentBuilderFactory dbf;
	
	/*
	 * Instance to be returned after invocating getInstance method
	 */
	private static XMLUtil xmlUtil;
	
	/*
	 * Data to be saved/read in settings.xml
	 */
	private List<Filter> filterList = new ArrayList<Filter>();
	private List<Execution> executionList = new ArrayList<Execution>();
	private List<SysEnv> sysEnvList = new ArrayList<SysEnv>();
	private List<Target> targetList = new ArrayList<Target>();
	private POMDirectory moduleDirectory = new POMDirectory();
	private Defaults defaults = new Defaults();
	private Shortcut shortcut1 = new Shortcut(XMLConstants.SHORTCUT_1);
	private Shortcut shortcut2 = new Shortcut(XMLConstants.SHORTCUT_2);
	private Shortcut shortcut3 = new Shortcut(XMLConstants.SHORTCUT_3);
	

	/**
	 *	Get synchronized instance of this class anywhere 
	 */
	public static synchronized XMLUtil getInstance() {
		if (xmlUtil == null) {
			xmlUtil = new XMLUtil();
		}

		return xmlUtil;
	}

	/*
	 * Constructor to initiate XMLUtil class
	 */
	public XMLUtil() {
		// Creating an instance of Document Builder Factory
		dbf = DocumentBuilderFactory.newInstance();

		// Read settings.xml and put the data in this class
		readXML();
	}

	
	/**
	 * This method will replace settings.xml with all data saved in this class.
	 * Every setter method is configured to call this method when invocated.
	 */
	public void saveXML() {
		Document dom = null;

		try {
			Element property = null;
			
			/*
			 * Instantiating new XML File
			 */
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.newDocument();
			this.dom = dom;
			
			/*
			 * Root element
			 */
			Element settings = dom.createElement(XMLConstants.SETTINGS);
			
			/*
			 * Child elements of settings
			 */
			Element filters = dom.createElement(XMLConstants.FILTERS);
			Element executions = dom.createElement(XMLConstants.EXECUTIONS);
			Element pomDirectories = dom.createElement(XMLConstants.POMDIRECTORIES);
			Element defaults = dom.createElement(XMLConstants.DEFAULTS);
			Element targets = dom.createElement(XMLConstants.TARGETS);
			Element sysEnvs = dom.createElement(XMLConstants.SYSENVS);
			Element shortcuts = dom.createElement(XMLConstants.SHORTCUTS);
			
			/*
			 * Save filterList
			 */
			for (Filter filter : filterList) {
				Element filterEle = dom.createElement(XMLConstants.FILTER);
				
				if (!Util.isNullOrEmpty(filter.getModulePath().getName())) {
					filterEle.appendChild(createTextElement(XMLConstants.MODULE_PATH, filter.getModulePath().getName()));
				}
				if (!Util.isNullOrEmpty(filter.getAction().getName())) {
					filterEle.appendChild(createTextElement(XMLConstants.ACTION, filter.getAction().getName()));
				}
				if (!Util.isNullOrEmpty(filter.getGoal().getName())) {
					filterEle.appendChild(createTextElement(XMLConstants.GOAL, filter.getGoal().getName()));
				}
				if (!Util.isNullOrEmpty(filter.getOpts().getName())) {
					filterEle.appendChild(createTextElement(XMLConstants.OPTS, filter.getOpts().getName()));
				}
				filterEle.appendChild(createTextElement(XMLConstants.ISCLEAN, filter.getIsClean().toString()));
				filters.appendChild(filterEle);
			}
			
			/*
			 * Save executionList
			 */
			for (Execution execution : executionList) {
				Element executionEle = dom.createElement(XMLConstants.EXECUTION);

				if (!Util.isNullOrEmpty(execution.getModulePath().getName())) {
					executionEle.appendChild(createTextElement(XMLConstants.MODULE_PATH, execution.getModulePath().getName()));
				}

				if (!Util.isNullOrEmpty(execution.getAfterCommand().getName())) {
					property = dom.createElement(XMLConstants.AFTER_COMMAND);

					String[] command = execution.getAfterCommand().getCommand();
					for (int i = 0; i < command.length; i++) {
						property.appendChild(createTextElement(XMLConstants.ROW, command[i]));
					}
					
					executionEle.appendChild(property);
				}

				if (!Util.isNullOrEmpty(execution.getBeforeCommand().getName())) {
					property = dom.createElement(XMLConstants.BEFORE_COMMAND);

					String[] command = execution.getBeforeCommand().getCommand();
					for (int i = 0; i < command.length; i++) {
						property.appendChild(createTextElement(XMLConstants.ROW, command[i]));
					}
					executionEle.appendChild(property);
				}

				executions.appendChild(executionEle);
			}

			/*
			 * Save pomDirectories
			 */
			pomDirectories.appendChild(createPOMDirectory(this.moduleDirectory));
			
			/*
			 * Save defaults
			 */
			defaults.appendChild(createTextElement(XMLConstants.PROJECT_PATH, this.defaults.getProjectPath()));
			defaults.appendChild(createTextElement(XMLConstants.BUILD_PARAMS, this.defaults.getBuildParameters()));
			defaults.appendChild(createTextElement(XMLConstants.MAVEN_OPTS, this.defaults.getMavenOpts()));
			defaults.appendChild(createTextElement(XMLConstants.ISBUILD, this.defaults.getIsBuild().toString()));
			defaults.appendChild(createTextElement(XMLConstants.ISREPLACE, this.defaults.getIsReplace().toString()));
			defaults.appendChild(createTextElement(XMLConstants.GOAL, this.defaults.getGoal().getName()));
			
			/*
			 * Save targets
			 */
			for (Target target : targetList) {
				Element targetEle = dom.createElement(XMLConstants.TARGET);
				targetEle.appendChild(createTextElement(XMLConstants.LABEL, target.getLabel()));
				targetEle.appendChild(createTextElement(XMLConstants.DIRECTORY, target.getDirectory().getName()));
				targets.appendChild(targetEle);
			}
			
			/*
			 * Save System environment variables
			 */
			for (SysEnv sysEnv : sysEnvList) {
				Element targetEle = dom.createElement(XMLConstants.SYSENV);
				targetEle.appendChild(createTextElement(XMLConstants.LABEL, sysEnv.getLabel()));
				targetEle.appendChild(createTextElement(XMLConstants.DIRECTORY, sysEnv.getDirectory().getName()));
				sysEnvs.appendChild(targetEle);
			}

			/*
			 * Save shortcuts
			 */
			// Shortcut 1
			property = dom.createElement(XMLConstants.SHORTCUT);
			
			property.appendChild(createTextElement(XMLConstants.NUMBER, shortcut1.getNumber()));
			if(!Util.isNullOrEmpty(shortcut1.getLabel())) {
				property.appendChild(createTextElement(XMLConstants.LABEL, shortcut1.getLabel()));
			}
			if(!Util.isNullOrEmpty(shortcut1.getFile())) {
				property.appendChild(createTextElement(XMLConstants.FILE, shortcut1.getFile()));
			}
			shortcuts.appendChild(property);
			
			
			// Shortcut 2
			property = dom.createElement(XMLConstants.SHORTCUT);
			
			property.appendChild(createTextElement(XMLConstants.NUMBER, shortcut2.getNumber()));
			if(!Util.isNullOrEmpty(shortcut2.getLabel())) {
				property.appendChild(createTextElement(XMLConstants.LABEL, shortcut1.getLabel()));
			}
			if(!Util.isNullOrEmpty(shortcut2.getFile())) {
				property.appendChild(createTextElement(XMLConstants.FILE, shortcut1.getFile()));
			}
			
			shortcuts.appendChild(property);
			
			
			// Shortcut 3
			property = dom.createElement(XMLConstants.SHORTCUT);
			
			property.appendChild(createTextElement(XMLConstants.NUMBER, shortcut3.getNumber()));
			if(!Util.isNullOrEmpty(shortcut3.getLabel())) {
				property.appendChild(createTextElement(XMLConstants.LABEL, shortcut3.getLabel()));
			}
			if(!Util.isNullOrEmpty(shortcut3.getFile())) {
				property.appendChild(createTextElement(XMLConstants.FILE, shortcut3.getFile()));
			}
			
			shortcuts.appendChild(property);
			
			
			settings.appendChild(filters);
			settings.appendChild(executions);
			settings.appendChild(pomDirectories);
			settings.appendChild(targets);
			settings.appendChild(defaults);
			settings.appendChild(sysEnvs);
			settings.appendChild(shortcuts);
			dom.appendChild(settings);

			this.dom = dom;
			
			Transformer tr = TransformerFactory.newInstance().newTransformer();
			tr.setOutputProperty(OutputKeys.INDENT, "yes");
			tr.setOutputProperty(OutputKeys.METHOD, XMLConstants.XML);
			tr.setOutputProperty(OutputKeys.ENCODING, XMLConstants.UTF_8);
			tr.setOutputProperty(XMLConstants.INDENT_AMOUNT, "4");

			// send DOM to file
			tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(getXMLFilePath())));
			
			logger.debug("XMLUtil has successfully saved data: ");
			logData();
		} catch (ParserConfigurationException pce) {
			logger.error("ParserConfigurationException occurred when reading the file: " + pce.getMessage(), pce);
		} catch (TransformerException te) {
			logger.error("TransformerException occurred when reading the file: " + te.getMessage(), te);
		} catch (IOException ioe) {
			logger.error("TransformerException occurred when reading the file: " + ioe.getMessage(), ioe);
		}
	}
	
	/**
	 * Method to copy all of the data from settings.xml to this instance
	 */
	public void readXML() {
		File file = new File(getXMLFilePath());
		if (!file.exists()) {
			return;
		}

		try {
			// use the factory to take an instance of the document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			if (this.dom == null) {
				dom = db.parse(getXMLFilePath());
			}

			Element settings = dom.getDocumentElement();

			/*
			 * Read filter
			 */
			List<Filter> filterList = new ArrayList<Filter>();
			NodeList filtersNodeList = settings.getElementsByTagName(XMLConstants.FILTERS);
			if (filtersNodeList != null && filtersNodeList.getLength() > 0 && filtersNodeList.item(0).hasChildNodes()) {
				NodeList filterNodeList = filtersNodeList.item(0).getChildNodes();
				for (int i = 0; i < filterNodeList.getLength(); i++) {
					Node filterNode = filterNodeList.item(i);

					if (filterNode.getNodeType() == Node.ELEMENT_NODE) {
						Filter filter = new Filter();
						NodeList filterPropertiesNodeList = filterNodeList.item(i).getChildNodes();

						for (int j = 0; j < filterPropertiesNodeList.getLength(); j++) {
							Node filterPropertiesNode = filterPropertiesNodeList.item(j);
							if (filterPropertiesNode.getNodeType() == Node.ELEMENT_NODE) {
								String nodeName = filterPropertiesNode.getNodeName();
								String nodeValue = filterPropertiesNode.getTextContent();
								if (nodeName == XMLConstants.MODULE_PATH) {
									filter.setModulePath(nodeValue);
								} else if (nodeName == XMLConstants.ACTION) {
									filter.setAction(nodeValue);
								} else if (nodeName == XMLConstants.GOAL) {
									filter.setGoal(nodeValue);
								} else if (nodeName == XMLConstants.OPTS) {
									filter.setOpts(nodeValue);
								} else if (nodeName == XMLConstants.ISCLEAN) {
									filter.setIsClean(nodeValue);
								}
							}
						}
						filterList.add(filter);
					}
				}
				this.filterList = filterList;
			}

			/*
			 * Read execution
			 */
			List<Execution> executionList = new ArrayList<Execution>();
			NodeList executionsNodeList = settings.getElementsByTagName(XMLConstants.EXECUTIONS);
			if (executionsNodeList != null && executionsNodeList.getLength() > 0
					&& executionsNodeList.item(0).hasChildNodes()) {
				NodeList executionNodeList = executionsNodeList.item(0).getChildNodes();
				for (int i = 0; i < executionNodeList.getLength(); i++) {
					Node executionNode = executionNodeList.item(i);

					if (executionNode.getNodeType() == Node.ELEMENT_NODE) {
						Execution execution = new Execution();
						NodeList executionPropertyNodeList = executionNodeList.item(i).getChildNodes();

						for (int j = 0; j < executionPropertyNodeList.getLength(); j++) {
							Node executionPropertyNode = executionPropertyNodeList.item(j);
							if (executionPropertyNode.getNodeType() == Node.ELEMENT_NODE) {
								String nodeName = executionPropertyNode.getNodeName();
								String nodeValue = executionPropertyNode.getTextContent();

								if (nodeName == XMLConstants.MODULE_PATH) {
									execution.setModulePath(nodeValue);
									
								} else if (nodeName == XMLConstants.BEFORE_COMMAND) {
									List<String> commandList = new ArrayList<String>();
									NodeList rowNodeList = executionPropertyNode.getChildNodes();
									for (int k = 0; k < rowNodeList.getLength(); k++) {
										Node rowNode = rowNodeList.item(k);
										if (rowNode.getNodeType() == Node.ELEMENT_NODE) {
											commandList.add(rowNode.getTextContent());
										}
									}
									execution.getBeforeCommand().setCommand(commandList);
									
								} else if (nodeName == XMLConstants.AFTER_COMMAND) {
									List<String> commandList = new ArrayList<String>();
									NodeList rowNodeList = executionPropertyNode.getChildNodes();
									for (int k = 0; k < rowNodeList.getLength(); k++) {
										Node rowNode = rowNodeList.item(k);
										if (rowNode.getNodeType() == Node.ELEMENT_NODE) {
											commandList.add(rowNode.getTextContent());
										}
									}
									execution.getAfterCommand().setCommand(commandList);
								}
							}
						}
						executionList.add(execution);
					}
				}
				this.executionList = executionList;
			}

			/*
			 * Read POMDirectory
			 */
			POMDirectory moduleDirectory = null;
			NodeList pomDirsNodeList = settings.getElementsByTagName(XMLConstants.POMDIRECTORIES);
			if (pomDirsNodeList != null && pomDirsNodeList.getLength() > 0 && pomDirsNodeList.item(0).hasChildNodes()) {
				NodeList pomDirNodeList = pomDirsNodeList.item(0).getChildNodes();
				
				/*
					 The moduleDirectory is expected to have only one root, so
					 it will be safe to replace moduleDirectory everytime cause
					 there will be only one iteration for this (Except text nodes
					 which will be skipped
				*/
				for (int i = 0; i < pomDirNodeList.getLength(); i++) {
					Node pomDirNode = pomDirNodeList.item(i);
					if (pomDirNode.getNodeType() == Node.ELEMENT_NODE && pomDirNode.getNodeName() == XMLConstants.POMDIRECTORY) {
						moduleDirectory = readPOMDirectory(pomDirNode.getChildNodes());
					}
				}
				this.moduleDirectory = moduleDirectory;
			}
			
			/*
			 * Read targets
			 */
			List<Target> targetList = new ArrayList<Target>();
			NodeList targetsNodeList = settings.getElementsByTagName(XMLConstants.TARGETS);
			if (targetsNodeList != null && targetsNodeList.getLength() > 0 && targetsNodeList.item(0).hasChildNodes()) {
				NodeList targetNodeList = targetsNodeList.item(0).getChildNodes();
				for (int i = 0; i < targetNodeList.getLength(); i++) {
					Node targetNode = targetNodeList.item(i);

					if (targetNode.getNodeType() == Node.ELEMENT_NODE) {
						Target target = new Target();
						NodeList filterPropertiesNodeList = targetNodeList.item(i).getChildNodes();

						for (int j = 0; j < filterPropertiesNodeList.getLength(); j++) {
							Node filterPropertiesNode = filterPropertiesNodeList.item(j);
							if (filterPropertiesNode.getNodeType() == Node.ELEMENT_NODE) {
								String nodeName = filterPropertiesNode.getNodeName();
								String nodeValue = filterPropertiesNode.getTextContent();
								if (nodeName == XMLConstants.LABEL) {
									target.setLabel(nodeValue);
								}else if (nodeName == XMLConstants.DIRECTORY) {
									target.setDirectory(new Directory(nodeValue));
								}
							}
						}
						targetList.add(target);
					}
				}
				this.targetList = targetList;
			}
			
			/*
			 * Read System environment variables
			 */
			List<SysEnv> sysEnvList = new ArrayList<SysEnv>();
			NodeList sysEnvsNodeList = settings.getElementsByTagName(XMLConstants.SYSENVS);
			if (sysEnvsNodeList != null && sysEnvsNodeList.getLength() > 0 && sysEnvsNodeList.item(0).hasChildNodes()) {
				NodeList sysEnvNodeList = sysEnvsNodeList.item(0).getChildNodes();
				for (int i = 0; i < sysEnvNodeList.getLength(); i++) {
					Node sysEnvNode = sysEnvNodeList.item(i);

					if (sysEnvNode.getNodeType() == Node.ELEMENT_NODE) {
						SysEnv sysEnv = new SysEnv();
						NodeList sysEnvPropertiesNodeList = sysEnvNodeList.item(i).getChildNodes();

						for (int j = 0; j < sysEnvPropertiesNodeList.getLength(); j++) {
							Node sysEnvPropertiesNode = sysEnvPropertiesNodeList.item(j);
							if (sysEnvPropertiesNode.getNodeType() == Node.ELEMENT_NODE) {
								String nodeName = sysEnvPropertiesNode.getNodeName();
								String nodeValue = sysEnvPropertiesNode.getTextContent();
								if (nodeName == XMLConstants.LABEL) {
									sysEnv.setLabel(nodeValue);
								}else if (nodeName == XMLConstants.DIRECTORY) {
									sysEnv.setDirectory(new Directory(nodeValue));
								}
							}
						}
						sysEnvList.add(sysEnv);
					}
				}
				this.sysEnvList = sysEnvList;
			}
			
			/*
			 * Read defaults
			 */
			Defaults defaults = new Defaults();
			NodeList defaultsNodeList = settings.getElementsByTagName(XMLConstants.DEFAULTS);
			if(defaultsNodeList != null && defaultsNodeList.getLength() > 0) {
				NodeList defaultsItemNodeList = defaultsNodeList.item(0).getChildNodes();
				for(int i = 0; i < defaultsItemNodeList.getLength(); i++) {
					Node node = defaultsItemNodeList.item(i);
					if(node.getNodeType() == Node.ELEMENT_NODE) {
						if(node.getNodeName() == XMLConstants.PROJECT_PATH) {
							defaults.setProjectPath(node.getTextContent());
						}else if(node.getNodeName() == XMLConstants.BUILD_PARAMS) {
							defaults.setBuildParameters(node.getTextContent());
						}else if(node.getNodeName() == XMLConstants.MAVEN_OPTS) {
							defaults.setMavenOpts(node.getTextContent());
						}else if(node.getNodeName() == XMLConstants.ISBUILD) {
							defaults.setIsBuild(Boolean.parseBoolean(node.getTextContent()));
						}else if(node.getNodeName() == XMLConstants.ISREPLACE) {
							defaults.setIsReplace(Boolean.parseBoolean(node.getTextContent()));
						}else if(node.getNodeName() == XMLConstants.GOAL) {
							defaults.setGoal(new Goal(node.getTextContent()));
						}
					}
				}
				this.defaults = defaults;
			}
			
			/*
			 * Read shortcuts
			 */
			NodeList shortcutsNodeList = settings.getElementsByTagName(XMLConstants.SHORTCUTS);
			if(shortcutsNodeList != null &&  shortcutsNodeList.getLength() > 0) {
				for(int i = 0; i < shortcutsNodeList.getLength(); i++) {
					Node shortcutsNode = shortcutsNodeList.item(i);
					if(shortcutsNode.getNodeType() == Node.ELEMENT_NODE) {
						NodeList shortcutNodeList = shortcutsNode.getChildNodes();
						
						for(int j = 0; j < shortcutNodeList.getLength(); j++) {
							Node shortcutPropertyNode = shortcutNodeList.item(j);
							if(shortcutPropertyNode.getNodeType() == Node.ELEMENT_NODE) {
								NodeList shortcutPropertiesNodeList = shortcutPropertyNode.getChildNodes();
								Shortcut shortcut = null;
								for(int k = 0; k < shortcutPropertiesNodeList.getLength(); k++) {
									Node shortcutPropertyNodes = shortcutPropertiesNodeList.item(k);
									
									if(shortcutPropertyNodes.getNodeType() == Node.ELEMENT_NODE) {
										if(shortcutPropertyNodes.getNodeName() == XMLConstants.NUMBER) {
											if(shortcutPropertyNodes.getTextContent().equals(XMLConstants.SHORTCUT_1)) {
												shortcut = this.shortcut1;
											}else if(shortcutPropertyNodes.getTextContent().equals(XMLConstants.SHORTCUT_2)) {
												shortcut = this.shortcut2;
											}else if(shortcutPropertyNodes.getTextContent().equals(XMLConstants.SHORTCUT_3)) {
												shortcut = this.shortcut3;
											}
										}else if(shortcutPropertyNodes.getNodeName() == XMLConstants.LABEL) {
											shortcut.setLabel(shortcutPropertyNodes.getTextContent());
										}else if(shortcutPropertyNodes.getNodeName() == XMLConstants.FILE) {
											shortcut.setFile(shortcutPropertyNodes.getTextContent());
										}
									}
								}
							}
						}
					}
				}
			}
			
			
			logger.debug("XMLUtil has successfully read XML data");
			logData();
		} catch (ParserConfigurationException pce) {
			logger.error("ParserConfigurationException occurred when reading XMLData" + pce.getMessage(), pce);
		} catch (SAXException se) {
			logger.error("ParserConfigurationException occurred when reading XMLData" + se.getMessage(), se);
		} catch (IOException ioe) {
			logger.error("ParserConfigurationException occurred when reading XMLData" + ioe.getMessage(), ioe);
		}
	}

	/**
	 *	Get XML file in user directory
	 */
	private static String getXMLFilePath() {
		return System.getProperty(XMLConstants.USER_DIR) + File.separator + Constants.SETTINGS_XML;
	}

	/**
	 * Recursively create POMDirectory accordingly.
	 * As long as a POMDirectory has children, XMLUtil will create a new
	 * node for the children and a new node for the new POMDirectory
	 * @param pomDirectory
	 * @return
	 */
	public Element createPOMDirectory(POMDirectory pomDirectory) {
		Element element = dom.createElement(XMLConstants.POMDIRECTORY);
		Element directory = dom.createElement(XMLConstants.DIRECTORY);
		Element children = dom.createElement(XMLConstants.CHILDREN);

		directory.appendChild(dom.createTextNode(pomDirectory.getDirectory()));
		for (POMDirectory child : pomDirectory.getChildren()) {
			children.appendChild(createPOMDirectory(child));
			element.appendChild(children);
		}

		element.appendChild(directory);
		return element;
	}

	/**
	 * Read the XML File for the POMDirectory. The reading of the children
	 * is done recursively to accomodate dynamic rendering of children.
	 * @param nodeChildren
	 * @return
	 */
	public POMDirectory readPOMDirectory(NodeList nodeChildren) {
		POMDirectory pomDirectory = new POMDirectory();
		for (int j = 0; j < nodeChildren.getLength(); j++) {
			Node pomDirPropertiesNode = nodeChildren.item(j);
			if (pomDirPropertiesNode.getNodeType() == Node.ELEMENT_NODE) {
				
				if (pomDirPropertiesNode.getNodeName() == XMLConstants.DIRECTORY) {
					pomDirectory.setDirectory(pomDirPropertiesNode.getTextContent());
				} else if (pomDirPropertiesNode.getNodeName() == XMLConstants.CHILDREN) {
					NodeList pomDirChildren = pomDirPropertiesNode.getChildNodes(); //POMDirectory
					
					for (int k = 0; k < pomDirChildren.getLength(); k++) {
						Node children = pomDirChildren.item(k);
						if (children.getNodeType() == Node.ELEMENT_NODE) {
							pomDirectory.addChild(readPOMDirectory(children.getChildNodes()));
						}
					}
				}
			}
		}
		
		return pomDirectory ;
	}
	
	/**
	 * Create an element with a textnode
	 * @param nodeName
	 * @param value
	 * @return
	 */
	public Element createTextElement(String nodeName, String value) {
		Element property = dom.createElement(nodeName);
		property.appendChild(dom.createTextNode(value));
		return property;
	}
	
	public void logData() {
		logger.debug("filters: " + filterList.toString());
		logger.debug("execitons: " + executionList.toString());
		logger.debug("pomDirectories: " + moduleDirectory.toString());
		logger.debug("targets: " + targetList.toString());
		logger.debug("defaults: " + defaults.toString());
		logger.debug("sysEnvs: " + sysEnvList.toString());
		logger.debug("shortcut 1: " + shortcut1.toString());
		logger.debug("shortcut 2: " + shortcut2.toString());
		logger.debug("shortcut 3: " + shortcut3.toString());
	}
	
	/*
	 * Getters
	 */
	public List<Filter> getFilterList() {
		return filterList;
	}

	public List<Execution> getExecutionList() {
		return executionList;
	}

	public POMDirectory getModuleDirectory() {
		return moduleDirectory;
	}

	public List<Target> getTargetList() {
		return targetList;
	}
	
	public Defaults getDefaults() {
		return defaults;
	}

	public List<SysEnv> getSysEnvList() {
		return sysEnvList;
	}
	
	public Shortcut getShortcut1() {
		return shortcut1;
	}

	public Shortcut getShortcut2() {
		return shortcut2;
	}

	public Shortcut getShortcut3() {
		return shortcut3;
	}
	
	/*
	 * Setters are configured to invoke saveXML
	 */
	public void setFilterList(List<Filter> filterList) {
		this.filterList = filterList;
		saveXML();
	}

	public void setExecutionList(List<Execution> executionList) {
		this.executionList = executionList;
		saveXML();
	}
	
	public void setGeneralSettings(POMDirectory moduleDirectory, Defaults defaults, List<Target> targetList, List<SysEnv> sysEnvList) {
		this.moduleDirectory = moduleDirectory;
		this.defaults = defaults;
		this.targetList = targetList;
		this.sysEnvList = sysEnvList;
		saveXML();
	}
	
}

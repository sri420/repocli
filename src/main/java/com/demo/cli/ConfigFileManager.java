package com.demo.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigFileManager {
	
	public static Properties props=new Properties();

	public static void main(String[] args) throws Exception {
		ConfigFileManager.loadConfiguration();
		ConfigFileManager.showConfig();
		ConfigFileManager.updateConfig(args[0], args[1]);
		ConfigFileManager.showConfig();
	}

	public static void showConfig(){
		ConfigFileManager.getConfiguration().list(System.out);
		
	}
	
	public static void updateConfig(String property,String value){
		ConfigFileManager.getConfiguration().setProperty(property, value);
		ConfigFileManager.saveConfig();
	}
	public static void saveConfig(){
		String path = System.getProperty(CLIConstants.USER_HOME_DIR);
		path += File.separator + CLIConstants.SETTINGS_FOLDER;
		File customDir = new File(path);

		if (!customDir.exists()) {
			if (customDir.mkdirs()) {
			} else {
			}
		}

		path += File.separator + CLIConstants.SETTINGS_FILE;
		customDir = new File(path);
		if (!customDir.exists()) {
		}
		FileOutputStream out;
		try {
			out = new FileOutputStream(customDir);
			props.store(out,"demo");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		

	}
	public  static void loadConfiguration() throws FileNotFoundException, IOException{
		String path = System.getProperty(CLIConstants.USER_HOME_DIR);
		path += File.separator + CLIConstants.SETTINGS_FOLDER;
		File customDir = new File(path);
		if (customDir.exists()) {
			path += File.separator + CLIConstants.SETTINGS_FILE;
			customDir = new File(path);
			if (customDir.exists()) {
				props.load(new FileInputStream(customDir));
			} else {
				addPropsToConfigFile(props);
			}
		}else{
			addPropsToConfigFile(props);
		}
	}
	
	public static  Properties getConfiguration(){
		return props;
	}
	public static boolean shouldPromptUserForCredentials() throws FileNotFoundException, IOException {
		String path = System.getProperty(CLIConstants.USER_HOME_DIR);
		path += File.separator + CLIConstants.SETTINGS_FOLDER;
		File customDir = new File(path);

		if (!customDir.exists()) {
			if (customDir.mkdirs()) {
			} else {
		}
			return true;
		}

		path += File.separator + CLIConstants.SETTINGS_FILE;
		customDir = new File(path);
		if (!customDir.exists()) {
			return true;
		}

		Properties props = new Properties();
		props.load(new FileInputStream(customDir));
		String username = (String) props.get("username");
		String password = (String) props.get("password");
		if (null == username || username.trim().length() <= 0 || null == password || password.trim().length() <= 0) {
			return true;
		}
		return false;

	}

	public  static void addPropsToConfigFile(Properties properties) throws IOException {
		props.setProperty("protocol", CLIConstants.PROTOCOL);
		props.setProperty("apiEndpoint",CLIConstants.API_ENDPOINT);
		props.setProperty("branchToClone", CLIConstants.BRANCH_TO_CLONE);
		props.setProperty("strArchetypeGroupId", CLIConstants.ARCHETYPE_GROUPID);
		props.setProperty("strArchetypeArtifactId", CLIConstants.ARCHETYPE_ARCHETYPEID);
		props.setProperty("strArcheTypeVersion", CLIConstants.ARCHETYPE_ARCHETYPEVERSION);
		props.setProperty("description", CLIConstants.DESCRIPTION);
		props.setProperty("version", CLIConstants.version);
		props.setProperty("bitbucketServerPushContextPath", CLIConstants.BITBUCKET_SERVER_CONTEXT_PATH);
		props.setProperty("configRepo", CLIConstants.CONFIGREPO);
		saveConfig();
		
		
	}

	public  String getValueForKey(String key) throws IOException {
		String retValue = null;

		Properties prop = new Properties();
		prop.load(new FileInputStream(getConfigPath()));
		if (null != prop) {
			retValue = (String) prop.get(key);
		}
		return retValue;

	}

	private  String getConfigPath() {
		String path = System.getProperty(CLIConstants.USER_HOME_DIR);
		path += File.separator + CLIConstants.SETTINGS_FOLDER;
		path += File.separator + CLIConstants.SETTINGS_FILE;
		return path;
	}
}

package repoclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class ConfigFileManager {

	public static void main(String[] args) throws Exception {

		if (ConfigFileManager.shouldPromptUserForCredentials()) {
			System.out.println("Prompting for credentials");
			Properties prop = new Properties();
			prop.put("username", "bsridhar");// Test Data
			prop.put("password", "MjYxNTA2MjQzOTI3OhWGI9RPvwGhvtoQQwGZDVujVemE");// Test Data
			prop.put("api_endpoint", RepoMain.API_ENDPOINT);// Test Data
			prop.put("host_port", RepoMain.HOST_PORT); // Test Data
			ConfigFileManager.addPropsToConfigFile(prop);
		} else {
			System.out.println("Config Already Exists!");
		}

	}

	public static boolean shouldPromptUserForCredentials() throws FileNotFoundException, IOException {
		String path = System.getProperty("user.home");
		path += File.separator + ".repoclient";
		File customDir = new File(path);

		if (!customDir.exists()) {
			System.out.println(customDir + " Does  Not Exists");
			if (customDir.mkdirs()) {
				System.out.println(customDir + " was created");
			} else {
				System.out.println(customDir + " was not created");
			}
			return true;
		}

		path += File.separator + "repoconfig.properties";
		customDir = new File(path);
		if (!customDir.exists()) {
			System.out.println("Folder Exists but " + customDir + " Does  Not Exists");
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

	public static boolean addPropsToConfigFile(Properties properties) throws IOException {
		OutputStream output = new FileOutputStream(getConfigPath());
		properties.store(output, null);
		return true;
	}

	public static String getValueForKey(String key) throws IOException {
		String retValue = null;

		Properties prop = new Properties();
		prop.load(new FileInputStream(getConfigPath()));
		if (null != prop) {
			retValue = (String) prop.get(key);
		}
		return retValue;

	}

	private static String getConfigPath() {
		String path = System.getProperty("user.home");
		path += File.separator + ".repoclient";
		path += File.separator + "repoconfig.properties";
		return path;
	}
}

package com.demo.cli;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import okhttp3.Response;

public class RepoMain {


	public static final String SCMID = "git";

	public static void main(String[] args) throws Exception {
		ConfigFileManager configFileManager=new ConfigFileManager();
		
		Properties props=configFileManager.getConfiguration();
		
		String url = props.getProperty("protocol") + props.getProperty("hostPort") + props.getProperty("apiEndpoint");
		String strGitRemoteUrl = "%s@%s/scm/home/%s.git";
		String mvnFormattedString = null;


		Console console = System.console();
		String s = null;
		String gitAdd = "git add .";
		String message = "Initial";
		String userName = null;
		String encodedAuthString = null;

		// Prompt for Parameters required for creating Maven Project creation
		// from archetype
		System.out.println();
		System.out.println();
		System.out.println();
		String strGroupId = console.readLine("Enter the GroupId  for new new project: ");
		System.out.println();
		String strArtifactId = console.readLine("Enter the ArtifactId  for new new project: ");
		System.out.println();
		String strPackageName = strGroupId + "." + strArtifactId;// "com.demo.bp.services.hello-service";//console.readLine("Enter
																	// the
																	// PackageName
																	// for new
																	// new
																	// project:
																	// ");
		System.out.println();
		String strDescription = "description of service";// console.readLine("Enter
															// the Description
															// for new new
															// project: ");
		System.out.println();
		String strVersion = "1.0.0-SNAPSHOT";// console.readLine("Enter the
												// Version for new new project:
												// ");
		System.out.println();
		// Create the Repository in Bitbucket Server
		System.out.println();
		System.out.println();
		System.out.println("Creating Remote Repository...");
		System.out.println();
		System.out.println();
		try {
			// System.out.println(" URL is:" + url);
			Response response = RepositoryManager.createRepository(
					new RepoCreationRequest(strArtifactId, RepoMain.SCMID, true), url, "Basic " + encodedAuthString);
			int code = response.code();
			// System.out.println("***code is:" + code);
			if (code == 401) {
				Properties prop = new Properties();
				prop.setProperty("username", "");
				prop.put("password", "");
				prop.put("api_endpoint", props.getProperty("apiEndpoint"));
				prop.put("host_port", props.getProperty("hostPort"));
				configFileManager.addPropsToConfigFile(prop);
				System.out.println();
				System.out.println();
				System.out.println("!!!!!!! Authentication Failed. Please try again.!!!!!");
				System.out.println();
				System.out.println();
				System.exit(0);
			}
			if (code == 404) {
				System.out.println();
				System.out.println();
				System.out.println("!!!!!!! URL Not Found. Please check Bitbucket Server URL is correct: " + url);
				System.exit(0);
			}

			System.out.println();
			System.out.println();
			System.out.println("**********Successfully Created Remote Repository:  " + strArtifactId);
			System.out.println();
			System.out.println();
		} catch (Exception e) {
			System.out.println("****Exception: " + e);
			System.exit(0);
		}

		// Execute the Maven Creation , Git Initialization,Git Local Commit and
		// Pushing to Remote Repository
		String strFormattedGitRemoteUrl = String.format(strGitRemoteUrl, props.getProperty("protocol"), props.getProperty("hostPort"), strArtifactId);
		String finalStr = String.format(
				"cmd.exe /c  %s & cd %s & git init & %s  & git commit -m %s & git remote add origin  %s & git checkout -B integration & git push -u origin integration ",
				mvnFormattedString, strArtifactId, gitAdd, message, strFormattedGitRemoteUrl);

		System.out.println("finalStr:" + finalStr);
		System.out.println();
		System.out.println(
				"***Creating Project From Template and Pushing the code to Remote Repository: " + strArtifactId);
		System.out.println();
		System.out.println();
		Process finalProcess = Runtime.getRuntime().exec(finalStr);

		BufferedReader stdInputfinalProcess = new BufferedReader(new InputStreamReader(finalProcess.getInputStream()));
		BufferedReader stdErrorfinalProcess = new BufferedReader(new InputStreamReader(finalProcess.getErrorStream()));
		displayCommndInput(stdInputfinalProcess, "Command");
		displayCommndInput(stdErrorfinalProcess, "Error");

	}

	public static void displayCommndInput(BufferedReader reader, String type) throws IOException {
		String s = null;
		System.out.println(type + " Output:\n");
		while ((s = reader.readLine()) != null) {
			System.out.println(s);
		}
	}
}

package com.demo.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProjectCreator {

	public static void  main(String[] args) {
		
		String strGroupId=args[0];
		String strArtifactId=args[1];
		System.out.println();
		System.out.println();
		System.out.println("************Project Being Created From Template with following values");
		System.out.println();
		System.out.println();
		System.out.println("GroupId:" + strGroupId);
		System.out.println();
		System.out.println("ArtifactId:" + strArtifactId);
		createProject("org.apache.maven.archetypes","maven-archetype-j2ee-simple","1.0",strGroupId,strArtifactId);
		System.out.println();
		System.out.println();
		System.out.println("Project Created Successfully. Import the project folder : " + System.getProperty("user.dir") + File.separator + strArtifactId + " into IDE");
		
	}
	public static void displayInput(BufferedReader reader, String type) throws IOException {
		String s = null;
		while ((s = reader.readLine()) != null) {
			System.out.println(s);
		}
	}
	
	public static void createProject(String strArchetypeGroupId,String strArchetypeArtifactId,String strArcheTypeVersion,String strGroupId,String strArtifactId){
		String mvnFormatString = "mvn archetype:generate -DarchetypeGroupId=%s -DarchetypeArtifactId=%s -DarchetypeVersion=%s -DgroupId=%s -DartifactId=%s -Dversion=%s -Ddescription=%s -DinteractiveMode=false ";
		String mvnFormattedString = String.format(mvnFormatString, strArchetypeGroupId,strArchetypeArtifactId,strArcheTypeVersion,strGroupId, strArtifactId,"1.0.0-SNAPSHOT","\"Simple Service\"");
		
		try {
			//System.out.println("mvnFormattedString:" );
			//System.out.println();
			//System.out.println();
			//System.out.println(mvnFormattedString);
			Process finalProcess = Runtime.getRuntime().exec("cmd.exe /c  " + mvnFormattedString);
			BufferedReader stdInputfinalProcess = new BufferedReader(new InputStreamReader(finalProcess.getInputStream()));
			BufferedReader stdErrorfinalProcess = new BufferedReader(new InputStreamReader(finalProcess.getErrorStream()));
			System.out.println();
			System.out.println();
			System.out.println("Project Being Created in folder:" + System.getProperty("user.dir") + File.separator + strArtifactId );
			displayInput(stdInputfinalProcess, "Input");
			displayInput(stdErrorfinalProcess, "Error");
		} catch (IOException e) {
			System.out.println("Error Occured:" + e);
			System.exit(0);
		}catch(Exception e){
			System.out.println("Error Occured:" + e);
			System.exit(0);
		}
		
		
	}
}

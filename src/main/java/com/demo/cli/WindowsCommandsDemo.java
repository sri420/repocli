package com.demo.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WindowsCommandsDemo {

	public static void main(String[] args) {
		String finalStr = String.format(
				"cmd.exe /c  mkdir %s & cd %s & dir ",
				args[0], args[0]);

		System.out.println("finalStr:" + finalStr);
		System.out.println();
		//System.out.println(
			//	"***Creating Project From Template and Pushing the code to Remote Repository: " + strArtifactId);
		System.out.println();
		System.out.println();
		Process finalProcess;
		try {
			finalProcess = Runtime.getRuntime().exec(finalStr);
			BufferedReader stdInputfinalProcess = new BufferedReader(new InputStreamReader(finalProcess.getInputStream()));
			BufferedReader stdErrorfinalProcess = new BufferedReader(new InputStreamReader(finalProcess.getErrorStream()));
			displayCommndInput(stdInputfinalProcess, "Command");
			displayCommndInput(stdErrorfinalProcess, "Error");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

	}
	public static void displayCommndInput(BufferedReader reader, String type) throws IOException {
		String s = null;
		System.out.println(type + " Output:\n");
		while ((s = reader.readLine()) != null) {
			System.out.println(s);
		}
	}
}

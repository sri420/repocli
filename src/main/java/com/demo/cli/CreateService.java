package com.demo.cli;

import java.util.Properties;

public class CreateService {

	public static void main(String[] args) {

		/*
		 * Maven Project Creation strArchetypeGroupId strArchetypeArtifactId
		 * strArcheTypeVersion
		 * 
		 * strGroupId strArtifactId
		 * 
		 * Repository Creation and populating with Maven Project Code
		 * repoFolderName username password protocol hostPort apiEndpoint
		 * 
		 * Check Out Config-Repo and Update configuration Files protocol
		 * bostPort repoName branchToClone username password
		 * 
		 * 
		 * Parameters From User Input username password
		 * 
		 * strGroupId strArtifactId
		 * 
		 * Zuul Custom Route Endpoint
		 * 
		 * Parameters from Settings Configuration protocol hostPort apiEndpoint
		 * branchToClone strArchetypeGroupId strArchetypeArtifactId
		 * strArcheTypeVersion
		 * 
		 * 
		 */

		try {
			String strGroupId = "com.demo.services";
			String strArtifactId = "pppp";

			String username = "bsridhar";
			String password = "Kajol143$";

			
			ConfigFileManager.loadConfiguration();
			Properties props=ConfigFileManager.getConfiguration();
			
			System.out.println(props.getProperty("strArchetypeGroupId"));

			ProjectCreator.createProject(props.getProperty("strArchetypeGroupId"),
					props.getProperty("strArchetypeArtifactId"), props.getProperty("strArcheTypeVersion"), strGroupId,
					strArtifactId);
//			LocalGitRepoDemo.createAndPushLocalRepoToRemote(strArtifactId, username, password,
//					props.getProperty("protocol"), props.getProperty("hostPort"), props.getProperty("apiEndpoint"));
//			CheckoutAndUpdateRemoteRepo.doTask(props.getProperty("protocol"), props.getProperty("hostPort"),
//					props.getProperty("configRepo"), props.getProperty("branchToClone"), username, password);

		} catch (Exception e) {
			System.out.println("Error Executing Create Service: " + e);
		}

	}

}

package com.demo.cli;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.AbortedByHookException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import okhttp3.Response;

public class LocalGitRepoDemo {

	public static final String TEMP_FOLDER = "temp";
	public static final String TEMP_FOLDER_PATH = System.getProperty("user.home") + File.separator + TEMP_FOLDER;
	public static final String MESSAGE = "Added New Files on New Service Creation";

	public static void main(String[] args) {

		
		String username = "bsridhar";
		String password = "Kajol143$";		
		String repoFolderName="mohini-service";
		String apiEndpoint=ConfigFileManager.getConfiguration().getProperty("apiEndpoint");
	
		System.out.println();
		System.out.println();
		System.out.println();
		
		createAndPushLocalRepoToRemote(repoFolderName,username,password,"http://","localhost:7990",apiEndpoint);
	}
	
	public static void createAndPushLocalRepoToRemote(String repoFolderName,String username,String password,String protocol,String hostPort,String apiEndpoint){
		
		//
		String bitbucketRemoteUrl=protocol + username + "@" + hostPort  + ConfigFileManager.getConfiguration().getProperty("bitbucketServerPushContextPath") + repoFolderName + ".git";
		System.out.println("********createAndPushLocalRepoToRemote PARAMETERS******* ");
		System.out.println();
		System.out.println("computed bitbucketRemoteUrl: " +bitbucketRemoteUrl);
		System.out.println("Received repoFolderName: "+repoFolderName);
		System.out.println("Received protocol: "+protocol);
		System.out.println("Received hostPort: "+hostPort);
		System.out.println("Received apiEndpoint: "+apiEndpoint);
		initializeLocalGitRepo(repoFolderName);
		//addFilesToLocalGitRepo(repoFolderName);
		stageAndCommitLocalRepo(repoFolderName);
		createRemoteRepository(repoFolderName,username,password,protocol,hostPort,apiEndpoint);
		pushLocalToRemoteRepo(repoFolderName,bitbucketRemoteUrl ,username,password);
		//deleteTempFolder();
	}
	

	private static void createRemoteRepository(String repoName,String username,String password,String protocol,String hostPort,String apiEndpoint) {
		try {
			
			String authString = username + ":" + password;
			String encodedAuthString = Base64.getEncoder().encodeToString(authString.getBytes());
			System.out.println("********createRemoteRepository PARAMETERS******* ");
			System.out.println();
			System.out.println("Received repoName: "+repoName);
			System.out.println("Received protocol: "+protocol);
			System.out.println("Received hostPort: "+hostPort);
			System.out.println("Received apiEndpoint: "+apiEndpoint);
			Response response = RepositoryManager.createRepository(
					new RepoCreationRequest(repoName, RepoMain.SCMID, true), protocol + hostPort + apiEndpoint, "Basic " + encodedAuthString);
			System.out.println();
			System.out.println();
			System.out.println("******Create Repository Response:" + response.body().string());
			System.out.println();
			System.out.println();
		}catch(Exception e){
			System.err.println(e);
		}
	}

	private static void deleteTempFolder() {
		try {
			FileUtils.deleteDirectory(new File(TEMP_FOLDER_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	private static void pushLocalToRemoteRepo(String repoFolderName, String remoteUrl,String username, String password) {
		Git git;
		try {
			System.out.println();
			System.out.println();
			System.out.println("******Received parameters for pushLocalToRemoteRepo");;
			System.out.println();
			System.out.println();
			System.out.println("******repoFolderName:" + repoFolderName);;
			System.out.println();
			System.out.println();
			
			git = Git.open(new File(repoFolderName));
			PushCommand pushCommand = git.push();
			pushCommand.setRemote(remoteUrl);
			pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password));
			pushCommand.call();
			git.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidRemoteException e) {
			e.printStackTrace();
		} catch (TransportException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}

	}

	private static void stageAndCommitLocalRepo(String repoFolderName) {
		Git git;
		try {
			System.out.println("***********stageAndCommitLocalRepo****");
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println("repoFolderName:" + repoFolderName);
			git = Git.open(new File(repoFolderName));
			
			System.out.println("Opened Git Repository");
			git.add().addFilepattern(".").call();
			System.out.println("Added All Files");
			git.commit().setMessage(MESSAGE).call();
			System.out.println("Committed All Files");
			git.checkout().setCreateBranch(true).setName("integration").call();
			System.out.println("Created Branch Integration and Checked out the Code");
			git.close();
			System.out.println("Closed git repository");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoHeadException e) {
			e.printStackTrace();
		} catch (NoMessageException e) {
			e.printStackTrace();
		} catch (UnmergedPathsException e) {
			e.printStackTrace();
		} catch (ConcurrentRefUpdateException e) {
			e.printStackTrace();
		} catch (WrongRepositoryStateException e) {
			e.printStackTrace();
		} catch (AbortedByHookException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}

	}


	
	
		
	



	public static void initializeLocalGitRepo(String dirPath) {

		try {
			Git git = Git.init().setDirectory(new File(dirPath)).call();
			git.close();
		} catch (IllegalStateException | GitAPIException e) {
			e.printStackTrace();
		}

	}

	
};
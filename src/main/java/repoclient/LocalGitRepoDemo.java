package repoclient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

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
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import okhttp3.Response;

public class LocalGitRepoDemo {

	public static final String TEMP_FOLDER = "temp";
	public static final String TEMP_FOLDER_PATH = System.getProperty("user.home") + File.separator + TEMP_FOLDER;
	public static final String MESSAGE = "Added New Files on New Service Creation";

	public static void main(String[] args) {

		String username = "bsridhar";
		String password = "Kajol143$";		
		String repoFolderName="tyy-service";
		String bitbucketRemoteUrl=getPushUrl("LOCAL",username);
		initializeLocalGitRepo(repoFolderName);
		addFilesToLocalGitRepo(repoFolderName);
		stageAndCommitLocalRepo(repoFolderName);
		createRemoteRepository(repoFolderName,username,password);
		pushLocalToRemoteRepo(repoFolderName,bitbucketRemoteUrl + repoFolderName + ".git",username,password);
		deleteTempFolder();
	}
	
	
	private static String getPushUrl(String type , String username){
		if("LOCAL".equalsIgnoreCase(type)){
			return RepoMain.HTTP_PROTOCOL + username + "@" + RepoMain.LOCAL_API_ENDPOINT + "/scm/home/";
		}else{
			return RepoMain.HTTPS_PROTOCOL + username + "@" + RepoMain.API_ENDPOINT + "/scm/home/";
		}
		
		
	}

	private static void createRemoteRepository(String repoName,String username,String password) {
		try {
			
			String authString = username + ":" + password;
			String encodedAuthString = Base64.getEncoder().encodeToString(authString.getBytes());
			Response response = RepositoryManager.createRepository(
					new RepoCreationRequest(repoName, RepoMain.SCMID, true), RepoMain.HTTP_PROTOCOL + RepoMain.LOCAL_HOST_PORT + RepoMain.LOCAL_API_ENDPOINT, "Basic " + encodedAuthString);
			System.out.println(response.body().string());
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
			git = Git.open(new File(TEMP_FOLDER_PATH + File.separator + repoFolderName));
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
			git = Git.open(new File(TEMP_FOLDER_PATH + File.separator + repoFolderName));
			
			
			//git.checkout().setCreateBranch(true).setName("integration").call();
			
			
			
			/* List<Ref> call = git.branchList().call();
             for (Ref ref : call) {
                 System.out.println("Branch-Before: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName());
             }*/
			git.add().addFilepattern(".").call();
			git.commit().setMessage(MESSAGE).call();
			git.checkout().setCreateBranch(true).setName("integration").call();
			//git.commit().setMessage(MESSAGE).call();
			git.close();
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


	
	private static void addFilesToLocalGitRepo(String repoFolderName) {
		

			
			FileWriter fileWriter;
			try {
				fileWriter = new FileWriter(new File(TEMP_FOLDER_PATH + File.separator + repoFolderName + File.separator + "wtf_kajol.yml"));
				DumperOptions options = new DumperOptions();
				options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
				options.setPrettyFlow(true);
				
				Map<String,String> data=new HashMap<String, String>();
				data.put("Name", "Sridhar");
				data.put("Age","40");
				Yaml yaml = new Yaml(options);
				yaml.dump(data, fileWriter);
				fileWriter.close();

			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		
	



	public static void initializeLocalGitRepo(String dirPath) {

		try {
			Git git = Git.init().setDirectory(new File(TEMP_FOLDER_PATH + File.separator + dirPath)).call();
			git.close();
		} catch (IllegalStateException | GitAPIException e) {
			e.printStackTrace();
		}

	}

	
};
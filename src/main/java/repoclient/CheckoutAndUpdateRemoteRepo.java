package repoclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

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

public class CheckoutAndUpdateRemoteRepo {

	public static final String TEMP_FOLDER = "temp";
	public static final String TEMP_FOLDER_PATH = System.getProperty("user.home") + File.separator + TEMP_FOLDER;
	public static final String MESSAGE = "Updated Config File on New Service Creation";

	public static void main(String[] args) {

		String gitRepo = "repocli";
		String repoUrl = "https://github.com/sri420/repocli";
		String branchToClone = "master";
		String username = "sri420";
		String password = "Kajol143$";

		new CheckoutAndUpdateRemoteRepo().doTask(repoUrl, gitRepo, branchToClone, username, password);
	}

	public void doTask(String repoUrl, String repoFolderName, String branchToClone, String username, String password) {
		cloneRepo(repoUrl, repoFolderName, branchToClone);
		updateLocalRepoFolder(repoFolderName);
		stageAndCommitLocalRepo(repoFolderName);
		pushLocalToRemoteRepo(repoFolderName, username, password);
		deleteTempFolder();
	}

	private static void updateLocalRepoFolder(String repoFolderName) {

		InputStream input;
		FileWriter fileWriter;
		try {
			String folder_path = TEMP_FOLDER_PATH + File.separator + repoFolderName;
			input = new FileInputStream(new File(folder_path + File.separator + "wtf_shaithan.yml"));
			Yaml yaml1 = new Yaml();
			Object data = yaml1.load(input);
			input.close();

			fileWriter = new FileWriter(new File(folder_path + File.separator + "wtf_kajol.yml"));
			DumperOptions options = new DumperOptions();
			options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
			options.setPrettyFlow(true);
			Yaml yaml2 = new Yaml(options);
			yaml2.dump(data, fileWriter);
			fileWriter.close();

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void stageAndCommitLocalRepo(String repoFolderName) {
		Git git;
		try {
			git = Git.open(new File(TEMP_FOLDER_PATH + File.separator + repoFolderName));
			git.add().addFilepattern(".").call();

			git.commit().setMessage(MESSAGE).call();
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

	private static void pushLocalToRemoteRepo(String repoFolderName, String username, String password) {
		Git git;
		try {
			git = Git.open(new File(TEMP_FOLDER_PATH + File.separator + repoFolderName));
			PushCommand pushCommand = git.push();
			pushCommand.setRemote("origin");
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

	private static void deleteTempFolder() {
		try {
			FileUtils.deleteDirectory(new File(TEMP_FOLDER_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void cloneRepo(String url, String targetDir, String branch) {
		try {
			Git git = Git.cloneRepository().setURI(url)
					.setDirectory(new File(TEMP_FOLDER_PATH + File.separator + targetDir)).setBranch(branch).call();
			git.close();
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

	static void deleteDir(File file) {
		File[] contents = file.listFiles();
		if (contents != null) {
			for (File f : contents) {
				deleteDir(f);
			}
		}
		file.delete();
	}
};
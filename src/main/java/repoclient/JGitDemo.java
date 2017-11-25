package repoclient;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class JGitDemo {

	public static void displayCommndInput(BufferedReader reader, String type) throws IOException {
		String s = null;
		System.out.println(type + " Output:\n");
		while ((s = reader.readLine()) != null) {
			System.out.println(s);
		}
	}

	public static void main(String[] arg) {

		// try {

		String localFolder = "F:/demojavarepo";
		/*String strClone = "cmd.exe /c git clone https://github.com/skeeto/sample-java-project F:/demojavarepo";

		Process finalProcess;
		try {
			finalProcess = Runtime.getRuntime().exec(strClone);
			BufferedReader stdInputfinalProcess = new BufferedReader(
					new InputStreamReader(finalProcess.getInputStream()));
			BufferedReader stdErrorfinalProcess = new BufferedReader(
					new InputStreamReader(finalProcess.getErrorStream()));
			displayCommndInput(stdInputfinalProcess, "Command");
			displayCommndInput(stdErrorfinalProcess, "Error");
			//FileUtils.forceDelete(new File(localFolder));
		} catch (IOException e) {
			e.printStackTrace();
		}
*/
		 /*try {
			 
			 File path=Files.createTempDirectory("sri");
			Git git=Git.cloneRepository().setURI("https://github.com/skeeto/sample-java-project")
			 .setDirectory(path).setBranch("master").call();
			
			
			git.close();
			FileUtils.deleteDirectory(new File(localFolder));
			
			
		} catch (GitAPIException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		// Some Processing on the cloned directory

		 

		// FileUtils.deleteQuietly(new File(localFolder));

		// FileUtils.forceDeleteOnExit(new File(localFolder));

		/*
		 * } catch (GitAPIException e) { e.printStackTrace(); } catch
		 * (IOException e) { e.printStackTrace(); }
		 */

	}
}
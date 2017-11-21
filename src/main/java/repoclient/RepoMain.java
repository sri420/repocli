package repoclient;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.Properties;

import okhttp3.Response;

public class RepoMain {

	
	public static final String LOCAL_API_ENDPOINT = "/rest/api/1.0/projects/HOME/repos";
	public static final String LOCAL_HOST_PORT = "localhost:7990";
	public static final String HTTP_PROTOCOL = "http://";
	
	public static final String API_ENDPOINT = "/rest/api/1.0/projects/BP/repos";
	public static final String HOST_PORT = "bitbucket.company.com";
	public static final String HTTPS_PROTOCOL = "https://";
	
	public static final String SCMID = "git";
	
	public static void main(String[] args) throws Exception {
		String env="LOCAL";
		String url=HTTP_PROTOCOL + LOCAL_HOST_PORT + LOCAL_API_ENDPOINT;
		String api_endpoint=LOCAL_API_ENDPOINT;
		String host_port=LOCAL_HOST_PORT;
		String strGitRemoteUrl="%s@%s/scm/home/%s.git";
		String mvnFormattedString=null;
		String mvnFormatString=null;
				
		try{
			env=args[0];
			if(null!=env && env.equalsIgnoreCase("REMOTE")){
				url=HTTPS_PROTOCOL + HOST_PORT + API_ENDPOINT;
				api_endpoint=API_ENDPOINT;
				host_port=HOST_PORT;
				strGitRemoteUrl="%s@%s/scm/bp/%s.git";
			}
		}catch(Exception e){
			env="REMOTE";
			url=HTTPS_PROTOCOL + HOST_PORT + API_ENDPOINT;
			api_endpoint=API_ENDPOINT;
			host_port=HOST_PORT;
			strGitRemoteUrl="%s@%s/scm/bp/%s.git";
			
			
		}
		
		//System.out.println("********ENV is :" + env);
		//System.out.println("********Url is :" + url);
		
		Console console = System.console();
		String s = null;
		String gitAdd="git add ."; 
		String message="Initial";
		String userName =null;
		String encodedAuthString=null;
		
		
		
		//Prompt for Parameters required for creating Maven Project creation from archetype
		System.out.println();
		System.out.println();
		System.out.println();
		String strGroupId = console.readLine("Enter the GroupId  for new new project: ");
		System.out.println();
		String strArtifactId = console.readLine("Enter the ArtifactId  for new new project: ");
		System.out.println();
		String strPackageName =strGroupId +"." + strArtifactId;// "com.demo.bp.services.hello-service";//console.readLine("Enter the PackageName  for new new project: ");
		System.out.println();
		String strDescription = "description of service";//console.readLine("Enter the Description  for new new project: ");
		System.out.println();
		String strVersion = "1.0.0-SNAPSHOT";//console.readLine("Enter the Version  for new new project: ");
		System.out.println();
		
		
		if(ConfigFileManager.shouldPromptUserForCredentials()){
			System.out.println("Prompting User for Credentials");
			
			
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			userName = console.readLine("Enter your bitbucket UserName: ");
			System.out.println();
			System.out.println();
			char passwordArray[] = console.readPassword("Enter your bitbucket password: ");
			String strPwd = new String(passwordArray);
			
			//Base64 encode username:password
			String authString = userName + ":" + strPwd;
			encodedAuthString = Base64.getEncoder().encodeToString(authString.getBytes());
			
			//Store UserName and Password in user's home directory
			Properties prop=new Properties();
			prop.setProperty("username", userName);

			prop.put("password", encodedAuthString);
			
			if(env.equalsIgnoreCase("REMOTE")){
				prop.put("api_endpoint", API_ENDPOINT);
				prop.put("host_port", HOST_PORT);
			}else{
				prop.put("api_endpoint", LOCAL_API_ENDPOINT);
				prop.put("host_port", LOCAL_HOST_PORT);
			}

			ConfigFileManager.addPropsToConfigFile(prop);
		}else{
			//System.out.println("Credentials are not required already setup");
			//Fetch the values from user's home directory
			userName=ConfigFileManager.getValueForKey("username");
			encodedAuthString=ConfigFileManager.getValueForKey("password");
		}

		
	
		if(env.equalsIgnoreCase("REMOTE")){
			mvnFormatString="mvn archetype:generate -DarchetypeGroupId=com.anthem.bp.suport  -DarchetypeArtifactId=bp-service-archetype -DarchetypeVersion=0.0.7-SNAPSHOT -DgroupId=%s -DartifactId=%s -Dversion=%s -Ddescription=%s -DinteractiveMode=false ";
			mvnFormattedString=String.format(mvnFormatString,strGroupId,strArtifactId,strVersion,strDescription);
		}else{
			 mvnFormatString = " mvn archetype:generate -DgroupId=%s  -DartifactId=%s -DarchetypeArtifactId=maven-archetype-webapp -DinteractiveMode=false";
			 mvnFormattedString=String.format(mvnFormatString,strGroupId,strArtifactId);
		}
		
		
		//Create the Repository in Bitbucket Server
		System.out.println();
		System.out.println();
		System.out.println("Creating Remote Repository...");
		System.out.println();
		System.out.println();
		try{
				//System.out.println(" URL is:" + url);
				Response response=RepositoryManager.createRepository(
					new RepoCreationRequest(
											strArtifactId,
											RepoMain.SCMID, 
											true
											),
					   url,
					  "Basic " + encodedAuthString
					);
				int code=response.code();
				//System.out.println("***code is:" + code);
				if(code==401){
						Properties prop=new Properties();
						prop.setProperty("username", "");
						prop.put("password", "");
						prop.put("api_endpoint", api_endpoint);
						prop.put("host_port", host_port);
						ConfigFileManager.addPropsToConfigFile(prop);
						System.out.println();
						System.out.println();
						System.out.println("!!!!!!! Authentication Failed. Please try again.!!!!!");
						System.out.println();
						System.out.println();
						System.exit(0);
				}
				if(code==404){
					System.out.println();
					System.out.println();
					System.out.println("!!!!!!! URL Not Found. Please check Bitbucket Server URL is correct: " + url);
					System.exit(0);
				}
				
				System.out.println();
				System.out.println();
				System.out.println("**********Successfully Created Remote Repository:  "+ strArtifactId );
				System.out.println();
				System.out.println();
		}catch(Exception e){
			System.out.println("****Exception: " + e);
			System.exit(0);
		}
		
		
		//Execute the Maven Creation , Git Initialization,Git Local Commit and Pushing to Remote Repository 
		String strFormattedGitRemoteUrl=String.format(strGitRemoteUrl,HTTP_PROTOCOL,LOCAL_HOST_PORT,strArtifactId);
		if(env.equalsIgnoreCase("REMOTE")){
			strFormattedGitRemoteUrl=String.format(strGitRemoteUrl,HTTPS_PROTOCOL,HOST_PORT,strArtifactId);
		}
		

		String finalStr=String.format("cmd.exe /c  %s & cd %s & git init & %s  & git commit -m %s & git remote add origin  %s & git checkout -B integration & git push -u origin integration ", mvnFormattedString,strArtifactId,gitAdd,message,strFormattedGitRemoteUrl);

		
		//System.out.println("finalStr:" + finalStr);
		System.out.println();
		System.out.println("***Creating Project From Template and Pushing the code to Remote Repository: " + strArtifactId);
		System.out.println();
		System.out.println();
		
		Process process = Runtime.getRuntime().exec(finalStr);
		
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

		BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

		// read the output from the command
		System.out.println("Command Output:\n");
		while ((s = stdInput.readLine()) != null) {
			System.out.println(s);
		}

		// read any errors from the attempted command
		System.out.println("Error Output ( If any )\n");
		while ((s = stdError.readLine()) != null) {
			System.out.println(s);
		}

	}
}

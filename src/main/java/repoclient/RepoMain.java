package repoclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;
import java.io.Console;
import java.util.Base64;
import okhttp3.Response;

public class RepoMain {

	
	public static final String API_ENDPOINT = "/rest/api/1.0/projects/BP/repos";
	public static final String HOST_PORT = "localhost:7990";//"bitbucket.anthem.com";
	public static final String SCMID = "git";
	public static final String PROTOCOL = "http://";//https://
	
	
	public static void main(String[] args) throws Exception {
		Console console = System.console();
		String s = null;
		String gitAdd="git add ."; 
		String message="Initial";
		String userName =null;
		String encodedAuthString=null;
		
	/*	
		
		/*
		 * Inputs required for the Program
		 * ===============================
		 * For Maven Project Creation from archetype
		 * -----------------------------------------
		 * -groupId
		 * -artifactId
		 * -package
		 * -version
		 * -description
		 * 
		 * For Remote Bitbucket Reository Creation
		 * ---------------------------------------
		 * repository name ( 
		 * 	Ideally we can resuse artifactId  without prompting the user ,
		 *  Alternatively,  we can default to artifactId and have user override it if required )
		 * 
		 * 
		 * For Bitbucket authentication
		 * ----------------------------
		 * -username
		 * -password
		 * 
		 * This is only required for first time. After successful authentication,
		 * the username and base64 encoded password would be stored in the user's home directory.
		 * On subsequent runs, the values would be fetched from there and authenticated with bitbucket accordingly.
		 * 
		 * If authentication fails on subsequent runs, due to one or more reasons as below
		 * 	--Credentials have expired/unknown credentials
		 *  --Config file stored in user's home directory is not accessible / missing or
		 *  --Config File exists but username and/or password vales are either null or empty
		 *  --Some other unknown reason
		 *  
		 *  In those scenarios, the solution is  Renew the config credentials by,
		 *  -- Prompt the user for username and password and 
		 *  -- Authenticate with  bitbucket and 
		 *  -- On successful authentication write it to the config file accordingly.
		 *  
		 *   Creating Project from Maven Archetype
		 *   =====================================
		 *    mvn archetype:generate -DinteractiveMode=false^
		 *		-DarchetypeGroupId=com.anthem.bp.support^
		 *		-DarchetypeArtifactId=bp-service-archetype^
		 *		-DarchetypeVersion=0.0.7-SNAPSHOT^
		 *		-DgroupId=<<GroupId>>^
		 *		-DartifactId=<<ArtifactId>>^
		 *		-Dversion=<<Version>>^
		 *		-Ddescription="<<Description>>"
		 * 
		 *  Git Repository Specific Commands
		 *  ================================
		 *   git init
		 *   git add .
		 *	 git commit - m <<message>>
		 *
		 *   git remote add origin  http://<<host:Port>>/scm/home/<<artifactId>>.git
		 *   git checkout -B integration
		 *   git push -u origin integration";
		 */
		
		
		//Prompt for Parameters required for creating Maven Project creation from archetype
		System.out.println();
		System.out.println();
		System.out.println();
		String strGroupId = console.readLine("Enter the GroupId  for new new project: ");
		System.out.println();
		String strArtifactId = console.readLine("Enter the ArtifactId  for new new project: ");
		System.out.println();
		//String strPackageName =strGroupId +"." + strArtifactId;// "com.demo.bp.services.hello-service";//console.readLine("Enter the PackageName  for new new project: ");
		System.out.println();
		String strDescription = "description of service";//console.readLine("Enter the Description  for new new project: ");
		System.out.println();
		String strVersion = "1.0.0-SNAPSHOT";//console.readLine("Enter the Version  for new new project: ");
		System.out.println();
		
		/*System.out.println();
		System.out.println();
		System.out.println("***Received the below parameters***");
		System.out.println("******GroupId:" + strGroupId);
		System.out.println("***ArtifactId:" + strArtifactId);
		System.out.println("**PackageName:" + strPackageName);
		System.out.println("**Description:" + strDescription);
		System.out.println("******Version:" + strVersion);
		System.out.println();
		System.out.println();*/
		
		if(ConfigFileManager.shouldPromptUserForCredentials()){
			System.out.println("Prompt User for Credentials");
			
			
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
			prop.put("api_endpoint", API_ENDPOINT);
			prop.put("host_port", HOST_PORT);

			ConfigFileManager.addPropsToConfigFile(prop);
		}else{
			System.out.println("Credentials are not required already setup");
			
			//Fetch the values from user's home directory
			userName=ConfigFileManager.getValueForKey("username");
			encodedAuthString=ConfigFileManager.getValueForKey("password");
		}
		
		//Create the Repository in Bitbucket Server
		
		System.out.println("About to Create Remote Repository...");
		try{
				Response response=RepositoryManager.createRepository(
					new RepoCreationRequest(
											strArtifactId,
											"git", 
											true
											),
					PROTOCOL + HOST_PORT + API_ENDPOINT,
					  "Basic " + encodedAuthString
					);
				int code=response.code();
				System.out.println("code is:" + code);
				if(code==401){
						Properties prop=new Properties();
						prop.setProperty("username", "");
						prop.put("password", "");
						prop.put("api_endpoint", API_ENDPOINT);
						prop.put("host_port", HOST_PORT);
						ConfigFileManager.addPropsToConfigFile(prop);
						System.out.println("Authentication Failed. Please try again.");
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
		
		
		
		//Construct String for Maven Creation from Archetype
		
		String mvnFormatString="mvn archetype:generate -DarchetypeGroupId=com.anthem.bp.support  -DarchetypeArtifactId=bp-service-archetype -DarchetypeVersion=0.0.7-SNAPSHOT -DgroupId=%s -DartifactId=%s -Dversion=%s -Ddescription=%s -DinteractiveMode=false ";
		
		//System.out.println("mvnFormatString: " + mvnFormatString);
		
		String mvnFormattedString=String.format(mvnFormatString,strGroupId,strArtifactId,strVersion,strDescription);
		
		//Execute the Maven Creation , Git Initialization,Git Local Commit and Pushing to Remote Repository 
		String strGitRemoteUrl="%s@%s/scm/dp/%s.git";
		String strFormattedGitRemoteUrl=String.format(strGitRemoteUrl,PROTOCOL,HOST_PORT,strArtifactId);

		

		String finalStr=String.format("cmd.exe /c  %s & cd %s & git init & %s  & git commit -m %s & git remote add origin  %s & git checkout -B integration & git push -u origin integration ", mvnFormattedString,strArtifactId,gitAdd,message,strFormattedGitRemoteUrl);

		
		System.out.println();
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

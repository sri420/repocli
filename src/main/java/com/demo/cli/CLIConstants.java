package com.demo.cli;

public class CLIConstants {
/*	
	protocol=http://
		hostPort=localhost:7990
		apiEndpoint=/rest/api/1.0/projects/HOME/repos
		branchToClone=integration
		strArchetypeGroupId=internal:com.github.spring-boot-archetypes
		strArchetypeArtifactId=spring-boot-quickstart
		strArcheTypeVersion=1.0.0
		description=SimpleService
		version=1.0.0-SNAPSHOT
		StorePasswordInCache=N
		bitbucketServerPushContextPath=/scm/home/
		configRepo=config-repo*/

	public static final String PROTOCOL = "http://";
	public static final String HOST_PORT="localhost:7990";
	public static final String API_ENDPOINT = "/rest/api/1.0/projects/HOME/repos";
	public static final String BRANCH_TO_CLONE="integration";
	public static final String ARCHETYPE_GROUPID = "com.github.spring-boot-archetypes";
	public static final String ARCHETYPE_ARCHETYPEID = "spring-boot-quickstart";
	public static final String ARCHETYPE_ARCHETYPEVERSION = "1.0.0";
	public static final String DESCRIPTION= "SimpleService";
	public static final String version= "1.0.0-SNAPSHOT";
	public static final String CONFIGREPO= "config-repo";
	public static final String BITBUCKET_SERVER_CONTEXT_PATH="/scm/home/";
	public static final String HTTP_PROTOCOL = "http://";
	public static final String SETTINGS_FOLDER=".microservices-cli";
	public static final String SETTINGS_FILE="settings.properties";
	public static final String USER_HOME_DIR="user.home";
	
	

	
	
	/*public static final String HTTPS_PROTOCOL = "https://";
	
	public static final String PUSH_URL="@" + RepoMain.LOCAL_HOST_PORT  + BITBUCKET_SERVER_CONTEXT_PATH;*/

}

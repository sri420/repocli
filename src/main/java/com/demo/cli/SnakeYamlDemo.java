package com.demo.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class SnakeYamlDemo {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args){
		Yaml yaml = new Yaml();
		String document = "\n- Hesperiidae\n- Papilionidae\n- Apatelodidae\n- Epiplemidae";
		List<String> list = (List<String>) yaml.load(document);
		System.out.println(list);
		
		 InputStream input;
		try {
			input = new FileInputStream(new File("src/test/resources/contact.yml"));

			Yaml yaml2 = new Yaml();
			 Object data = yaml2.load(input);
			 System.out.println(data);
			 StringWriter writer = new StringWriter();
			 yaml.dump(data, writer);

			 DumperOptions options = new DumperOptions();
			 options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
			 options.setPrettyFlow(true);
			 
			 
			 Yaml yaml3=new Yaml(options);

			 
			 
			 System.out.println(writer.toString());
			 
			 String userHome=System.getProperty("user.home");
			 
			 String tempDir=userHome + File.separator + "hellotemp";
			 
			 FileUtils.forceMkdir(new File(tempDir));
			 
			 
			 FileWriter fileWriter = new FileWriter(new File(tempDir + File.separator + "newestcontact.yml")); 
			
			 yaml3.dump(data, fileWriter);
			 fileWriter.close();
			
			 FileUtils.deleteDirectory(new File(tempDir));
			
			 
			 FileRepositoryBuilder builder = new FileRepositoryBuilder();
			 Repository repository = builder.setGitDir(new File("F:/salmon"))
			   .readEnvironment() // scan environment GIT_* variables
			   .findGitDir() // scan up the file system tree
			   .build();
			 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

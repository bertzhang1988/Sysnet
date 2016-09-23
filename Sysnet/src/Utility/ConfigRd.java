package Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigRd {
	
	Properties pro;
	
	public ConfigRd() throws IOException{
		File src=new File("./Configuration/SysnetConfig.property");
		FileInputStream fis= new FileInputStream(src);
		pro= new Properties();
		pro.load(fis);
	}

    public String GetChromePath(){
    	String path=pro.getProperty("ChromeDriverPath");
    	return path;
    } 

    public String GetIEPath(){
    	String path=pro.getProperty("IEdriverPath");
    	return path;
    } 
   
    public String GetSysnetSitURL(){
    	String path=pro.getProperty("SysnetSitURL");
    	return path;
    } 

}

package Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigRd {

	private Properties pro;
	private String URL;
	private String DBurl;
	private String DBuser;
	private String DBpassword;
	private String DBschema;

	public ConfigRd() {
		try {
			File src = new File("./Configuration/SysnetConfig.property");
			FileInputStream fis = new FileInputStream(src);
			pro = new Properties();
			pro.load(fis);

			fis.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public ConfigRd(String environment) {
		try {
			File src = new File("./Configuration/SysnetConfig.property");
			FileInputStream fis = new FileInputStream(src);
			pro = new Properties();
			pro.load(fis);

			fis.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		if (environment.equalsIgnoreCase("sit")) {

			URL = pro.getProperty("SysnetSitURL");
			DBurl = pro.getProperty("Sitdb");
			DBuser = pro.getProperty("UserName");
			DBpassword = pro.getProperty("Password");
			DBschema = pro.getProperty("SitUser");

		} else if (environment.equalsIgnoreCase("dev")) {

			URL = pro.getProperty("SysnetDevURL");
			DBurl = pro.getProperty("Devdb");
			DBuser = pro.getProperty("UserName");
			DBpassword = pro.getProperty("Password");
			DBschema = pro.getProperty("DevUser");

		} else if (environment.equalsIgnoreCase("qa")) {

			URL = pro.getProperty("SysnetQaURL");
			DBurl = pro.getProperty("Qadb");
			DBuser = pro.getProperty("UserName");
			DBpassword = pro.getProperty("Password");
			DBschema = pro.getProperty("QaUser");
		}
	}

	public String GetChromePath() {
		String path = pro.getProperty("ChromeDriverPath");
		return path;
	}

	public String GetIEPath() {
		String path = pro.getProperty("IEdriverPath");
		return path;
	}

	/* set current connection */

	public String GetDatabase() {
		String path = DBurl;
		return path;
	}

	public String GetDbUserName() {
		String path = DBuser;
		return path;
	}

	public String GetDbPassword() {
		String path = DBpassword;
		return path;
	}

	public String GetUserSchema() {
		String path = DBschema;
		return path;
	}

	public String GetSysnetURL() {
		String path = URL;
		return path;
	}

}

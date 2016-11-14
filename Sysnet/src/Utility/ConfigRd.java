package Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigRd {

	Properties pro;

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

	public String GetChromePath() {
		String path = pro.getProperty("ChromeDriverPath");
		return path;
	}

	public String GetIEPath() {
		String path = pro.getProperty("IEdriverPath");
		return path;
	}

	public String GetSysnetSitURL() {
		String path = pro.getProperty("SysnetSitURL");
		return path;
	}

	public String GetSysnetDevURL() {
		String path = pro.getProperty("SysnetDevURL");
		return path;
	}

	public String GetSysnetQaURL() {
		String path = pro.getProperty("SysnetQaURL");
		return path;
	}

	public String GetDatabase() {
		String path = pro.getProperty("Sitdb");
		return path;
	}

	public String GetDbUserName() {
		String path = pro.getProperty("UserName");
		return path;
	}

	public String GetDbPassword() {
		String path = pro.getProperty("Password");
		return path;
	}

	public String GetUserSchema() {
		String path = pro.getProperty("SitUser");
		return path;
	}

	public String GetSitDatabase() {
		String path = pro.getProperty("Sitdb");
		return path;
	}

	public String GetQaDatabase() {
		String path = pro.getProperty("Qadb");
		return path;
	}

	public String GetDevDatabase() {
		String path = pro.getProperty("Devdb");
		return path;
	}

	public String GetSitUserSchema() {
		String path = pro.getProperty("SitUser");
		return path;
	}

	public String GetDevUserSchema() {
		String path = pro.getProperty("DevUser");
		return path;
	}

	public String GetQaUserSchema() {
		String path = pro.getProperty("QaUser");
		return path;
	}

}

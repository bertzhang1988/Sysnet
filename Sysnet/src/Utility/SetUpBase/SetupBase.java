package Utility.SetUpBase;

import java.awt.AWTException;
import java.io.IOException;
import java.sql.SQLException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import Utility.ConfigRd;

public class SetupBase {

	protected WebDriver driver;
	protected static ConfigRd conf;

	@BeforeClass
	@Parameters({ "browser", "env" })
	public void SetUpBrowser(@Optional("chrome") String browser, @Optional("sit") String environment)
			throws AWTException, InterruptedException, IOException, SQLException {
		conf = new ConfigRd(environment);

		if (browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", conf.GetChromePath());
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("ie")) {
			System.setProperty("webdriver.ie.driver", conf.GetIEPath());
			driver = new InternetExplorerDriver();
		}
		driver.get(conf.GetSysnetURL());
		driver.manage().window().maximize();
	}

	@AfterClass
	public void CloseBrowser() {
		driver.close();
	}

}

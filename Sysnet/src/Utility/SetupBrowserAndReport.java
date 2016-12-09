package Utility;

import java.awt.AWTException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.TimeZone;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class SetupBrowserAndReport {

	protected WebDriver driver;
	protected FileWriter fw;
	protected String Nl;
	
	@BeforeClass
	@Parameters({ "browser" })
	public void SetUp(@Optional("chrome") String browser)
			throws AWTException, InterruptedException, IOException, SQLException {
		ConfigRd Conf = new ConfigRd();

		if (browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", Conf.GetChromePath());
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("ie")) {
			System.setProperty("webdriver.ie.driver", Conf.GetIEPath());
			driver = new InternetExplorerDriver();
		}
		driver.get(Conf.GetSysnetDevURL());
		driver.manage().window().maximize();

		// create text file
		String CDate = Function.GetTimeValue(TimeZone.getDefault().getID());
		File file2 = new File("./Report/" + CDate);
		file2.mkdir();
		File file = new File(file2, this.getClass().getName() + ".txt");
		try {
			fw = new FileWriter(file, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Nl = System.getProperty("line.separator");
	}

	@AfterClass
	public void CloseBrowser() {
		try {
			fw.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		driver.close();
	}

}

package Utility.SetUpBase;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

import Utility.ConfigRd;
import Utility.Function;

import org.testng.annotations.BeforeClass;

import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.TimeZone;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;
/*setup with extent report*/
public class SetupBrowserAndExtentReport {

	protected WebDriver driver;
	protected ExtentReports Report;
	protected ExtentTest testlog;

	@BeforeClass
	@Parameters({ "browser", "env" })
	public void SetUp(@Optional("chrome") String browser, @Optional("sit") String environment)
			throws AWTException, InterruptedException, IOException, SQLException {
		ConfigRd Conf = new ConfigRd(environment);

		if (browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", Conf.GetChromePath());
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("ie")) {
			System.setProperty("webdriver.ie.driver", Conf.GetIEPath());
			driver = new InternetExplorerDriver();
		}
		driver.get(Conf.GetSysnetURL());
		driver.manage().window().maximize();
	}

	@AfterClass
	public void CloseBrowser() {
		driver.close();
	}

	@BeforeTest
	public void SetupExtentReport() {
		String CDate = Function.GetTimeValue(TimeZone.getDefault().getID());
		File f1 = new File("./Report/" + CDate);
		f1.mkdir();
		Report = new ExtentReports(f1.getAbsolutePath() + "/" + this.getClass().getName() + ".html", true);
		testlog = Report.startTest(this.getClass().getName());
	}

	@AfterTest
	public void afterTest() {
		Report.endTest(testlog);
		Report.flush();
		Report.close();
	}

	@BeforeSuite
	public void beforeSuite() {
	}

	@AfterSuite
	public void afterSuite() {
	}

}

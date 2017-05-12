package Utility.SetUpBase;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

import Utility.Function;

import java.io.File;
import java.util.TimeZone;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;

/*setup with extent report*/
public class SetupBrowserAndExtentReport extends SetupBase {

	protected ExtentReports Report;
	protected ExtentTest testlog;

	@BeforeClass
	public void SetupExtentReport() {
		String CDate = Function.GetTimeValue(TimeZone.getDefault().getID());
		File f1 = new File("./Report/" + CDate);
		f1.mkdir();
		Report = new ExtentReports(f1.getAbsolutePath() + "/" + this.getClass().getName() + ".html", true);
		testlog = Report.startTest(this.getClass().getName());
	}

	@AfterClass
	public void CloseExtentReport() {
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

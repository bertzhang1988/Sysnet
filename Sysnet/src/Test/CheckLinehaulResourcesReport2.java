package Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import Data.CommonData;
import Utility.Function;
import Utility.Utility;
import Utility.SetUpBase.SetupBrowserAndExtentReport;
import page.SysnetPage;
import java.util.Set;
public class CheckLinehaulResourcesReport2 extends SetupBrowserAndExtentReport {

	private SysnetPage page;
	private WebDriverWait wait1;

	@BeforeClass
	public void Setup() throws IOException, SQLException {
		page = new SysnetPage(driver);
		if (!page.isVisable(page.LinehaulResourcesButton))
			page.Square.click();
		wait1 = new WebDriverWait(driver, 50);
		wait1.until(ExpectedConditions.visibilityOf(page.LinehaulResourcesButton));
		page.LinehaulResourcesButton.click();
		wait1.until(ExpectedConditions.visibilityOf(page.LinehaulForm));
		wait1.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		if (page.isVisable(page.LinehaulResourcesButton) && page.isVisable(page.Square))
			page.Square.click();
		testlog.log(LogStatus.INFO, "open linehual resourse page");
		  Set<Cookie> totalCookies = driver.manage().getCookies();
		  System.out.println("Total Number Of cookies : " +totalCookies.size());
		  for (Cookie currentCookie : totalCookies) {
		      System.out.println(String.format("%s -> %s -> %s -> %s", "Domain Name : "+currentCookie.getDomain(), "Cookie Name : "+currentCookie.getName(), "Cookie Value : "+currentCookie.getValue(), "Cookie Expiry : "+currentCookie.getExpiry()));
		  }  
	}

	@Test
	public void CheckLineHaulResourceAllLocation() throws SQLException, IOException {
		page.LhAllLocTer.click();
		wait1.until(ExpectedConditions.visibilityOf(page.LhUsaCy));
		LinkedHashSet<ArrayList<String>> ExpectedLineHualResourceList = CommonData.GetLinehaulForm();
		LinkedHashSet<ArrayList<String>> ActualLineHualResourceList = page.GetReport(page.LinehaulForm);
		if (!ActualLineHualResourceList.equals(ExpectedLineHualResourceList)) {
			System.out.println("Linehaul Resource is wrong");
			System.out.println("expected : " + ExpectedLineHualResourceList);
			System.out.println("actual :   " + ActualLineHualResourceList);
			String ScreenshotFile = Utility.takescreenshot(driver, "CheckLineHaulResourceAllLocation");
			testlog.log(LogStatus.FAIL, "actual result show but not expected: "
					+ Function.GetDiffBetwForm(ActualLineHualResourceList, ExpectedLineHualResourceList),testlog.addScreenCapture(ScreenshotFile));
		} else {
			System.out.println("Linehaul Resource is correct");
			testlog.log(LogStatus.PASS, "Linehaul Resource is correct");
		}
	}

	@AfterMethod
	public void TakeScreenShotForFailure(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			String ScreenshotFile = Utility.takescreenshot(driver, result.getName());
			testlog.addScreenCapture(ScreenshotFile);
		}
	}

}

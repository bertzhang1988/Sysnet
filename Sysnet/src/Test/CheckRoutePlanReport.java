package Test;

import org.testng.annotations.Test;
import Data.CommonData;
import Utility.Function;
import Utility.SetUpBase.SetupBrowserAndTextReport;
import page.SysnetPage;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/*validate report of Route plan */
public class CheckRoutePlanReport extends SetupBrowserAndTextReport {

	private SysnetPage page;
	private JavascriptExecutor js;
	private WebDriverWait w1;
	private WebDriverWait w2;

	@BeforeClass
	public void SetUp() throws IOException, SQLException {
		page = new SysnetPage(driver);
		js = (JavascriptExecutor) driver;
		w1 = new WebDriverWait(driver, 120);
		w2 = new WebDriverWait(driver, 50);
		// navigate to Route plan page
		if (!page.isVisable(page.RoutePlanButton))
			page.Square.click();
		w1.until(ExpectedConditions.elementToBeClickable(page.RoutePlanButton));
		page.RoutePlanButton.click();
		w2.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		fw.write(Function.GetDisplayTime().replace(":", "-") + Nl);
		if (page.isVisable(page.RoutePlanButton) && page.isVisable(page.Square))
			page.Square.click();
	}

	@Test(priority = 1, description = "check inter terminal")
	public void CheckInterRoutePlan() throws SQLException, IOException {
		// expand terminal type radio button
		js.executeScript("arguments[0].scrollIntoView(false);", page.InterRadioButton);
		if (page.FilterView.isDisplayed())
			page.FilterView.findElement(By.cssSelector("i.fa.fa-chevron-down.pull-right.toggle-filter-view")).click();
		// select inter
		if (!page.InterRadioButton.isSelected())
			page.InterRadioButton.click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		// get route plan value from screen
		LinkedHashSet<ArrayList<String>> ActualRoutePlanInfo = page.GetReport(page.RoutePlanForm);
		long d = Calendar.getInstance().getTime().getTime();
		// get expected value
		LinkedHashSet<ArrayList<String>> ExpectedRoutePlanInfo = CommonData.GetRoutePlanForm("inter");
		long e = Calendar.getInstance().getTime().getTime();
		System.out.println((e - d) / 1000);
		// verify result and write result to report
		if (!ActualRoutePlanInfo.equals(ExpectedRoutePlanInfo)) {
			System.out.println("Inter Route Plan is wrong");
			System.out.println("expected : " + ExpectedRoutePlanInfo);
			System.out.println("actual :   " + ActualRoutePlanInfo);
			fw.write("Inter Route Plan is wrong:" + Nl);
			fw.write(Nl+"actual :   " + ActualRoutePlanInfo+Nl);
			fw.write(Nl+"expected : " + ExpectedRoutePlanInfo+Nl);
			fw.write(Nl + "expected result but not show:" + Nl
					+ Function.GetDiffBetwForm(ExpectedRoutePlanInfo, ActualRoutePlanInfo) + Nl);
			fw.write(Nl + "actual result show but not expected:" + Nl
					+ Function.GetDiffBetwForm(ActualRoutePlanInfo, ExpectedRoutePlanInfo) + Nl);
		} else {
			System.out.println("Inter Route Plan is correct");
			fw.write(Nl + "Inter Route Plan is correct");
		}

	}

	@Test(priority = 2, description = "check intra terminal")
	public void CheckIntraRoutePlan() throws SQLException, IOException {
		// expand terminal type radio button
		js.executeScript("arguments[0].scrollIntoView(false);", page.IntraRadioButton);
		if (page.FilterView.isDisplayed())
			page.FilterView.findElement(By.cssSelector("i.fa.fa-chevron-down.pull-right.toggle-filter-view")).click();
		// select intra
		if (!page.IntraRadioButton.isSelected())
			page.IntraRadioButton.click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		// get route plan value from screen
		LinkedHashSet<ArrayList<String>> ActualRoutePlanInfo = page.GetReport(page.RoutePlanForm);
		// get expected value
		LinkedHashSet<ArrayList<String>> ExpectedRoutePlanInfo = CommonData.GetRoutePlanForm("intra");
		// verify result and write result to report
		if (!ActualRoutePlanInfo.equals(ExpectedRoutePlanInfo)) {
			System.out.println("Intra Route Plan is wrong");
			System.out.println("expected : " + ExpectedRoutePlanInfo);
			System.out.println("actual :   " + ActualRoutePlanInfo);
			fw.write(Nl + "Intra Route Plan is wrong" + Nl);
			fw.write(Nl+"actual :   " + ActualRoutePlanInfo+Nl);
			fw.write(Nl+"expected : " + ExpectedRoutePlanInfo+Nl);
			fw.write(Nl + "expected result but not show:" + Nl
					+ Function.GetDiffBetwForm(ExpectedRoutePlanInfo, ActualRoutePlanInfo) + Nl);
			fw.write(Nl + "actual result show but not expected:" + Nl
					+ Function.GetDiffBetwForm(ActualRoutePlanInfo, ExpectedRoutePlanInfo) + Nl);
		} else {
			System.out.println("intra Route Plan is correct");
			fw.write(Nl + "Intra Route Plan is correct");
		}

	}

	@AfterMethod
	public void afterMethod() {
	}

}

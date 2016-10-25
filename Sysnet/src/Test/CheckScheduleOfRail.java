package Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.TimeZone;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import Data.CommonData;
import Utility.Function;
import Utility.SetupBase;
import page.SysnetPage;

public class CheckScheduleOfRail extends SetupBase {

	private SysnetPage Page;
	private String Nl;
	private FileWriter fw;

	@BeforeClass
	public void Setup() {

		Page = new SysnetPage(driver);
		(new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOf(Page.SystemSummaryButton));
		Page.SystemSummaryButton.click();

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

	@Test(priority = 1)
	public void VerifySchedulesForRail() throws SQLException {
		SoftAssert sa = new SoftAssert();
		(new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOf(Page.Railform));
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));

		// check empty rail schedule
		String ScheduleE = Page.TotalEmptyRail.findElement(By.xpath("following-sibling::td")).getText();
		String ExpectedSheduleE = CommonData.GetScheduleForEmptyRail();
		sa.assertEquals(ScheduleE, ExpectedSheduleE, "the schedule of empty rail is wrong");

		// check load rail schedule
		String ScheduleL = Page.TotalLoadedRail.findElement(By.xpath("following-sibling::td")).getText();
		String ExpectedShedulel = CommonData.GetScheduleForLoadedRail();
		sa.assertEquals(ScheduleL, ExpectedShedulel, "the schedule of loaded rail is wrong");

		sa.assertAll();
	}

	@Test(priority = 2)
	public void VerifySchedulesForRail2() throws SQLException, IOException {

		(new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOf(Page.Railform));
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		fw.write(Nl + "empty rail schedule: ");
		// check empty rail schedule
		String ScheduleE = Page.TotalEmptyRail.findElement(By.xpath("following-sibling::td")).getText();
		String ExpectedSheduleE = CommonData.GetScheduleForEmptyRail();

		if (ScheduleE.equals(ExpectedSheduleE)) {
			fw.write(Nl + "the result is correct");
		} else {
			fw.write(Nl + "expected Shedule of empty Rail: " + ExpectedSheduleE + "but found: " + ScheduleE);
		}

		fw.write(Nl + "loaded rail schedule: ");
		// check load rail schedule
		String ScheduleL = Page.TotalLoadedRail.findElement(By.xpath("following-sibling::td")).getText();
		String ExpectedShedulel = CommonData.GetScheduleForLoadedRail();
		if (ScheduleL.equals(ExpectedShedulel)) {
			fw.write(Nl + "the result is correct");
		} else {
			fw.write(Nl + "expected Shedule of loaded Rail: " + ExpectedShedulel + " but found: " + ScheduleL);
		}
	}

	@AfterClass
	public void close() {
		try {
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

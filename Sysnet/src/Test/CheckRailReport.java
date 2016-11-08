package Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TimeZone;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import Data.CommonData;
import Data.DataDAO;
import Utility.Function;
import Utility.SetupBase;
import page.SysnetPage;

public class CheckRailReport extends SetupBase {

	private SysnetPage Page;
	private String Nl;
	private FileWriter fw;
	private String MainWindowHandler;

	@BeforeClass
	public void Setup() {

		Page = new SysnetPage(driver);
		(new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOf(Page.SystemSummaryButton));
		Page.SystemSummaryButton.click();
		(new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOf(Page.Railform));
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));

		// get main page handler
		MainWindowHandler = driver.getWindowHandle();

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

	@Test(priority = 1, enabled = false)
	public void VerifySchedulesForRail() throws SQLException {
		SoftAssert sa = new SoftAssert();

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

	@Test(priority = 3)
	public void RailEmptyForm(Method m) throws IOException, SQLException {

		Page.TotalEmptyRail.click();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> WindowHandles = driver.getWindowHandles();
		for (String windowHandle : WindowHandles) {
			if (!windowHandle.equalsIgnoreCase(MainWindowHandler)) {
				driver.switchTo().window(windowHandle);
			}
		}
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		(new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOf(Page.TotalEmptyRailForm));
		LinkedHashSet<ArrayList<String>> TrailerGRID = Page.GetTrailerReportList(Page.TotalEmptyRailForm);
		System.out.println("\n Total Empty Rail Form " + TrailerGRID.size());
		fw.write(Nl + " Total Empty Rail Form " + TrailerGRID.size() + Nl);
		int i = 0;
		int j = 0;
		DataDAO DA = new DataDAO();
		ArrayList<ArrayList<String>> ExpectedTrailerInforReport = new ArrayList<ArrayList<String>>(
				DA.GetTrailerInforReport(TrailerGRID));

		for (ArrayList<String> trailer : TrailerGRID) {
			String SCAC = trailer.get(0);
			String TrailerNB = trailer.get(1);
			String ExpectedLstReportT = ExpectedTrailerInforReport.get(j).get(4);
			String ActualLstReportT = trailer.get(7);
			String ExpectedETA = ExpectedTrailerInforReport.get(j).get(6);
			String ActualETA = trailer.get(8);
			String CurrentTerminal = ExpectedTrailerInforReport.get(j).get(3);
			String Destination = ExpectedTrailerInforReport.get(j).get(2);
			++j;
			boolean FlagLst = ExpectedLstReportT.equals(ActualLstReportT);
			boolean FlagETA = ExpectedETA.equals(ActualETA);
			if (!(FlagLst && FlagETA)) {
				++i;
				System.out.println("\n" + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB
						+ " CurrentTerminal " + CurrentTerminal + " Destination " + Destination);

				fw.write(Nl + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB + " CurrentTerminal "
						+ CurrentTerminal + " Destination " + Destination);
				if (FlagLst == false) {
					fw.write("  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
					System.out.print(
							"  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
				}
				if (FlagETA == false) {
					fw.write("  " + "TTMS expected: " + ExpectedETA + " but found: " + ActualETA);
					System.out.print("  " + "TTMS expected: " + ExpectedETA + " but found: " + ActualETA);
				}

			}
		}

		System.out.println("\n" + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + "\n");
		fw.write(Nl + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + Nl);

		// get back to
		driver.close();

		driver.switchTo().window(MainWindowHandler);
	}

	@Test(priority = 4)
	public void RailLoadedForm(Method m) throws IOException, SQLException {

		Page.TotalLoadedRail.click();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> WindowHandles = driver.getWindowHandles();
		for (String windowHandle : WindowHandles) {
			if (!windowHandle.equalsIgnoreCase(MainWindowHandler)) {
				driver.switchTo().window(windowHandle);
			}
		}
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		(new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOf(Page.TotalLoadedRailForm));
		LinkedHashSet<ArrayList<String>> TrailerGRID = Page.GetTrailerReportTime(Page.TotalLoadedRailForm);
		System.out.println("\n Total Loaded Rail Form " + TrailerGRID.size());
		fw.write(Nl + " Total Loaded Rail Form " + TrailerGRID.size() + Nl);
		int i = 0;
		int j = 0;
		DataDAO DA = new DataDAO();
		ArrayList<ArrayList<String>> ExpectedTrailerInforReport = new ArrayList<ArrayList<String>>(
				DA.GetTrailerInforReport(TrailerGRID));

		for (ArrayList<String> trailer : TrailerGRID) {
			String SCAC = trailer.get(0);
			String TrailerNB = trailer.get(1);
			String ExpectedLstReportT = ExpectedTrailerInforReport.get(j).get(4);
			String ActualLstReportT = trailer.get(4);
			String ExpectedETA = ExpectedTrailerInforReport.get(j).get(6);
			String ActualETA = trailer.get(5);
			String CurrentTerminal = ExpectedTrailerInforReport.get(j).get(3);
			String Destination = ExpectedTrailerInforReport.get(j).get(2);
			String ActualTTMS = trailer.get(3);
			String ExpectedTTMS = ExpectedTrailerInforReport.get(j).get(5);
			++j;
			boolean FlagLst = ExpectedLstReportT.equals(ActualLstReportT);
			boolean FlagETA = ExpectedETA.equals(ActualETA);
			boolean FlagTTMS = ExpectedTTMS.equals(ActualTTMS);

			if (!(FlagLst && FlagETA && FlagTTMS)) {
				++i;
				System.out.println("\n" + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB
						+ " CurrentTerminal " + CurrentTerminal + " Destination " + Destination);

				fw.write(Nl + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB + " CurrentTerminal "
						+ CurrentTerminal + " Destination " + Destination);
				if (FlagLst == false) {
					fw.write("  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
					System.out.print(
							"  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
				}
				if (FlagETA == false) {
					fw.write("  " + "ETA expected: " + ExpectedETA + " but found: " + ActualETA);
					System.out.print("  " + "ETA expected: " + ExpectedETA + " but found: " + ActualETA);
				}
				if (FlagTTMS == false) {
					fw.write("  " + "TTMS expected: " + ExpectedTTMS + " but found: " + ActualTTMS);
					System.out.print("  " + "TTMS expected: " + ExpectedTTMS + " but found: " + ActualTTMS);
				}
			}
		}

		System.out.println("\n" + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + "\n");
		fw.write(Nl + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + Nl);

		// get back to
		driver.close();

		driver.switchTo().window(MainWindowHandler);
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

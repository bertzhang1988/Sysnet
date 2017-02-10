package Test;

import java.awt.AWTException;
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
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Data.CommonData;
import Data.DataDAO;
import Utility.ConfigRd;
import Utility.Function;
import Utility.SetupBrowserAndReport;
import page.SysnetPage;

public class CheckIntraRegionalReport extends SetupBrowserAndReport {

	private SysnetPage page;
	private String MainWindowHandler;

	@BeforeClass
	public void SetUp() throws AWTException, InterruptedException, IOException, SQLException {
		page = new SysnetPage(driver);
		page.Square.click();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.visibilityOf(page.SystemSummaryButton));
		page.SystemSummaryButton.click();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.visibilityOf(page.InterRegionalform));

		// get summary handler
		MainWindowHandler = driver.getWindowHandle();

	}

	@Test(priority = 1, groups = "test time")
	public void IntraRegionalAtLddTrailerReport(Method m) throws ClassNotFoundException, SQLException, IOException {

		page.IntraRegionalATLdd.click();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> WindowHandles = driver.getWindowHandles();
		for (String windowHandle : WindowHandles) {
			if (!windowHandle.equalsIgnoreCase(MainWindowHandler)) {
				driver.switchTo().window(windowHandle);
			}
		}
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		(new WebDriverWait(driver, 50))
				.until(ExpectedConditions.visibilityOf(page.IntraRegionalformATLddTrailerInforGrid));
		LinkedHashSet<ArrayList<String>> TrailerGRID = page
				.GetTrailerReportTime(page.IntraRegionalformATLddTrailerInforGrid);
		System.out.println("\n Intra-Regional ldd totally " + TrailerGRID.size());
		fw.write(Nl + " Intra-Regional ldd totally " + TrailerGRID.size() + Nl);
		int i = 0;
		int j = 0;
		DataDAO DA = new DataDAO();
		ArrayList<ArrayList<String>> ExpectedTrailerInforReport = new ArrayList<ArrayList<String>>(
				DA.GetTrailerInforReport(TrailerGRID));

		for (ArrayList<String> trailer : TrailerGRID) {
			String SCAC = trailer.get(0);
			String TrailerNB = trailer.get(1);
			String ExpectedTTMS = ExpectedTrailerInforReport.get(j).get(5);
			String ExpectedLstReportT = ExpectedTrailerInforReport.get(j).get(4);
			String ActualTTMS = trailer.get(3);
			String ActualLstReportT = trailer.get(4);
			String CurrentTerminal = ExpectedTrailerInforReport.get(j).get(3);
			String ActualSDT = trailer.get(6);
			String ExpectedSDT = ExpectedTrailerInforReport.get(j).get(7);
			String NextTerminal_1 = ExpectedTrailerInforReport.get(j).get(8);
			++j;
			boolean FlagLst = ExpectedLstReportT.equals(ActualLstReportT);
			boolean FlagTTMS = ExpectedTTMS.equals(ActualTTMS);
			boolean FlagSDT = ExpectedSDT.equals(ActualSDT);
			if (!(FlagLst && FlagTTMS && FlagSDT)) {
				++i;
				System.out.println("\n" + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB
						+ " CurrentTerminal " + CurrentTerminal+ " NextTerminal " + NextTerminal_1);

				fw.write(Nl + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB + " CurrentTerminal "
						+ CurrentTerminal+ " NextTerminal " + NextTerminal_1);
				if (FlagLst == false) {
					fw.write("  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
					System.out.print(
							"  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
				}
				if (FlagTTMS == false) {
					fw.write("  " + "TTMS expected: " + ExpectedTTMS + " but found: " + ActualTTMS);
					System.out.print("  " + "TTMS expected: " + ExpectedTTMS + " but found: " + ActualTTMS);
				}
				if (FlagSDT == false) {
					fw.write("  " + "SDT expected: " + ExpectedSDT + " but found: " + ActualSDT);
					System.out.print("  " + "SDT expected: " + ExpectedSDT + " but found: " + ActualSDT);
				}
			}
		}

		System.out.println("\n" + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + "\n");
		fw.write(Nl + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + Nl);

		// get back to
		driver.close();

		driver.switchTo().window(MainWindowHandler);
	}

	@Test(priority = 2, groups = "test time")
	public void IntraRegionalAtArrTrailerReport(Method m) throws ClassNotFoundException, SQLException, IOException {

		page.IntraRegionalATArr.click();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> WindowHandles = driver.getWindowHandles();
		for (String windowHandle : WindowHandles) {
			if (!windowHandle.equalsIgnoreCase(MainWindowHandler)) {
				driver.switchTo().window(windowHandle);
			}
		}
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		(new WebDriverWait(driver, 50))
				.until(ExpectedConditions.visibilityOf(page.IntraRegionalformATArrTrailerInforGrid));
		LinkedHashSet<ArrayList<String>> TrailerGRID = page
				.GetTrailerReportTime(page.IntraRegionalformATArrTrailerInforGrid);
		System.out.println("\n Intra-Regional ARR totally " + TrailerGRID.size());
		fw.write(Nl + " Intra-Regional ARR totally " + TrailerGRID.size() + Nl);
		int i = 0;
		int j = 0;
		DataDAO DA = new DataDAO();
		ArrayList<ArrayList<String>> ExpectedTrailerInforReport = new ArrayList<ArrayList<String>>(
				DA.GetTrailerInforReport(TrailerGRID));

		for (ArrayList<String> trailer : TrailerGRID) {
			String SCAC = trailer.get(0);
			String TrailerNB = trailer.get(1);
			String ExpectedTTMS = ExpectedTrailerInforReport.get(j).get(5);
			String ExpectedLstReportT = ExpectedTrailerInforReport.get(j).get(4);
			String ActualTTMS = trailer.get(3);
			String ActualLstReportT = trailer.get(4);
			String CurrentTerminal = ExpectedTrailerInforReport.get(j).get(3);
			String ActualSDT = trailer.get(6);
			String ExpectedSDT = ExpectedTrailerInforReport.get(j).get(7);
			String NextTerminal_1 = ExpectedTrailerInforReport.get(j).get(8);
			++j;
			boolean FlagLst = ExpectedLstReportT.equals(ActualLstReportT);
			boolean FlagTTMS = ExpectedTTMS.equals(ActualTTMS);
			boolean FlagSDT = ExpectedSDT.equals(ActualSDT);
			if (!(FlagLst && FlagTTMS && FlagSDT)) {
				++i;
				System.out.println("\n" + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB
						+ " CurrentTerminal " + CurrentTerminal+ " NextTerminal " + NextTerminal_1);

				fw.write(Nl + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB + " CurrentTerminal "
						+ CurrentTerminal+ " NextTerminal " + NextTerminal_1);
				if (FlagLst == false) {
					fw.write("  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
					System.out.print(
							"  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
				}
				if (FlagTTMS == false) {
					fw.write("  " + "TTMS expected: " + ExpectedTTMS + " but found: " + ActualTTMS);
					System.out.print("  " + "TTMS expected: " + ExpectedTTMS + " but found: " + ActualTTMS);
				}
				if (FlagSDT == false) {
					fw.write("  " + "SDT expected: " + ExpectedSDT + " but found: " + ActualSDT);
					System.out.print("  " + "SDT expected: " + ExpectedSDT + " but found: " + ActualSDT);
				}
			}
		}

		System.out.println("\n" + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + "\n");
		fw.write(Nl + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + Nl);
		// get back to
		driver.close();
		driver.switchTo().window(MainWindowHandler);
	}

	@Test(priority = 3, groups = "test time")
	public void IntraRegionalAtLddArrTrailerReport(Method m) throws ClassNotFoundException, SQLException, IOException {

		page.IntraRegionalATLddArr.click();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> WindowHandles = driver.getWindowHandles();
		for (String windowHandle : WindowHandles) {
			if (!windowHandle.equalsIgnoreCase(MainWindowHandler)) {
				driver.switchTo().window(windowHandle);
			}
		}
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		(new WebDriverWait(driver, 50))
				.until(ExpectedConditions.visibilityOf(page.IntraRegionalformATLddArvTrailerInforGrid));
		LinkedHashSet<ArrayList<String>> TrailerGRID = page
				.GetTrailerReportTime(page.IntraRegionalformATLddArvTrailerInforGrid);
		System.out.println("\n Intra-Regional LDD AND ARR totally " + TrailerGRID.size());
		fw.write(Nl + " Intra-Regional LDD AND ARR totally " + TrailerGRID.size() + Nl);
		int i = 0;
		int j = 0;
		DataDAO DA = new DataDAO();
		ArrayList<ArrayList<String>> ExpectedTrailerInforReport = new ArrayList<ArrayList<String>>(
				DA.GetTrailerInforReport(TrailerGRID));

		for (ArrayList<String> trailer : TrailerGRID) {
			String SCAC = trailer.get(0);
			String TrailerNB = trailer.get(1);
			String ExpectedTTMS = ExpectedTrailerInforReport.get(j).get(5);
			String ExpectedLstReportT = ExpectedTrailerInforReport.get(j).get(4);
			String ActualTTMS = trailer.get(3);
			String ActualLstReportT = trailer.get(4);
			String CurrentTerminal = ExpectedTrailerInforReport.get(j).get(3);
			String ActualSDT = trailer.get(6);
			String ExpectedSDT = ExpectedTrailerInforReport.get(j).get(7);
			String NextTerminal_1 = ExpectedTrailerInforReport.get(j).get(8);
			++j;
			boolean FlagLst = ExpectedLstReportT.equals(ActualLstReportT);
			boolean FlagTTMS = ExpectedTTMS.equals(ActualTTMS);
			boolean FlagSDT = ExpectedSDT.equals(ActualSDT);
			if (!(FlagLst && FlagTTMS && FlagSDT)) {
				++i;
				System.out.println("\n" + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB
						+ " CurrentTerminal " + CurrentTerminal+ " NextTerminal " + NextTerminal_1);

				fw.write(Nl + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB + " CurrentTerminal "
						+ CurrentTerminal+ " NextTerminal " + NextTerminal_1);
				if (FlagLst == false) {
					fw.write("  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
					System.out.print(
							"  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
				}
				if (FlagTTMS == false) {
					fw.write("  " + "TTMS expected: " + ExpectedTTMS + " but found: " + ActualTTMS);
					System.out.print("  " + "TTMS expected: " + ExpectedTTMS + " but found: " + ActualTTMS);
				}
				if (FlagSDT == false) {
					fw.write("  " + "SDT expected: " + ExpectedSDT + " but found: " + ActualSDT);
					System.out.print("  " + "SDT expected: " + ExpectedSDT + " but found: " + ActualSDT);
				}
			}
		}

		System.out.println("\n" + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + "\n");
		fw.write(Nl + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + Nl);
		// get back to
		driver.close();
		driver.switchTo().window(MainWindowHandler);
	}

	@Test(priority = 4, groups = "test time")
	public void IntraRegionalAtLddArrDC_EOLReport(Method m) throws ClassNotFoundException, SQLException, IOException {

		page.IntraRegionalATLddArrDC_EOL.click();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> WindowHandles = driver.getWindowHandles();
		for (String windowHandle : WindowHandles) {
			if (!windowHandle.equalsIgnoreCase(MainWindowHandler)) {
				driver.switchTo().window(windowHandle);
			}
		}
		(new WebDriverWait(driver, 100)).until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		(new WebDriverWait(driver, 50))
				.until(ExpectedConditions.visibilityOf(page.IntraRegionalformATLddArrDC_EOLInforGrid));
		LinkedHashSet<ArrayList<String>> TrailerGRID = page
				.GetTrailerReportTime(page.IntraRegionalformATLddArrDC_EOLInforGrid);
		System.out.println("\n Intra-Regional DC_EOL totally " + TrailerGRID.size());
		fw.write(Nl + " Intra-Regional DC_EOL totally " + TrailerGRID.size() + Nl);
		int i = 0;
		int j = 0;
		DataDAO DA = new DataDAO();
		ArrayList<ArrayList<String>> ExpectedTrailerInforReport = new ArrayList<ArrayList<String>>(
				DA.GetTrailerInforReport(TrailerGRID));

		for (ArrayList<String> trailer : TrailerGRID) {
			String SCAC = trailer.get(0);
			String TrailerNB = trailer.get(1);
			String ExpectedTTMS = ExpectedTrailerInforReport.get(j).get(5);
			String ExpectedLstReportT = ExpectedTrailerInforReport.get(j).get(4);
			String ActualTTMS = trailer.get(3);
			String ActualLstReportT = trailer.get(4);
			String CurrentTerminal = ExpectedTrailerInforReport.get(j).get(3);
			String ActualSDT = trailer.get(6);
			String ExpectedSDT = ExpectedTrailerInforReport.get(j).get(7);
			String NextTerminal_1 = ExpectedTrailerInforReport.get(j).get(8);
			++j;
			boolean FlagLst = ExpectedLstReportT.equals(ActualLstReportT);
			boolean FlagTTMS = ExpectedTTMS.equals(ActualTTMS);
			boolean FlagSDT = ExpectedSDT.equals(ActualSDT);
			if (!(FlagLst && FlagTTMS && FlagSDT)) {
				++i;
				System.out.println("\n" + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB
						+ " CurrentTerminal " + CurrentTerminal+ " NextTerminal " + NextTerminal_1);

				fw.write(Nl + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB + " CurrentTerminal "
						+ CurrentTerminal+ " NextTerminal " + NextTerminal_1);
				if (FlagLst == false) {
					fw.write("  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
					System.out.print(
							"  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
				}
				if (FlagTTMS == false) {
					fw.write("  " + "TTMS expected: " + ExpectedTTMS + " but found: " + ActualTTMS);
					System.out.print("  " + "TTMS expected: " + ExpectedTTMS + " but found: " + ActualTTMS);
				}
				if (FlagSDT == false) {
					fw.write("  " + "SDT expected: " + ExpectedSDT + " but found: " + ActualSDT);
					System.out.print("  " + "SDT expected: " + ExpectedSDT + " but found: " + ActualSDT);
				}
			}
		}

		System.out.println("\n" + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + "\n");
		fw.write(Nl + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + Nl);
		// get back to
		driver.close();
		driver.switchTo().window(MainWindowHandler);
	}

	@Test(priority = 5, groups = "test time")
	public void IntraRegionalAtLddArrEOL_DCReport(Method m) throws ClassNotFoundException, SQLException, IOException {

		page.IntraRegionalATLddArrEOL_DC.click();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> WindowHandles = driver.getWindowHandles();
		for (String windowHandle : WindowHandles) {
			if (!windowHandle.equalsIgnoreCase(MainWindowHandler)) {
				driver.switchTo().window(windowHandle);
			}
		}
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		(new WebDriverWait(driver, 50))
				.until(ExpectedConditions.visibilityOf(page.IntraRegionalformATLddArrEOL_DCInforGrid));
		LinkedHashSet<ArrayList<String>> TrailerGRID = page
				.GetTrailerReportTime(page.IntraRegionalformATLddArrEOL_DCInforGrid);
		System.out.println("\n Intra-Regional EOL_DC totally " + TrailerGRID.size());
		fw.write(Nl + " Intra-Regional EOL_DC totally " + TrailerGRID.size() + Nl);
		int i = 0;
		int j = 0;
		DataDAO DA = new DataDAO();
		ArrayList<ArrayList<String>> ExpectedTrailerInforReport = new ArrayList<ArrayList<String>>(
				DA.GetTrailerInforReport(TrailerGRID));

		for (ArrayList<String> trailer : TrailerGRID) {
			String SCAC = trailer.get(0);
			String TrailerNB = trailer.get(1);
			String ExpectedTTMS = ExpectedTrailerInforReport.get(j).get(5);
			String ExpectedLstReportT = ExpectedTrailerInforReport.get(j).get(4);
			String ActualTTMS = trailer.get(3);
			String ActualLstReportT = trailer.get(4);
			String CurrentTerminal = ExpectedTrailerInforReport.get(j).get(3);
			String ActualSDT = trailer.get(6);
			String ExpectedSDT = ExpectedTrailerInforReport.get(j).get(7);
			String NextTerminal_1 = ExpectedTrailerInforReport.get(j).get(8);
			++j;
			boolean FlagLst = ExpectedLstReportT.equals(ActualLstReportT);
			boolean FlagTTMS = ExpectedTTMS.equals(ActualTTMS);
			boolean FlagSDT = ExpectedSDT.equals(ActualSDT);
			if (!(FlagLst && FlagTTMS && FlagSDT)) {
				++i;
				System.out.println("\n" + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB
						+ " CurrentTerminal " + CurrentTerminal+ " NextTerminal " + NextTerminal_1);

				fw.write(Nl + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB + " CurrentTerminal "
						+ CurrentTerminal+ " NextTerminal " + NextTerminal_1);
				if (FlagLst == false) {
					fw.write("  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
					System.out.print(
							"  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
				}
				if (FlagTTMS == false) {
					fw.write("  " + "TTMS expected: " + ExpectedTTMS + " but found: " + ActualTTMS);
					System.out.print("  " + "TTMS expected: " + ExpectedTTMS + " but found: " + ActualTTMS);
				}
				if (FlagSDT == false) {
					fw.write("  " + "SDT expected: " + ExpectedSDT + " but found: " + ActualSDT);
					System.out.print("  " + "SDT expected: " + ExpectedSDT + " but found: " + ActualSDT);
				}
			}
		}
		System.out.println("\n" + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + "\n");
		fw.write(Nl + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + Nl);
		// get back to
		driver.close();
		driver.switchTo().window(MainWindowHandler);
	}

	@Test(priority = 6, groups = "test time")
	public void IntraRegionalRoadEmptiesTrailerReport(Method m)
			throws ClassNotFoundException, SQLException, IOException {

		page.IntraRegionalRoadEmpties.click();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> WindowHandles = driver.getWindowHandles();
		for (String windowHandle : WindowHandles) {
			if (!windowHandle.equalsIgnoreCase(MainWindowHandler)) {
				driver.switchTo().window(windowHandle);
			}
		}
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		(new WebDriverWait(driver, 50))
				.until(ExpectedConditions.visibilityOf(page.IntraRegionalRoadEmptiesTrailerInforGrid));
		LinkedHashSet<ArrayList<String>> TrailerGRID = page
				.GetTrailerReportTime(page.IntraRegionalRoadEmptiesTrailerInforGrid);
		System.out.println("\n Intra-Regional Road Empties totally " + TrailerGRID.size());
		fw.write(Nl + " Intra-Regional Road Empties totally " + TrailerGRID.size() + Nl);
		int i = 0;
		int j = 0;
		DataDAO DA = new DataDAO();
		ArrayList<ArrayList<String>> ExpectedTrailerInforReport = new ArrayList<ArrayList<String>>(
				DA.GetTrailerInforReport(TrailerGRID));

		for (ArrayList<String> trailer : TrailerGRID) {
			String SCAC = trailer.get(0);
			String TrailerNB = trailer.get(1);
			String ExpectedLstReportT = ExpectedTrailerInforReport.get(j).get(4);
			String ExpectedETA = ExpectedTrailerInforReport.get(j).get(6);
			String CurrentTerminal = ExpectedTrailerInforReport.get(j).get(3);
			//String Destination = ExpectedTrailerInforReport.get(j).get(2);
			String ActualLstReportT = trailer.get(4);
			String ActualETA = trailer.get(5);
			String ActualSDT = trailer.get(6);
			String ExpectedSDT = ExpectedTrailerInforReport.get(j).get(7);
			String NextTerminal_1 = ExpectedTrailerInforReport.get(j).get(8);
			++j;
			boolean FlagLst = ExpectedLstReportT.equals(ActualLstReportT);
			boolean FlagETA = ExpectedETA.equals(ActualETA);
			boolean FlagSDT = ExpectedSDT.equals(ActualSDT);
			if (!(FlagLst && FlagETA && FlagSDT)) {
				++i;
				System.out.println("\n" + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB
						+ " CurrentTerminal " + CurrentTerminal + " NextTerminal " + NextTerminal_1);

				fw.write(Nl + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB + " CurrentTerminal "
						+ CurrentTerminal + " NextTerminal " + NextTerminal_1);
				if (FlagLst == false) {
					fw.write("  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
					System.out.print(
							"  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
				}
				if (FlagETA == false) {
					fw.write("  " + "TTMS expected: " + ExpectedETA + " but found: " + ActualETA);
					System.out.print("  " + "TTMS expected: " + ExpectedETA + " but found: " + ActualETA);
				}
				if (FlagSDT == false) {
					fw.write("  " + "SDT expected: " + ExpectedSDT + " but found: " + ActualSDT);
					System.out.print("  " + "SDT expected: " + ExpectedSDT + " but found: " + ActualSDT);
				}
			}
		}

		System.out.println("\n" + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + "\n");
		fw.write(Nl + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + Nl);
		// get back to
		driver.close();
		driver.switchTo().window(MainWindowHandler);
	}

	@Test(priority = 7)
	public void VerifySchedulesForIntraRegionalRoadEmpty() throws SQLException, IOException {
		(new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOf(page.IntraRegionalRoadEmpties));
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		fw.write(Nl + "intra-regional road schedule: ");
		// check load rail schedule
		String ScheduleL = page.IntraRegionalRoadEmpties.findElement(By.xpath("following-sibling::td")).getText();
		String ExpectedShedulel = CommonData.GetScheduleForIntraRegionalRoadEmpty();
		if (ScheduleL.equals(ExpectedShedulel)) {
			fw.write(Nl + "the result is correct");
		} else {
			fw.write(Nl + "expected Shedule of loaded Rail: " + ExpectedShedulel + " but found: " + ScheduleL);
		}
	}

	@AfterMethod(groups = { "test time" })
	public void CheckFailure(ITestResult result) {

		if (result.getStatus() == ITestResult.FAILURE) {
			if(!driver.getWindowHandle().equals(MainWindowHandler))
			driver.close();
			driver.switchTo().window(MainWindowHandler);
		}
	}

	@AfterClass
	public void Close() {

	}
}

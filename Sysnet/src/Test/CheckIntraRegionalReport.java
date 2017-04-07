package Test;

import java.awt.AWTException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
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
import Utility.SetUpBase.SetupBrowserAndTextReport;
import page.SysnetPage;

/*
 check system summary intra-regional screen, only verify the time field
 * */
public class CheckIntraRegionalReport extends SetupBrowserAndTextReport {

	private SysnetPage page;
	private String MainWindowHandler;
	private WebDriverWait wait1;
	private DataDAO DA;

	@BeforeClass
	public void SetUp() throws AWTException, InterruptedException, IOException, SQLException {
		// navigate to system summary page
		DA = new DataDAO();
		page = new SysnetPage(driver);
		if (!page.isVisable(page.SystemSummaryButton))
			page.Square.click();
		wait1 = new WebDriverWait(driver, 50);
		wait1.until(ExpectedConditions.visibilityOf(page.SystemSummaryButton));
		page.SystemSummaryButton.click();
		wait1.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		wait1.until(ExpectedConditions.visibilityOf(page.InterRegionalform));
		if (page.isVisable(page.SystemSummaryButton) && page.isVisable(page.Square))
			page.Square.click();
		// get window handler of summary page
		MainWindowHandler = driver.getWindowHandle();

	}

	@Test(priority = 1, groups = "test time", description = "Average Time on LDD Status in Hours")
	public void IntraRegionalAtLddTrailerReport(Method m) throws ClassNotFoundException, SQLException, IOException {
		// navigate to LDD page
		page.IntraRegionalATLdd.click();
		wait1.until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> WindowHandles = driver.getWindowHandles();
		for (String windowHandle : WindowHandles) {
			if (!windowHandle.equalsIgnoreCase(MainWindowHandler)) {
				driver.switchTo().window(windowHandle);
			}
		}
		wait1.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		wait1.until(ExpectedConditions.visibilityOf(page.IntraRegionalformATLddTrailerInforGrid));
		// grab all time columns from the screen
		LinkedHashSet<ArrayList<String>> TrailerGRID = page
				.GetTrailerReportTime(page.IntraRegionalformATLddTrailerInforGrid);
		System.out.println("\n Intra-Regional ldd totally " + TrailerGRID.size());
		// write the header to report
		fw.write(Nl + " Intra-Regional ldd totally " + TrailerGRID.size() + Nl);
		int i = 0;
		int j = 0;
		// get all expected result of time
		ArrayList<ArrayList<String>> ExpectedTrailerInforReport = new ArrayList<ArrayList<String>>(
				DA.GetTrailerInforReport(TrailerGRID));
		// do comparison and write in the report
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
						+ " CurrentTerminal " + CurrentTerminal + " NextTerminal " + NextTerminal_1);

				fw.write(Nl + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB + " CurrentTerminal "
						+ CurrentTerminal + " NextTerminal " + NextTerminal_1);
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
		// write summary of report
		System.out.println("\n" + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + "\n");
		fw.write(Nl + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + Nl);

		// get back to system summary page
		driver.close();

		driver.switchTo().window(MainWindowHandler);
	}

	@Test(priority = 2, groups = "test time", description = "Average Time on ARR Status in Hours")
	public void IntraRegionalAtArrTrailerReport(Method m) throws ClassNotFoundException, SQLException, IOException {
		// navigate to ARR status page
		page.IntraRegionalATArr.click();
		wait1.until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> WindowHandles = driver.getWindowHandles();
		for (String windowHandle : WindowHandles) {
			if (!windowHandle.equalsIgnoreCase(MainWindowHandler)) {
				driver.switchTo().window(windowHandle);
			}
		}
		wait1.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		wait1.until(ExpectedConditions.visibilityOf(page.IntraRegionalformATArrTrailerInforGrid));
		// get all time stamp columns from screen
		LinkedHashSet<ArrayList<String>> TrailerGRID = page
				.GetTrailerReportTime(page.IntraRegionalformATArrTrailerInforGrid);
		System.out.println("\n Intra-Regional ARR totally " + TrailerGRID.size());
		// write report, do validation
		fw.write(Nl + " Intra-Regional ARR totally " + TrailerGRID.size() + Nl);
		int i = 0;
		int j = 0;
		// get expected result
		ArrayList<ArrayList<String>> ExpectedTrailerInforReport = new ArrayList<ArrayList<String>>(
				DA.GetTrailerInforReport(TrailerGRID));
		// validation
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
						+ " CurrentTerminal " + CurrentTerminal + " NextTerminal " + NextTerminal_1);

				fw.write(Nl + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB + " CurrentTerminal "
						+ CurrentTerminal + " NextTerminal " + NextTerminal_1);
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
		// write summary of reprot
		System.out.println("\n" + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + "\n");
		fw.write(Nl + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + Nl);
		// get back to system summary page
		driver.close();
		driver.switchTo().window(MainWindowHandler);
	}

	@Test(priority = 3, groups = "test time", description = "Average Time on LDD+ARR Status in Hours")
	public void IntraRegionalAtLddArrTrailerReport(Method m) throws ClassNotFoundException, SQLException, IOException {
		// navigate to LDD + ARR page
		page.IntraRegionalATLddArr.click();
		wait1.until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> WindowHandles = driver.getWindowHandles();
		for (String windowHandle : WindowHandles) {
			if (!windowHandle.equalsIgnoreCase(MainWindowHandler)) {
				driver.switchTo().window(windowHandle);
			}
		}
		wait1.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		wait1.until(ExpectedConditions.visibilityOf(page.IntraRegionalformATLddArvTrailerInforGrid));
		// get all time value from the screen
		LinkedHashSet<ArrayList<String>> TrailerGRID = page
				.GetTrailerReportTime(page.IntraRegionalformATLddArvTrailerInforGrid);
		// validation and write report
		System.out.println("\n Intra-Regional LDD AND ARR totally " + TrailerGRID.size());
		fw.write(Nl + " Intra-Regional LDD AND ARR totally " + TrailerGRID.size() + Nl);
		int i = 0;
		int j = 0;
		// get expected time result by calculation from database
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
						+ " CurrentTerminal " + CurrentTerminal + " NextTerminal " + NextTerminal_1);

				fw.write(Nl + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB + " CurrentTerminal "
						+ CurrentTerminal + " NextTerminal " + NextTerminal_1);
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
		// write summary of report
		System.out.println("\n" + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + "\n");
		fw.write(Nl + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + Nl);
		// get back to system summary
		driver.close();
		driver.switchTo().window(MainWindowHandler);
	}

	@Test(priority = 4, groups = "test time", description = "Total Intra-Regional LDD + ARR DC-EOL Loads")
	public void IntraRegionalAtLddArrDC_EOLReport(Method m) throws ClassNotFoundException, SQLException, IOException {
		// navigate to LDD+ARR DC-EOL
		page.IntraRegionalATLddArrDC_EOL.findElement(By.xpath("following-sibling::td")).click();
		wait1.until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> WindowHandles = driver.getWindowHandles();
		for (String windowHandle : WindowHandles) {
			if (!windowHandle.equalsIgnoreCase(MainWindowHandler)) {
				driver.switchTo().window(windowHandle);
			}
		}
		wait1.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		wait1.until(ExpectedConditions.visibilityOf(page.IntraRegionalformATLddArrDC_EOLInforGrid));
		// get time value from the screen
		LinkedHashSet<ArrayList<String>> TrailerGRID = page
				.GetTrailerReportTime(page.IntraRegionalformATLddArrDC_EOLInforGrid);
		System.out.println("\n Intra-Regional DC_EOL totally " + TrailerGRID.size());
		// validation and generate report
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
						+ " CurrentTerminal " + CurrentTerminal + " NextTerminal " + NextTerminal_1);

				fw.write(Nl + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB + " CurrentTerminal "
						+ CurrentTerminal + " NextTerminal " + NextTerminal_1);
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
		// write summary of report
		System.out.println("\n" + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + "\n");
		fw.write(Nl + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + Nl);
		// get back to system summary page
		driver.close();
		driver.switchTo().window(MainWindowHandler);
	}

	@Test(priority = 5, groups = "test time", description = "Total Intra-Regional LDD + ARR EOL-DC Loads")
	public void IntraRegionalAtLddArrEOL_DCReport(Method m) throws ClassNotFoundException, SQLException, IOException {
		// navigate LDD+ARR EOL-DC page
		page.IntraRegionalATLddArrEOL_DC.findElement(By.xpath("following-sibling::td")).click();
		wait1.until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> WindowHandles = driver.getWindowHandles();
		for (String windowHandle : WindowHandles) {
			if (!windowHandle.equalsIgnoreCase(MainWindowHandler)) {
				driver.switchTo().window(windowHandle);
			}
		}
		wait1.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		wait1.until(ExpectedConditions.visibilityOf(page.IntraRegionalformATLddArrEOL_DCInforGrid));
		// get the tiem value
		LinkedHashSet<ArrayList<String>> TrailerGRID = page
				.GetTrailerReportTime(page.IntraRegionalformATLddArrEOL_DCInforGrid);
		// validation time and write result to report
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
						+ " CurrentTerminal " + CurrentTerminal + " NextTerminal " + NextTerminal_1);

				fw.write(Nl + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB + " CurrentTerminal "
						+ CurrentTerminal + " NextTerminal " + NextTerminal_1);
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
		// get back to system summary page
		driver.close();
		driver.switchTo().window(MainWindowHandler);
	}

	@Test(priority = 6, groups = "test time", description = "Total Intra-Regional Road Empties")
	public void IntraRegionalRoadEmptiesTrailerReport(Method m)
			throws ClassNotFoundException, SQLException, IOException {
		// navigate to intra-regional road empites page
		page.IntraRegionalRoadEmpties.click();
		wait1.until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> WindowHandles = driver.getWindowHandles();
		for (String windowHandle : WindowHandles) {
			if (!windowHandle.equalsIgnoreCase(MainWindowHandler)) {
				driver.switchTo().window(windowHandle);
			}
		}
		wait1.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		wait1.until(ExpectedConditions.visibilityOf(page.IntraRegionalRoadEmptiesTrailerInforGrid));
		// get all time value from screen
		LinkedHashSet<ArrayList<String>> TrailerGRID = page
				.GetTrailerReportTime(page.IntraRegionalRoadEmptiesTrailerInforGrid);
		// validation time result and write report
		System.out.println("\n Intra-Regional Road Empties totally " + TrailerGRID.size());
		fw.write(Nl + " Intra-Regional Road Empties totally " + TrailerGRID.size() + Nl);
		int i = 0;
		int j = 0;
		ArrayList<ArrayList<String>> ExpectedTrailerInforReport = new ArrayList<ArrayList<String>>(
				DA.GetTrailerInforReport(TrailerGRID));

		for (ArrayList<String> trailer : TrailerGRID) {
			String SCAC = trailer.get(0);
			String TrailerNB = trailer.get(1);
			String ExpectedLstReportT = ExpectedTrailerInforReport.get(j).get(4);
			String ExpectedETA = ExpectedTrailerInforReport.get(j).get(6);
			String CurrentTerminal = ExpectedTrailerInforReport.get(j).get(3);
			// String Destination = ExpectedTrailerInforReport.get(j).get(2);
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
		// write report summary
		System.out.println("\n" + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + "\n");
		fw.write(Nl + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + Nl);
		// get back to system summary page
		driver.close();
		driver.switchTo().window(MainWindowHandler);
	}

	@Test(priority = 7, description = "Verify Schedules For Intra-Regional Road Empty")
	public void VerifySchedulesForIntraRegionalRoadEmpty() throws SQLException, IOException {
		wait1.until(ExpectedConditions.visibilityOf(page.IntraRegionalRoadEmpties));
		wait1.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		fw.write(Nl + "intra-regional road schedule: ");
		// check load rail schedule
		String ScheduleL = page.IntraRegionalRoadEmpties.findElement(By.xpath("following-sibling::td")).getText();
		// get expected schedule
		String ExpectedShedulel = CommonData.GetScheduleForIntraRegionalRoadEmpty();
		// do validation and write to report
		if (ScheduleL.equals(ExpectedShedulel)) {
			fw.write(Nl + "the result is correct");
		} else {
			fw.write(Nl + "expected Shedule of loaded Rail: " + ExpectedShedulel + " but found: " + ScheduleL);
		}
	}

	@AfterMethod(groups = { "test time" })
	public void CheckFailure(ITestResult result) {
		// if failure of test then get back to system summary page
		if (result.getStatus() == ITestResult.FAILURE) {
			if (!driver.getWindowHandle().equals(MainWindowHandler))
				driver.close();
			driver.switchTo().window(MainWindowHandler);
		}
	}

	@AfterClass
	public void Close() {

	}
}

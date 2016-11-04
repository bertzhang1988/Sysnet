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

import Data.DataDAO;
import Utility.ConfigRd;
import Utility.Function;
import Utility.SetupBase;
import page.SysnetPage;

public class CheckIntraRegionalReport2 extends SetupBase {

	private SysnetPage page;
	private String Nl;
	private FileWriter fw;
	private String MainWindowHandler;

	@BeforeClass
	public void SetUp() throws AWTException, InterruptedException, IOException, SQLException {
		ConfigRd Conf = new ConfigRd();
		page = new SysnetPage(driver);
		driver.get(Conf.GetSysnetSitURL());
		driver.manage().window().maximize();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.visibilityOf(page.SystemSummaryButton));
		page.SystemSummaryButton.click();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.visibilityOf(page.InterRegionalform));

		// get summary handler
		MainWindowHandler = driver.getWindowHandle();

		// create text file
		String CDate = Function.GetTimeValue(TimeZone.getDefault().getID());
		File file2 = new File("./Report/" + CDate);
		file2.mkdir();
		File file = new File(file2, this.getClass().getName() + ".txt");
		fw = new FileWriter(file, true);
		Nl = System.getProperty("line.separator");

	}

	@Test(priority = 1)
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
			String CurrentTerminal = trailer.get(2);
			j = j + 1;
			boolean FlagLst = ExpectedLstReportT.equals(ActualLstReportT);
			boolean FlagTTMS = ExpectedTTMS.equals(ActualTTMS);
			if (!(FlagLst && FlagTTMS)) {
				i = i + 1;
				System.out.println("\n" + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB
						+ " CurrentTerminal " + CurrentTerminal);

				fw.write(Nl + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB + " CurrentTerminal "
						+ CurrentTerminal);
				if (FlagLst == false) {
					fw.write("  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
					System.out.print(
							"  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
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

	@Test(priority = 2)
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
			String CurrentTerminal = trailer.get(2);
			j = j + 1;
			boolean FlagLst = ExpectedLstReportT.equals(ActualLstReportT);
			boolean FlagTTMS = ExpectedTTMS.equals(ActualTTMS);
			if (!(FlagLst && FlagTTMS)) {
				i = i + 1;
				System.out.println("\n" + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB
						+ " CurrentTerminal " + CurrentTerminal);

				fw.write(Nl + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB + " CurrentTerminal "
						+ CurrentTerminal);
				if (FlagLst == false) {
					fw.write("  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
					System.out.print(
							"  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
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

	@Test(priority = 3)
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
			String CurrentTerminal = trailer.get(2);
			j = j + 1;
			boolean FlagLst = ExpectedLstReportT.equals(ActualLstReportT);
			boolean FlagTTMS = ExpectedTTMS.equals(ActualTTMS);
			if (!(FlagLst && FlagTTMS)) {
				i = i + 1;
				System.out.println("\n" + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB
						+ " CurrentTerminal " + CurrentTerminal);

				fw.write(Nl + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB + " CurrentTerminal "
						+ CurrentTerminal);
				if (FlagLst == false) {
					fw.write("  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
					System.out.print(
							"  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
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

	@Test(priority = 4)
	public void IntraRegionalAtLddArrDC_SATReport(Method m) throws ClassNotFoundException, SQLException, IOException {

		page.IntraRegionalATLddArrDC_SAT.click();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> WindowHandles = driver.getWindowHandles();
		for (String windowHandle : WindowHandles) {
			if (!windowHandle.equalsIgnoreCase(MainWindowHandler)) {
				driver.switchTo().window(windowHandle);
			}
		}
		(new WebDriverWait(driver, 100)).until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		(new WebDriverWait(driver, 50))
				.until(ExpectedConditions.visibilityOf(page.IntraRegionalformATLddArrDC_SATInforGrid));
		LinkedHashSet<ArrayList<String>> TrailerGRID = page
				.GetTrailerReportTime(page.IntraRegionalformATLddArrDC_SATInforGrid);
		System.out.println("\n Intra-Regional DC_SAT totally " + TrailerGRID.size());
		fw.write(Nl + " Intra-Regional DC_SAT totally " + TrailerGRID.size() + Nl);
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
			String CurrentTerminal = trailer.get(2);
			j = j + 1;
			boolean FlagLst = ExpectedLstReportT.equals(ActualLstReportT);
			boolean FlagTTMS = ExpectedTTMS.equals(ActualTTMS);
			if (!(FlagLst && FlagTTMS)) {
				i = i + 1;
				System.out.println("\n" + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB
						+ " CurrentTerminal " + CurrentTerminal);

				fw.write(Nl + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB + " CurrentTerminal "
						+ CurrentTerminal);
				if (FlagLst == false) {
					fw.write("  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
					System.out.print(
							"  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
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

	@Test(priority = 5)
	public void IntraRegionalAtLddArrSAT_DCReport(Method m) throws ClassNotFoundException, SQLException, IOException {

		page.IntraRegionalATLddArrSAT_DC.click();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> WindowHandles = driver.getWindowHandles();
		for (String windowHandle : WindowHandles) {
			if (!windowHandle.equalsIgnoreCase(MainWindowHandler)) {
				driver.switchTo().window(windowHandle);
			}
		}
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		(new WebDriverWait(driver, 50))
				.until(ExpectedConditions.visibilityOf(page.IntraRegionalformATLddArrSAT_DCInforGrid));
		LinkedHashSet<ArrayList<String>> TrailerGRID = page
				.GetTrailerReportTime(page.IntraRegionalformATLddArrSAT_DCInforGrid);
		System.out.println("\n Intra-Regional SAT_DC totally " + TrailerGRID.size());
		fw.write(Nl + " Intra-Regional SAT_DC totally " + TrailerGRID.size() + Nl);
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
			String CurrentTerminal = trailer.get(2);
			j = j + 1;
			boolean FlagLst = ExpectedLstReportT.equals(ActualLstReportT);
			boolean FlagTTMS = ExpectedTTMS.equals(ActualTTMS);
			if (!(FlagLst && FlagTTMS)) {
				i = i + 1;
				System.out.println("\n" + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB
						+ " CurrentTerminal " + CurrentTerminal);

				fw.write(Nl + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB + " CurrentTerminal "
						+ CurrentTerminal);
				if (FlagLst == false) {
					fw.write("  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
					System.out.print(
							"  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
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

	@Test(priority = 6)
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
				.GetTrailerReportList(page.IntraRegionalRoadEmptiesTrailerInforGrid);
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
			String Destination = ExpectedTrailerInforReport.get(j).get(2);
			String ActualLstReportT = trailer.get(7);
			String ActualETA = trailer.get(8);
			j = j + 1;
			boolean FlagLst = ExpectedLstReportT.equals(ActualLstReportT);
			boolean FlagETA = ExpectedETA.equals(ActualETA);
			if (!(FlagLst && FlagETA)) {
				i = i + 1;
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

	@AfterMethod
	public void CheckFailure(ITestResult result) {

		if (result.getStatus() == ITestResult.FAILURE) {
			driver.close();
			driver.switchTo().window(MainWindowHandler);
		}
	}

	@AfterClass
	public void Close() {

		try {
			fw.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}

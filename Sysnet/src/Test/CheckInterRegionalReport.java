package Test;

import java.awt.AWTException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TimeZone;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import Utility.SetUpBase.SetupBase;
import page.SysnetPage;

/*
check system summary inter-regional screen, only verify the time field
 */
public class CheckInterRegionalReport extends SetupBase {

	private SysnetPage page;
	private TimeZone defaultTimeZone;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private int R;
	private DataDAO DA;
	private String MainWindowHandler;
	private WebDriverWait wait1;

	@BeforeClass
	public void SetUp() throws AWTException, InterruptedException, IOException, SQLException {
		// read the property file
		ConfigRd Conf = new ConfigRd();
		DA = new DataDAO();
		page = new SysnetPage(driver);
		driver.get(Conf.GetSysnetURL());
		wait1 = new WebDriverWait(driver, 150);
		driver.manage().window().maximize();
		// check all ready to click the System summary button
		if (!page.isVisable(page.SystemSummaryButton))
			page.Square.click();
		wait1.until(ExpectedConditions.visibilityOf(page.SystemSummaryButton));
		// click the summary button, wait the summary page fully loaded
		page.SystemSummaryButton.click();
		wait1.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		wait1.until(ExpectedConditions.visibilityOf(page.InterRegionalform));
		if (page.isVisable(page.SystemSummaryButton) && page.isVisable(page.Square))
			page.Square.click();
		// get time zone
		defaultTimeZone = TimeZone.getDefault();

		// get summary handler
		MainWindowHandler = driver.getWindowHandle();

		// create excel sheet and title
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet(Function.GetDisplayTime().replace(":", "-"));
		String[] TitleLine = { "Line", "Time is wrong for trailer", " CurrentTerminal", "LstReport time expected:",
				"but found: ", "TTMS expected: ", " but found: ", " NextTerminal: ", " ETA expected: ", "but found: ",
				" SDT expected: ", "but found: " };
		Row r = sheet.createRow(R);
		int ColumnOfFirstline = 0;
		for (String value : TitleLine) {
			Cell CellOfFirstRow = r.createCell(ColumnOfFirstline);
			CellOfFirstRow.setCellValue(value);
			ColumnOfFirstline++;
		}
	}

	@Test(priority = 1, groups = "check time", description = "Average Time on LDD Status in Hours")
	public void InterRegionalAtLddTrailerReport(Method m) throws ClassNotFoundException, SQLException {
		// navigate to LDD page
		page.InterRegionalATLdd.click();
		wait1.until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> WindowHandles = driver.getWindowHandles();
		for (String windowHandle : WindowHandles) {
			if (!windowHandle.equalsIgnoreCase(MainWindowHandler)) {
				driver.switchTo().window(windowHandle);
			}
		}
		wait1.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		wait1.until(ExpectedConditions.visibilityOf(page.InterRegionalATLddTrailerInforGrid));
		// grab all time columns from the screen
		LinkedHashSet<ArrayList<String>> TrailerGRID = page
				.GetTrailerReportTime(page.InterRegionalATLddTrailerInforGrid);
		System.out.println("\n Inter-Regional ldd totally " + TrailerGRID.size());
		// create header of the report
		Row subtitle = sheet.createRow(++R);
		subtitle.createCell(0).setCellValue("Inter-Regional ldd totally :");
		subtitle.createCell(1).setCellValue(TrailerGRID.size());
		int i = 0;
		int j = 0;
		// grab all expected result of time columns
		ArrayList<ArrayList<String>> ExpectedTrailerInforReport = new ArrayList<ArrayList<String>>(
				DA.GetTrailerInforReport(TrailerGRID));
		// validate the time columns and write into the report
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
				Row r = sheet.createRow(++R);
				r.createCell(0).setCellValue(j);
				r.createCell(1).setCellValue(SCAC + "-" + TrailerNB);
				r.createCell(2).setCellValue(CurrentTerminal);
				r.createCell(7).setCellValue(NextTerminal_1);
				System.out.println("\n" + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB
						+ " CurrentTerminal " + CurrentTerminal + " NextTerminal " + NextTerminal_1);
				if (FlagLst == false) {
					r.createCell(3).setCellValue(ExpectedLstReportT);
					r.createCell(4).setCellValue(ActualLstReportT);
					System.out.printf(
							"  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
				}
				if (FlagTTMS == false) {
					r.createCell(5).setCellValue(ExpectedTTMS);
					r.createCell(6).setCellValue(ActualTTMS);
					System.out.printf("  " + "TTMS expected: " + ExpectedTTMS + " but found: " + ActualTTMS);
				}
				if (FlagSDT == false) {
					r.createCell(10).setCellValue(ExpectedSDT);
					r.createCell(11).setCellValue(ActualSDT);
					System.out.printf("  " + "SDT expected: " + ExpectedSDT + " but found: " + ActualSDT);
				}

			}
		}
		// write the summary of the report
		System.out.println("\n" + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + "\n");
		Row summary = sheet.createRow(++R);
		summary.createCell(0).setCellValue(m.getName() + " form totally: ");
		summary.createCell(1).setCellValue(TrailerGRID.size());
		summary.createCell(2).setCellValue("  mismatch :");
		summary.createCell(3).setCellValue(i);
		sheet.createRow(++R);
		// get back to system summary page
		driver.close();
		driver.switchTo().window(MainWindowHandler);
	}

	@Test(priority = 2, groups = "check time", description = "Average Time on ARR Status in Hours")
	public void InterRegionalAtArrTrailerReport(Method m) throws ClassNotFoundException, SQLException {
		// navigate to ARR page
		page.InterRegionalATArr.click();
		wait1.until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> WindowHandles = driver.getWindowHandles();
		for (String windowHandle : WindowHandles) {
			if (!windowHandle.equalsIgnoreCase(MainWindowHandler)) {
				driver.switchTo().window(windowHandle);
			}
		}
		wait1.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		wait1.until(ExpectedConditions.visibilityOf(page.InterRegionalATArrTrailerInforGrid));
		// get actual result from the screen
		LinkedHashSet<ArrayList<String>> TrailerGRID = page
				.GetTrailerReportTime(page.InterRegionalATArrTrailerInforGrid);
		System.out.println("\n Inter-Regional ARR totally " + TrailerGRID.size());
		Row subtitle = sheet.createRow(++R);
		subtitle.createCell(0).setCellValue("Inter-Regional ARR totally :");
		subtitle.createCell(1).setCellValue(TrailerGRID.size());
		int i = 0;
		int j = 0;
		// 1. get expected result, 2. validate the result, 3. write into the
		// report
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
				Row r = sheet.createRow(++R);
				r.createCell(0).setCellValue(j);
				r.createCell(1).setCellValue(SCAC + "-" + TrailerNB);
				r.createCell(2).setCellValue(CurrentTerminal);
				r.createCell(7).setCellValue(NextTerminal_1);
				System.out.println("\n" + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB
						+ " CurrentTerminal " + CurrentTerminal + " NextTerminal " + NextTerminal_1);
				if (FlagLst == false) {
					r.createCell(3).setCellValue(ExpectedLstReportT);
					r.createCell(4).setCellValue(ActualLstReportT);
					System.out.printf(
							"  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
				}
				if (FlagTTMS == false) {
					r.createCell(5).setCellValue(ExpectedTTMS);
					r.createCell(6).setCellValue(ActualTTMS);
					System.out.printf("  " + "TTMS expected: " + ExpectedTTMS + " but found: " + ActualTTMS);
				}

				if (FlagSDT == false) {
					r.createCell(10).setCellValue(ExpectedSDT);
					r.createCell(11).setCellValue(ActualSDT);
					System.out.printf("  " + "SDT expected: " + ExpectedSDT + " but found: " + ActualSDT);
				}
			}
		}
		// write summary of the report
		System.out.println("\n" + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + "\n");
		Row summary = sheet.createRow(++R);
		summary.createCell(0).setCellValue(m.getName() + " form totally: ");
		summary.createCell(1).setCellValue(TrailerGRID.size());
		summary.createCell(2).setCellValue("  mismatch :");
		summary.createCell(3).setCellValue(i);
		sheet.createRow(++R);
		// get back to system summary page
		driver.close();
		driver.switchTo().window(MainWindowHandler);
	}

	@Test(priority = 3, groups = "check time", description = "Average Time for LDD + ARR in Hours")
	public void InterRegionalAtLddArrTrailerReport(Method m) throws ClassNotFoundException, SQLException {
		// navigate to LDD+ARR page
		page.InterRegionalATLddArr.click();
		wait1.until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> WindowHandles = driver.getWindowHandles();
		for (String windowHandle : WindowHandles) {
			if (!windowHandle.equalsIgnoreCase(MainWindowHandler)) {
				driver.switchTo().window(windowHandle);
			}
		}
		wait1.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		wait1.until(ExpectedConditions.visibilityOf(page.InterRegionalATLddArrTrailerInforGrid));
		// get actual time value from screen
		LinkedHashSet<ArrayList<String>> TrailerGRID = page
				.GetTrailerReportTime(page.InterRegionalATLddArrTrailerInforGrid);
		System.out.println("\n Inter-Regional LDD AND ARR totally " + TrailerGRID.size());
		Row subtitle = sheet.createRow(++R);
		subtitle.createCell(0).setCellValue("Inter-Regional LDD AND ARR totally :");
		subtitle.createCell(1).setCellValue(TrailerGRID.size());
		int i = 0;
		int j = 0;
		// 1. get expected result, 2. validate the result, 3. write into the
		// report
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
				Row r = sheet.createRow(++R);
				r.createCell(0).setCellValue(j);
				r.createCell(1).setCellValue(SCAC + "-" + TrailerNB);
				r.createCell(2).setCellValue(CurrentTerminal);
				r.createCell(7).setCellValue(NextTerminal_1);
				System.out.println("\n" + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB
						+ " CurrentTerminal " + CurrentTerminal + " NextTerminal " + NextTerminal_1);
				if (FlagLst == false) {
					r.createCell(3).setCellValue(ExpectedLstReportT);
					r.createCell(4).setCellValue(ActualLstReportT);
					System.out.printf(
							"  " + "Lst Result expected: " + ExpectedLstReportT + " but found: " + ActualLstReportT);
				}
				if (FlagTTMS == false) {
					r.createCell(5).setCellValue(ExpectedTTMS);
					r.createCell(6).setCellValue(ActualTTMS);
					System.out.printf("  " + "TTMS expected: " + ExpectedTTMS + " but found: " + ActualTTMS);
				}
				if (FlagSDT == false) {
					r.createCell(10).setCellValue(ExpectedSDT);
					r.createCell(11).setCellValue(ActualSDT);
					System.out.printf("  " + "SDT expected: " + ExpectedSDT + " but found: " + ActualSDT);
				}

			}
		}
		// write summary of the report
		System.out.println("\n" + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + "\n");
		Row summary = sheet.createRow(++R);
		summary.createCell(0).setCellValue(m.getName() + " form totally: ");
		summary.createCell(1).setCellValue(TrailerGRID.size());
		summary.createCell(2).setCellValue("  mismatch :");
		summary.createCell(3).setCellValue(i);
		sheet.createRow(++R);
		// get back to
		driver.close();
		driver.switchTo().window(MainWindowHandler);
	}

	@Test(priority = 4, groups = "check time", description = "Total Inter-Regional Road Empties")
	public void InterRegionalRoadEmptiesTrailerReport(Method m) throws ClassNotFoundException, SQLException {
		// navigate to Inter-Regional Road Empties
		page.InterRegionalRoadEmpties.click();
		wait1.until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> WindowHandles = driver.getWindowHandles();
		for (String windowHandle : WindowHandles) {
			if (!windowHandle.equalsIgnoreCase(MainWindowHandler)) {
				driver.switchTo().window(windowHandle);
			}
		}
		wait1.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		wait1.until(ExpectedConditions.visibilityOf(page.InterRegionalRoadEmptiesTrailerInforGrid));
		// get actual time value from screen
		LinkedHashSet<ArrayList<String>> TrailerGRID = page
				.GetTrailerReportTime(page.InterRegionalRoadEmptiesTrailerInforGrid);
		System.out.println("\n Inter-Regional Road Empties totally " + TrailerGRID.size());
		Row subtitle = sheet.createRow(++R);
		subtitle.createCell(0).setCellValue("Inter-Regional Road Empties totally :");
		subtitle.createCell(1).setCellValue(TrailerGRID.size());
		int i = 0;
		int j = 0;
		// 1. get expected result, 2. validate the result, 3. write into the
		// report
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
				Row r = sheet.createRow(++R);
				r.createCell(0).setCellValue(j);
				r.createCell(1).setCellValue(SCAC + "-" + TrailerNB);
				r.createCell(2).setCellValue(CurrentTerminal);
				r.createCell(7).setCellValue(NextTerminal_1);
				System.out.println("\n" + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB
						+ " CurrentTerminal " + CurrentTerminal + " NextTerminal " + NextTerminal_1);
				if (FlagLst == false) {
					r.createCell(2).setCellValue(CurrentTerminal);
					r.createCell(3).setCellValue(ExpectedLstReportT);
					r.createCell(4).setCellValue(ActualLstReportT);
					System.out.print(" CurrentTerminal " + CurrentTerminal + "  " + "Lst Result expected: "
							+ ExpectedLstReportT + " but found: " + ActualLstReportT);
				}
				if (FlagETA == false) {
					r.createCell(8).setCellValue(ExpectedETA);
					r.createCell(9).setCellValue(ActualETA);
					System.out.print(" NextTerminal " + NextTerminal_1 + "  " + "ETA expected: " + ExpectedETA
							+ " but found: " + ActualETA);
				}
				if (FlagSDT == false) {
					r.createCell(10).setCellValue(ExpectedSDT);
					r.createCell(11).setCellValue(ActualSDT);
					System.out.printf(" NextTerminal " + NextTerminal_1 + "  " + "SDT expected: " + ExpectedSDT
							+ " but found: " + ActualSDT);
				}

			}
		}
		// write summary of the report
		System.out.println("\n" + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + "\n");
		Row summary = sheet.createRow(++R);
		summary.createCell(0).setCellValue(m.getName() + " form totally: ");
		summary.createCell(1).setCellValue(TrailerGRID.size());
		summary.createCell(2).setCellValue("  mismatch :");
		summary.createCell(3).setCellValue(i);
		sheet.createRow(++R);
		// get back to
		driver.close();
		driver.switchTo().window(MainWindowHandler);
	}

	@Test(priority = 5, description = " verfify schedule for Inter-Regional Road Empties")
	public void VerifySchedulesForInterRegionalRoadEmpty() throws SQLException, IOException {
		wait1.until(ExpectedConditions.visibilityOf(page.InterRegionalRoadEmpties));
		wait1.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		// verify schedule and write to report
		String ScheduleE = page.InterRegionalRoadEmpties.findElement(By.xpath("following-sibling::td")).getText();
		String ExpectedSheduleE = CommonData.GetScheduleForInterRegionalRoadEmpty();
		Row summary = sheet.createRow(++R);
		if (ScheduleE.equals(ExpectedSheduleE)) {
			System.out.println("The schedule of Inter-Regional Road Empties populate correct");
			summary.createCell(0).setCellValue("The schedule of Inter-Regional Road Empties populate correct");
		} else {
			System.out.println("The schedule of Inter-Regional Road Empties is wrong " + ExpectedSheduleE
					+ " But found : " + ScheduleE);
			summary.createCell(0)
					.setCellValue("The schedule of Inter-Regional Road Empties is wrong, Expected Shedule ");
			summary.createCell(1).setCellValue("Expected Shedule: " + ExpectedSheduleE);
			summary.createCell(2).setCellValue("But found : " + ScheduleE);
		}
		sheet.createRow(++R);
	}

	@AfterMethod(groups = { "check time" })
	public void CheckFailure(ITestResult result) {
		/*
		 * if the test failure during time checking, navigate back to system
		 * summary page
		 */
		if (result.getStatus() == ITestResult.FAILURE) {
			if (!driver.getWindowHandle().equals(MainWindowHandler))
				driver.close();
			driver.switchTo().window(MainWindowHandler);
		}
	}

	@AfterClass
	public void Close() {
		/*
		 * greate report folder by using the current time as folder name, and
		 * deliver report into it
		 */
		String CDate = Function.GetTimeValue(defaultTimeZone.getID());
		File file2 = new File("./Report/" + CDate);
		file2.mkdir();
		File file = new File(file2, this.getClass().getName() + ".xlsx");
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

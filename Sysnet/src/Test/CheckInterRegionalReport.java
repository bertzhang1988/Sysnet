package Test;

import java.awt.AWTException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import Data.DataDAO;
import Utility.ConfigRd;
import Utility.Function;
import page.SysnetPage;
import page.Trailer;

public class CheckInterRegionalReport {
	private WebDriver driver;
	private SysnetPage page;
	private Actions builder;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private int R;
	private DataDAO DA;

	@BeforeClass
	@Parameters({ "browser" })
	public void SetUp(@Optional("chrome") String browser)
			throws AWTException, InterruptedException, IOException, SQLException {
		ConfigRd Conf = new ConfigRd();
		DA = new DataDAO();
		if (browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", Conf.GetChromePath());
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("ie")) {
			System.setProperty("webdriver.ie.driver", Conf.GetIEPath());
			driver = new InternetExplorerDriver();
		}
		page = new SysnetPage(driver);
		driver.get(Conf.GetSysnetSitURL());
		driver.manage().window().maximize();
		builder = new Actions(driver);
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.visibilityOf(page.SystemSummaryButton));
		page.SystemSummaryButton.click();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.visibilityOf(page.InterRegionalform));
		// create excel sheet and title
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet(Function.GetDisplayTime().replace(":", "-"));
		String[] TitleLine = { "Line", "Time is wrong for trailer", " CurrentTerminal", "LstReport time expected:",
				"but found: ", "TTMS expected: ", " but found: " };
		Row r = sheet.createRow(R);
		int ColumnOfFirstline = 0;
		for (String value : TitleLine) {
			Cell CellOfFirstRow = r.createCell(ColumnOfFirstline);
			CellOfFirstRow.setCellValue(value);
			ColumnOfFirstline++;
		}
	}

	@Test(priority = 1)
	public void InterRegionalAtLddTrailerReport(Method m) throws ClassNotFoundException, SQLException {
		String CurrentWindowHandle = driver.getWindowHandle();
		page.InterRegionalATLdd.click();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> WindowHandles = driver.getWindowHandles();
		for (String windowHandle : WindowHandles) {
			if (!windowHandle.equalsIgnoreCase(CurrentWindowHandle)) {
				driver.switchTo().window(windowHandle);
			}
		}
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.visibilityOf(page.InterRegionalATLddTrailerInforGrid));
		LinkedHashSet<ArrayList<String>> TrailerGRID = page
				.GetTrailerReportTime(page.InterRegionalATLddTrailerInforGrid);
		System.out.println("\n Inter-Regional ldd totally " + TrailerGRID.size());

		Row subtitle = sheet.createRow(++R);
		subtitle.createCell(0).setCellValue("Inter-Regional ldd totally :");
		subtitle.createCell(1).setCellValue(TrailerGRID.size());
		int i = 0;
		int j = 0;
		for (ArrayList<String> trailer : TrailerGRID) {

			String SCAC = trailer.get(0);
			String TrailerNB = trailer.get(1);
			Trailer T = DA.GetTrailer(SCAC, TrailerNB);
			String ExpectedTTMS = T.getTTMS();
			String ExpectedLstReportT = T.getLastReportTime();
			String ActualTTMS = trailer.get(3);
			String ActualLstReportT = trailer.get(4);
			String CurrentTerminal = trailer.get(2);
			j = j + 1;
			boolean FlagLst = ExpectedLstReportT.equals(ActualLstReportT);
			boolean FlagTTMS = ExpectedTTMS.equals(ActualTTMS);
			if (!(FlagLst && FlagTTMS)) {
				i = i + 1;
				R = 1 + R;
				Row r = sheet.createRow(R);
				r.createCell(0).setCellValue(j);
				r.createCell(1).setCellValue(SCAC + "-" + TrailerNB);
				r.createCell(2).setCellValue(CurrentTerminal);
				System.out.println("\n" + j + " Time is wrong for trailer " + SCAC + "-" + TrailerNB
						+ " CurrentTerminal " + CurrentTerminal);
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
			}
		}

		System.out.println("\n" + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + "\n");
		Row summary = sheet.createRow(++R);
		summary.createCell(0).setCellValue(m.getName() + " form totally: ");
		summary.createCell(1).setCellValue(TrailerGRID.size());
		summary.createCell(2).setCellValue("  mismatch :");
		summary.createCell(3).setCellValue(i);
		// get back to
		driver.close();
		driver.switchTo().window(CurrentWindowHandle);
	}

	@Test(priority = 2)
	public void InterRegionalAtArrTrailerReport(Method m) throws ClassNotFoundException, SQLException {
		SoftAssert SA = new SoftAssert();
		String CurrentWindowHandle = driver.getWindowHandle();
		page.InterRegionalATArr.click();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> WindowHandles = driver.getWindowHandles();
		for (String windowHandle : WindowHandles) {
			if (!windowHandle.equalsIgnoreCase(CurrentWindowHandle)) {
				driver.switchTo().window(windowHandle);
			}
		}
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.visibilityOf(page.InterRegionalATArrTrailerInforGrid));
		Date d = Function.gettime("UTC");
		LinkedHashSet<ArrayList<String>> TrailerGRID = page
				.GetTrailerReportList(page.InterRegionalATArrTrailerInforGrid);
		Date d2 = Function.gettime("UTC");
		System.out.println((d2.getTime() - d.getTime()) / 1000.0);
		System.out.println("\n Inter-Regional ARR totally " + TrailerGRID.size());
		int i = 0;
		int j = 0;
		DataDAO DA = new DataDAO();
		ArrayList<ArrayList<String>> ExpectedTrailerInforReport = new ArrayList<ArrayList<String>>(
				DA.GetTrailerInforReport(TrailerGRID));

		for (ArrayList<String> trailer : TrailerGRID) {
			String SCAC = trailer.get(0);
			String TrailerNB = trailer.get(1);
			String Expected = ExpectedTrailerInforReport.get(j).get(4);
			String Actual = trailer.get(10);
			j = j + 1;
			if (!Expected.equals(Actual)) {
				i = i + 1;
				System.out.println(
						j + " Lst Rptd Time is wrong for trailer " + SCAC + "-" + TrailerNB + " CurrentTerminal "
								+ trailer.get(5) + "  " + "expected: " + Expected + " but found: " + Actual);
			}
		}

		System.out.println("\n" + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + "\n");

		// get back to
		driver.close();
		driver.switchTo().window(CurrentWindowHandle);
	}

	@Test(priority = 3)
	public void InterRegionalAtLddArrTrailerReport(Method m) throws ClassNotFoundException, SQLException {
		SoftAssert SA = new SoftAssert();
		String CurrentWindowHandle = driver.getWindowHandle();
		page.InterRegionalATLddArr.click();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> WindowHandles = driver.getWindowHandles();
		for (String windowHandle : WindowHandles) {
			if (!windowHandle.equalsIgnoreCase(CurrentWindowHandle)) {
				driver.switchTo().window(windowHandle);
			}
		}
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		(new WebDriverWait(driver, 50))
				.until(ExpectedConditions.visibilityOf(page.InterRegionalATLddArrTrailerInforGrid));
		LinkedHashSet<ArrayList<String>> TrailerGRID = page
				.GetTrailerReportList(page.InterRegionalATLddArrTrailerInforGrid);
		System.out.println("\n Inter-Regional LDD AND ARR totally " + TrailerGRID.size());
		int i = 0;
		int j = 0;
		DataDAO DA = new DataDAO();
		ArrayList<ArrayList<String>> ExpectedTrailerInforReport = new ArrayList<ArrayList<String>>(
				DA.GetTrailerInforReport(TrailerGRID));

		for (ArrayList<String> trailer : TrailerGRID) {
			String SCAC = trailer.get(0);
			String TrailerNB = trailer.get(1);
			String Expected = ExpectedTrailerInforReport.get(j).get(4);
			String Actual = trailer.get(10);
			j = j + 1;
			if (!Expected.equals(Actual)) {
				i = i + 1;
				System.out.println(
						j + " Lst Rptd Time is wrong for trailer " + SCAC + "-" + TrailerNB + " CurrentTerminal "
								+ trailer.get(5) + "  " + "expected: " + Expected + " but found: " + Actual);
			}
		}

		System.out.println("\n" + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + "\n");

		// get back to
		driver.close();
		driver.switchTo().window(CurrentWindowHandle);
	}

	@Test(priority = 4)
	public void InterRegionalRoadEmptiesTrailerReport(Method m) throws ClassNotFoundException, SQLException {
		SoftAssert SA = new SoftAssert();
		String CurrentWindowHandle = driver.getWindowHandle();
		page.InterRegionalRoadEmpties.click();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> WindowHandles = driver.getWindowHandles();
		for (String windowHandle : WindowHandles) {
			if (!windowHandle.equalsIgnoreCase(CurrentWindowHandle)) {
				driver.switchTo().window(windowHandle);
			}
		}
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		(new WebDriverWait(driver, 50))
				.until(ExpectedConditions.visibilityOf(page.InterRegionalRoadEmptiesTrailerInforGrid));
		LinkedHashSet<ArrayList<String>> TrailerGRID = page
				.GetTrailerReportList(page.InterRegionalRoadEmptiesTrailerInforGrid);
		System.out.println("\n Inter-Regional Road Empties totally " + TrailerGRID.size());
		int i = 0;
		int j = 0;
		DataDAO DA = new DataDAO();
		ArrayList<ArrayList<String>> ExpectedTrailerInforReport = new ArrayList<ArrayList<String>>(
				DA.GetTrailerInforReport(TrailerGRID));

		for (ArrayList<String> trailer : TrailerGRID) {
			String SCAC = trailer.get(0);
			String TrailerNB = trailer.get(1);
			String Expected = ExpectedTrailerInforReport.get(j).get(4);
			String Actual = trailer.get(7);
			j = j + 1;
			if (!Expected.equals(Actual)) {
				i = i + 1;
				System.out.println(j + " Lst Rptd Time is wrong for trailer " + SCAC + "-" + TrailerNB
						+ " CurrentTerminal " + ExpectedTrailerInforReport.get(j).get(3) + "  " + "expected: "
						+ Expected + " but found: " + Actual);

			}
		}

		System.out.println("\n" + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i + "\n");

		// get back to
		driver.close();
		driver.switchTo().window(CurrentWindowHandle);
	}

	@AfterClass
	public void Close() {
		driver.close();
	}
}

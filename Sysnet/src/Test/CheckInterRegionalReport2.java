package Test;

import java.awt.AWTException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import Data.DataDAO;
import Utility.ConfigRd;
import Utility.Function;
import Utility.Utility;
import page.SysnetPage;

public class CheckInterRegionalReport2 {
	private WebDriver driver;
	private SysnetPage page;
	private Actions builder;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private int R;

	@BeforeClass
	@Parameters({ "browser" })
	public void SetUp(@Optional("chrome") String browser) throws AWTException, InterruptedException, IOException {
		ConfigRd Conf = new ConfigRd();
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
		sheet = workbook.createSheet(CheckIntraRegionalReport2.class.getName());
		String[] TitleLine = { "Line", "Lst Rptd Time is wrong for trailer", " CurrentTerminal", "expected:",
				"but found:" };
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
		Utility.takescreenshot(driver, m.getName());
		LinkedHashSet<ArrayList<String>> TrailerGRID = page
				.GetTrailerReportList(page.InterRegionalATLddTrailerInforGrid);
		System.out.println("\n Inter-Regional ldd totally " + TrailerGRID.size());

		Row subtitle = sheet.createRow(++R);
		subtitle.createCell(0).setCellValue("Inter-Regional ldd totally :");
		subtitle.createCell(1).setCellValue(TrailerGRID.size());
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
				R = 1 + R;
				Row r = sheet.createRow(R);
				r.createCell(0).setCellValue(j);
				r.createCell(1).setCellValue(SCAC + "-" + TrailerNB);
				r.createCell(2).setCellValue(trailer.get(5));
				r.createCell(3).setCellValue(Expected);
				r.createCell(4).setCellValue(Actual);

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
		Utility.takescreenshot(driver, m.getName());
		LinkedHashSet<ArrayList<String>> TrailerGRID = page
				.GetTrailerReportList(page.InterRegionalATArrTrailerInforGrid);
		System.out.println("\n Inter-Regional ARR totally " + TrailerGRID.size());
		Row subtitle = sheet.createRow(++R);
		subtitle.createCell(0).setCellValue("Inter-Regional ARR totally :");
		subtitle.createCell(1).setCellValue(TrailerGRID.size());
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
				R = 1 + R;
				Row r = sheet.createRow(R);
				r.createCell(0).setCellValue(j);
				r.createCell(1).setCellValue(SCAC + "-" + TrailerNB);
				r.createCell(2).setCellValue(trailer.get(5));
				r.createCell(3).setCellValue(Expected);
				r.createCell(4).setCellValue(Actual);

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

	@Test(priority = 3)
	public void InterRegionalAtLddArrTrailerReport(Method m) throws ClassNotFoundException, SQLException {

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
		Utility.takescreenshot(driver, m.getName());
		LinkedHashSet<ArrayList<String>> TrailerGRID = page
				.GetTrailerReportList(page.InterRegionalATLddArrTrailerInforGrid);
		System.out.println("\n Inter-Regional LDD AND ARR totally " + TrailerGRID.size());
		Row subtitle = sheet.createRow(++R);
		subtitle.createCell(0).setCellValue("Inter-Regional LDD AND ARR totally :");
		subtitle.createCell(1).setCellValue(TrailerGRID.size());
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
				R = 1 + R;
				Row r = sheet.createRow(R);
				r.createCell(0).setCellValue(j);
				r.createCell(1).setCellValue(SCAC + "-" + TrailerNB);
				r.createCell(2).setCellValue(trailer.get(5));
				r.createCell(3).setCellValue(Expected);
				r.createCell(4).setCellValue(Actual);

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

	@Test(priority = 4)
	public void InterRegionalRoadEmptiesTrailerReport(Method m) throws ClassNotFoundException, SQLException {

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
		Utility.takescreenshot(driver, m.getName());
		LinkedHashSet<ArrayList<String>> TrailerGRID = page
				.GetTrailerReportList(page.InterRegionalRoadEmptiesTrailerInforGrid);
		System.out.println("\n Inter-Regional Road Empties totally " + TrailerGRID.size());
		Row subtitle = sheet.createRow(++R);
		subtitle.createCell(0).setCellValue("Inter-Regional Road Empties totally :");
		subtitle.createCell(1).setCellValue(TrailerGRID.size());
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
				R = 1 + R;
				Row r = sheet.createRow(R);
				r.createCell(0).setCellValue(j);
				r.createCell(1).setCellValue(SCAC + "-" + TrailerNB);
				r.createCell(2).setCellValue(trailer.get(5));
				r.createCell(3).setCellValue(Expected);
				r.createCell(4).setCellValue(Actual);

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

	@AfterClass
	public void Close() {
		driver.close();
		String CDate = new SimpleDateFormat("YYYY-MM-dd--HH-mm-ss").format(Function.gettime("America/Chicago"));
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

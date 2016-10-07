package Test;

import java.awt.AWTException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import Data.CommonData;
import Data.DataDAO;
import Utility.ConfigRd;
import page.SysnetPage;
import page.Trailer;

public class T1 {

	private WebDriver driver;
	private SysnetPage page;
	private Actions builder;

	// @BeforeClass
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
	}

	// @Test(priority = 2)
	public void GetTrailerInformation() {
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.visibilityOf(page.SystemSummaryButton));
		page.SystemSummaryButton.click();
		String CurrentWindowHandle = driver.getWindowHandle();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.visibilityOf(page.InterRegionalform));
		page.InterRegionalform.findElement(By.xpath("//span[contains(text(), 'Average Time on LDD Status in Hours')]"))
				.click();
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
				.GetTrailerReportList(page.InterRegionalATLddTrailerInforGrid);
		System.out.println(TrailerGRID.size());
		for (ArrayList<String> line : TrailerGRID) {
			System.out.print("\n");
			for (String information : line) {
				System.out.print(information + "  ");
			}
		}
	}

	// @Test(priority = 1)
	public void example() throws ClassNotFoundException, SQLException {
		SoftAssert SA = new SoftAssert();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.visibilityOf(page.SystemSummaryButton));
		page.SystemSummaryButton.click();
		String CurrentWindowHandle = driver.getWindowHandle();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.visibilityOf(page.InterRegionalform));
		page.InterRegionalform.findElement(By.xpath("//span[contains(text(), 'Average Time on LDD Status in Hours')]"))
				.click();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> WindowHandles = driver.getWindowHandles();
		for (String windowHandle : WindowHandles) {
			if (!windowHandle.equalsIgnoreCase(CurrentWindowHandle)) {
				driver.switchTo().window(windowHandle);
			}
		}
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.visibilityOf(page.InterRegionalATLddTrailerInforGrid));

		for (int i = 1; i <= 5; i++) {
			String[] Proline1 = page.InterRegionalATLddTrailerInforGrid.findElement(By.xpath("div[" + i + "]"))
					.getText().split("\\n");
			ArrayList<String> e1 = new ArrayList<String>(Arrays.asList(Proline1));
			String SCAC = e1.get(0);
			String TrailerNB = e1.get(1);
			HashMap<String, Object> MRSEQPST = CommonData.CheckEQPStatusUpdate(SCAC, TrailerNB);
			SA.assertEquals(e1.get(9), MRSEQPST.get("Lst Rptd Time"),
					" Lst Rptd Time is wrong for trailer " + SCAC + "-" + TrailerNB + "  ");
		}
		SA.assertAll();
	}

	// @Test(priority = 3)
	public void example2() throws ClassNotFoundException, SQLException {
		SoftAssert SA = new SoftAssert();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.visibilityOf(page.SystemSummaryButton));
		page.SystemSummaryButton.click();
		String CurrentWindowHandle = driver.getWindowHandle();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.visibilityOf(page.InterRegionalform));
		// page.InterRegionalform.findElement(By.xpath("//span[contains(text(),'Average
		// Time on LDD Status in Hours')]")).click();
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
				.GetTrailerReportList(page.InterRegionalATLddTrailerInforGrid);
		for (ArrayList<String> trailer : TrailerGRID) {

			String SCAC = trailer.get(0);
			String TrailerNB = trailer.get(1);
			HashMap<String, Object> MRSEQPST = CommonData.CheckEQPStatusUpdate(SCAC, TrailerNB);
			SA.assertEquals(trailer.get(9), MRSEQPST.get("Lst Rptd Time"),
					" Lst Rptd Time is wrong for trailer " + SCAC + "-" + TrailerNB + "  ");
		}
		System.out.println(TrailerGRID.size());
		SA.assertAll();
	}

	// @Test(priority = 4)
	public void example3() throws SQLException, ClassNotFoundException {
		SoftAssert SA = new SoftAssert();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.visibilityOf(page.SystemSummaryButton));
		page.SystemSummaryButton.click();
		String CurrentWindowHandle = driver.getWindowHandle();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.visibilityOf(page.InterRegionalform));
		// page.InterRegionalform.findElement(By.xpath("//span[contains(text(),'Average
		// Time on LDD Status in Hours')]")).click();
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
				.GetTrailerReportList(page.InterRegionalATLddTrailerInforGrid);
		System.out.println("totally " + TrailerGRID.size());

		int i = 0;
		int j = 0;
		for (ArrayList<String> trailer : TrailerGRID) {
			j = j + 1;
			String SCAC = trailer.get(0);
			String TrailerNB = trailer.get(1);
			HashMap<String, Object> MRSEQPST = CommonData.CheckEQPStatusUpdate(SCAC, TrailerNB);
			String Expected = (String) MRSEQPST.get("Lst Rptd Time");
			String Actual = trailer.get(9);
			if (!Expected.equals(Actual)) {
				i = i + 1;
				System.out.println(j + " Lst Rptd Time is wrong for trailer " + SCAC + "-" + TrailerNB + "  "
						+ "expected: " + Expected + " but found: " + Actual);
			}
		}

		System.out.println("totally " + TrailerGRID.size() + "  mismatch " + i);
	}

	// @Test(priority = 5)
	public void example4() throws SQLException, ClassNotFoundException {
		SoftAssert SA = new SoftAssert();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.visibilityOf(page.SystemSummaryButton));
		page.SystemSummaryButton.click();
		String CurrentWindowHandle = driver.getWindowHandle();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.visibilityOf(page.InterRegionalform));
		// page.InterRegionalform.findElement(By.xpath("//span[contains(text(),'Average
		// Time on LDD Status in Hours')]")).click();
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
				.GetTrailerReportList(page.InterRegionalATLddTrailerInforGrid);
		System.out.println("totally " + TrailerGRID.size());

		int i = 0;
		int j = 0;
		for (ArrayList<String> trailer : TrailerGRID) {
			j = j + 1;
			String SCAC = trailer.get(0);
			String TrailerNB = trailer.get(1);

			DataDAO DA = new DataDAO();
			Trailer currentTrailer = DA.GetTrailer(SCAC, TrailerNB);
			String Expected = currentTrailer.getLastReportTime();
			String Actual = trailer.get(9);
			if (!Expected.equals(Actual)) {
				i = i + 1;
				System.out.println(j + " Lst Rptd Time is wrong for trailer " + SCAC + "-" + TrailerNB + "  "
						+ "expected: " + Expected + " but found: " + Actual);
			}
		}

		System.out.println("totally " + TrailerGRID.size() + "  mismatch " + i);
	}

	@Test(priority = 6)
	public void example5(Method m) throws SQLException, ClassNotFoundException {
		SoftAssert SA = new SoftAssert();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.visibilityOf(page.SystemSummaryButton));
		page.SystemSummaryButton.click();
		String CurrentWindowHandle = driver.getWindowHandle();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.visibilityOf(page.InterRegionalform));
		// page.InterRegionalform.findElement(By.xpath("//span[contains(text(),'Average
		// Time on LDD Status in Hours')]")).click();
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
				.GetTrailerReportList(page.InterRegionalATLddTrailerInforGrid);
		System.out.println("totally " + TrailerGRID.size());
		int i = 0;
		int j = 0;
		DataDAO DA = new DataDAO();
		ArrayList<ArrayList<String>> ExpectedTrailerInforReport = new ArrayList<ArrayList<String>>(
				DA.GetTrailerInforReport(TrailerGRID));

		for (ArrayList<String> trailer : TrailerGRID) {
			String SCAC = trailer.get(0);
			String TrailerNB = trailer.get(1);
			String Expected = ExpectedTrailerInforReport.get(j).get(4);
			String Actual = trailer.get(9);
			j = j + 1;
			if (!Expected.equals(Actual)) {
				i = i + 1;
				System.out.println(j + " Lst Rptd Time is wrong for trailer " + SCAC + "-" + TrailerNB + "  "
						+ "expected: " + Expected + " but found: " + Actual);
			}
		}

		System.out.println("\n" + m.getName() + " form totally " + TrailerGRID.size() + "  mismatch " + i);
	}

}

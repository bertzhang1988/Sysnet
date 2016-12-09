package page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class SysnetPage {

	private WebDriver driver;

	public SysnetPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	/* Common */

	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "System\nSummary")
	public WebElement SystemSummaryButton;

	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Route Plan")
	public WebElement RoutePlanButton;
	
	@FindBy(how = How.PARTIAL_LINK_TEXT, using = " Linehaul\nResources")
	public WebElement LinehaulResourcesButton;
	
	/* SystemSummary */

	// inter-regional form

	@FindBy(how = How.ID, using = "inter_regional")
	public WebElement InterRegionalform;

	@FindBy(how = How.XPATH, using = ".//div[@id='inter_regional']//span[contains(text(), 'Average Time for LDD + ARR in Hours')]")
	public WebElement InterRegionalATLddArr;

	@FindBy(how = How.CSS, using = "div#yrcinterLDDARV>div:nth-of-type(2)>div>div:nth-of-type(3) div.ui-grid-canvas")
	public WebElement InterRegionalATLddArrTrailerInforGrid;

	@FindBy(how = How.XPATH, using = ".//div[@id='inter_regional']//span[text()='Average Time on LDD Status in Hours']")
	public WebElement InterRegionalATLdd;

	@FindBy(how = How.CSS, using = "div#yrcinterLDD>div:nth-of-type(2)>div>div:nth-of-type(3) div.ui-grid-canvas")
	public WebElement InterRegionalATLddTrailerInforGrid;

	@FindBy(how = How.XPATH, using = ".//div[@id='inter_regional']//span[contains(text(), 'Average Time on ARR Status in Hours')]")
	public WebElement InterRegionalATArr;

	@FindBy(how = How.CSS, using = "div#yrcinterARV>div:nth-of-type(2)>div>div:nth-of-type(3) div.ui-grid-canvas")
	public WebElement InterRegionalATArrTrailerInforGrid;

	@FindBy(how = How.XPATH, using = ".//div[@id='inter_regional']//td[contains(text(), 'Total Inter-Regional Road Empties')]")
	public WebElement InterRegionalRoadEmpties;

	@FindBy(how = How.CSS, using = "div#yrcinter>div:nth-of-type(2)>div>div:nth-of-type(3) div.ui-grid-canvas")
	public WebElement InterRegionalRoadEmptiesTrailerInforGrid;

	// intra-regional form

	@FindBy(how = How.ID, using = "intra_regional")
	public WebElement IntraRegionalform;

	@FindBy(how = How.XPATH, using = ".//div[@id='intra_regional']//span[contains(text(), 'Average Time for LDD + ARR in Hours')]")
	public WebElement IntraRegionalATLddArr;

	@FindBy(how = How.CSS, using = "div#yrcintraLDDARV>div:nth-of-type(2)>div>div:nth-of-type(3) div.ui-grid-canvas")
	public WebElement IntraRegionalformATLddArvTrailerInforGrid;

	@FindBy(how = How.XPATH, using = ".//div[@id='intra_regional']//span[text()='Average Time on LDD Status in Hours']")
	public WebElement IntraRegionalATLdd;

	@FindBy(how = How.CSS, using = "div#yrcintraLDD>div:nth-of-type(2)>div>div:nth-of-type(3) div.ui-grid-canvas")
	public WebElement IntraRegionalformATLddTrailerInforGrid;

	@FindBy(how = How.XPATH, using = ".//div[@id='intra_regional']//span[contains(text(), 'Average Time on ARR Status in Hours')]")
	public WebElement IntraRegionalATArr;

	@FindBy(how = How.CSS, using = "div#yrcintraARV>div:nth-of-type(2)>div>div:nth-of-type(3) div.ui-grid-canvas")
	public WebElement IntraRegionalformATArrTrailerInforGrid;

	@FindBy(how = How.XPATH, using = ".//div[@id='intra_regional']//td[contains(text(), 'Total Intra-Regional LDD + ARR DC-Sat Loads')]")
	public WebElement IntraRegionalATLddArrDC_SAT;

	@FindBy(how = How.CSS, using = "div#yrcintraDC_SAT>div:nth-of-type(2)>div>div:nth-of-type(3) div.ui-grid-canvas")
	public WebElement IntraRegionalformATLddArrDC_SATInforGrid;

	@FindBy(how = How.XPATH, using = ".//div[@id='intra_regional']//td[contains(text(), 'Total Intra-Regional LDD + ARR Sat-DC Loads')]")
	public WebElement IntraRegionalATLddArrSAT_DC;

	@FindBy(how = How.CSS, using = "div#yrcintraSAT_DC>div:nth-of-type(2)>div>div:nth-of-type(3) div.ui-grid-canvas")
	public WebElement IntraRegionalformATLddArrSAT_DCInforGrid;

	@FindBy(how = How.XPATH, using = ".//div[@id='intra_regional']//td[contains(text(), 'Total Intra-Regional Road Empties')]")
	public WebElement IntraRegionalRoadEmpties;

	@FindBy(how = How.CSS, using = "div#yrcintra>div:nth-of-type(2)>div>div:nth-of-type(3) div.ui-grid-canvas")
	public WebElement IntraRegionalRoadEmptiesTrailerInforGrid;

	// rail form
	@FindBy(how = How.ID, using = "rail")
	public WebElement Railform;

	@FindBy(how = How.XPATH, using = ".//div[@id='rail']//td[contains(text(), 'Total Empty Rail')]")
	public WebElement TotalEmptyRail;

	@FindBy(how = How.CSS, using = "div#yrcemptyRail>div:nth-of-type(2)>div>div:nth-of-type(3) div.ui-grid-canvas")
	public WebElement TotalEmptyRailForm;

	@FindBy(how = How.XPATH, using = ".//div[@id='rail']//td[contains(text(), 'Total Loaded Rail')]")
	public WebElement TotalLoadedRail;

	@FindBy(how = How.CSS, using = "div#yrcloadedRail>div:nth-of-type(2)>div>div:nth-of-type(3) div.ui-grid-canvas")
	public WebElement TotalLoadedRailForm;

	/* Route Plan*/
	
	@FindBy(how=How.XPATH,using=".//label[text()=' Inter']/input")
	public WebElement InterRadioButton;
	
	@FindBy(how=How.XPATH,using=".//label[text()=' Intra']/input")
	public WebElement IntraRadioButton;
	
	@FindBy(how=How.XPATH,using=".//label[text()=' Both']/input")
	public WebElement BothRadioButton;
	
	@FindBy(how=How.XPATH, using=".//div[@config='configHolistic']//div[@class='ui-grid-contents-wrapper']/div[3]/div[@role='rowgroup' and @class='ui-grid-viewport']//div[@class='ui-grid-canvas']")
	public WebElement RoutePlanForm;
	
	/*Linehaul Resources*/
	
	@FindBy(how=How.XPATH, using=".//div[@id='yrclinehaulResource']//div[@class='ui-grid-contents-wrapper']/div[2]/div[@role='rowgroup' and @class='ui-grid-viewport']//div[@class='ui-grid-canvas']")
	public WebElement LinehaulForm;
	
	@FindBy(how=How.ID, using="alllocations_LH")
	public WebElement LhAllLocTer;
	
	@FindBy(how=How.ID, using="usa_Country")
	public WebElement LhUsaCy;
	
	/* UI method */
	public LinkedHashSet<ArrayList<String>> GetReportList(WebElement TrailerInforGrid) {
		driver.manage().window().maximize();
		int line = TrailerInforGrid.findElements(By.xpath("div")).size();
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		LinkedHashSet<ArrayList<String>> ProInfo = new LinkedHashSet<ArrayList<String>>();
		int lastLine;
		do {
			for (int j = 1; j <= line; j++) {
				// String[]
				// Proline1=ArrayUtils.remove(TrailerInforGrid.findElement(By.xpath("div["
				// + j + "]")).getText().split("\\n"),0);
				String[] Proline1 = TrailerInforGrid.findElement(By.xpath("div[" + j + "]")).getText().split("\\n");
				ArrayList<String> e1 = new ArrayList<String>(Arrays.asList(Proline1));
				if (!Proline1[2].equals("+"))
					e1.add(2, "");
				// if ((TrailerInforGrid !=
				// this.InterRegionalRoadEmptiesTrailerInforGrid)
				// && (TrailerInforGrid !=
				// this.IntraRegionalRoadEmptiesTrailerInforGrid)) {
				// if (e1.get(10).length() < 7)
				// e1.add(9, "");
				// }
				ProInfo.add(e1);
			}

			jse.executeScript("arguments[0].scrollIntoView(true);",
					TrailerInforGrid.findElement(By.xpath("div[" + line + "]")));
			// System.out.println((Long) jse.executeScript("return
			// window.scrollY"));
			lastLine = line;
			line = TrailerInforGrid.findElements(By.xpath("div")).size();
		} while (line >= lastLine && line > 48);

		for (int j = 1; j <= line; j++) {
			// String[]
			// Proline1=ArrayUtils.remove(TrailerInforGrid.findElement(By.xpath("div["
			// + j + "]")).getText().split("\\n"),0);
			String[] Proline1 = TrailerInforGrid.findElement(By.xpath("div[" + j + "]")).getText().split("\\n");
			ArrayList<String> e1 = new ArrayList<String>(Arrays.asList(Proline1));
			if (!Proline1[2].equals("+"))
				e1.add(2, "");
			// if ((TrailerInforGrid !=
			// this.InterRegionalRoadEmptiesTrailerInforGrid)
			// && (TrailerInforGrid !=
			// this.IntraRegionalRoadEmptiesTrailerInforGrid)) {
			// if (e1.get(10).length() < 7)
			// e1.add(9, "");
			// }
			ProInfo.add(e1);
		}
		return ProInfo;
	}

	public LinkedHashSet<ArrayList<String>> GetReport(WebElement TrailerInforGrid) {
		driver.manage().window().maximize();
		int line = TrailerInforGrid.findElements(By.xpath("div")).size();
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		LinkedHashSet<ArrayList<String>> ProInfo = new LinkedHashSet<ArrayList<String>>();
		int lastLine;
		do {
			for (int j = 1; j <= line; j++) {
				
				String[] Proline1 = TrailerInforGrid.findElement(By.xpath("div[" + j + "]")).getAttribute("innerText").split("\\n");
				ArrayList<String> e1 = new ArrayList<String>(Arrays.asList(Proline1));
				ProInfo.add(e1);
			}

			jse.executeScript("arguments[0].scrollIntoView(true);",
					TrailerInforGrid.findElement(By.xpath("div[" + line + "]")));
			// System.out.println((Long) jse.executeScript("return
			// window.scrollY"));
			lastLine = line;
			line = TrailerInforGrid.findElements(By.xpath("div")).size();
		} while (line >= lastLine && line > 48);

		for (int j = 1; j <= line; j++) {
			// String[]
			// Proline1=ArrayUtils.remove(TrailerInforGrid.findElement(By.xpath("div["
			// + j + "]")).getText().split("\\n"),0);
			String[] Proline1 = TrailerInforGrid.findElement(By.xpath("div[" + j + "]")).getAttribute("innerText").split("\\n");
			ArrayList<String> e1 = new ArrayList<String>(Arrays.asList(Proline1));
			ProInfo.add(e1);
		}
		return ProInfo;
	}
	
	public LinkedHashSet<ArrayList<String>> GetTrailerReportTime(WebElement TrailerInforGrid) {
		driver.manage().window().maximize();
		int line = TrailerInforGrid.findElements(By.xpath("div")).size();
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		LinkedHashSet<ArrayList<String>> ProInfo = new LinkedHashSet<ArrayList<String>>();
		int lastLine;
		String SCAC;
		String Trailer;
		String CurrentTerminal;
		String TTMS;
		String LstRptdTime;
		String ETA;
		int IndexOfTTMS;
		int IndexOfLstRptdTime;
		int IndexOfETA;
		// get index of each time column
		List<WebElement> AllHeaders = TrailerInforGrid
				.findElement(By
						.xpath("parent::div/preceding-sibling::div//div[@role='row'][@class='ui-grid-header-cell-row']"))
				.findElements(By.xpath("div"));
		try {
			IndexOfLstRptdTime = AllHeaders.indexOf(
					driver.findElement(By.xpath(".//div[@title='Last Reported Time Date']/ancestor::div[2]"))) + 1;
		} catch (NoSuchElementException ns) {
			IndexOfLstRptdTime = 0;
		}

		try {
			IndexOfTTMS = AllHeaders.indexOf(
					driver.findElement(By.xpath(".//div[@title='Time to Make Service']/ancestor::div[2]"))) + 1;
		} catch (NoSuchElementException ns) {
			IndexOfTTMS = 0;
		}

		try {
			IndexOfETA = AllHeaders
					.indexOf(driver.findElement(By.xpath(".//div[@title='In Out Time Date']/ancestor::div[2]"))) + 1;
		} catch (NoSuchElementException ns) {
			IndexOfETA = 0;
		}
		do {
			for (int j = 1; j <= line; j++) {
				SCAC = TrailerInforGrid.findElement(By.xpath("div[" + j + "]/div/div[1]")).getText();
				Trailer = TrailerInforGrid.findElement(By.xpath("div[" + j + "]/div/div[2]")).getText();
				CurrentTerminal = TrailerInforGrid.findElement(By.xpath("div[" + j + "]/div/div[6]")).getText();
				try {
					TTMS = TrailerInforGrid.findElement(By.xpath("div[" + j + "]/div/div[" + IndexOfTTMS + "]"))
							.getText();
				} catch (NoSuchElementException ns) {
					TTMS = null;
				}
				try {
					LstRptdTime = TrailerInforGrid
							.findElement(By.xpath("div[" + j + "]/div/div[" + IndexOfLstRptdTime + "]")).getText();
				} catch (NoSuchElementException ns) {
					LstRptdTime = null;
				}
				try {
					ETA = TrailerInforGrid.findElement(By.xpath("div[" + j + "]/div/div[" + IndexOfETA + "]"))
							.getText();
				} catch (NoSuchElementException ns) {
					ETA = null;
				}
				ArrayList<String> e1 = new ArrayList<String>();
				e1.add(SCAC);// 0
				e1.add(Trailer);// 1
				e1.add(CurrentTerminal);// 2
				e1.add(TTMS);// 3
				e1.add(LstRptdTime);// 4
				e1.add(ETA);// 5
				ProInfo.add(e1);
			}

			jse.executeScript("arguments[0].scrollIntoView(true);",
					TrailerInforGrid.findElement(By.xpath("div[" + line + "]")));
			lastLine = line;
			line = TrailerInforGrid.findElements(By.xpath("div")).size();
		} while (line >= lastLine && line > 48);

		for (int j = 1; j <= line; j++) {
			SCAC = TrailerInforGrid.findElement(By.xpath("div[" + j + "]/div/div[1]")).getText();
			Trailer = TrailerInforGrid.findElement(By.xpath("div[" + j + "]/div/div[2]")).getText();
			CurrentTerminal = TrailerInforGrid.findElement(By.xpath("div[" + j + "]/div/div[6]")).getText();
			try {
				TTMS = TrailerInforGrid.findElement(By.xpath("div[" + j + "]/div/div[" + IndexOfTTMS + "]")).getText();
			} catch (NoSuchElementException ns) {
				TTMS = null;
			}
			try {
				LstRptdTime = TrailerInforGrid
						.findElement(By.xpath("div[" + j + "]/div/div[" + IndexOfLstRptdTime + "]")).getText();
			} catch (NoSuchElementException ns) {
				LstRptdTime = null;
			}
			try {
				ETA = TrailerInforGrid.findElement(By.xpath("div[" + j + "]/div/div[" + IndexOfETA + "]")).getText();
			} catch (NoSuchElementException ns) {
				ETA = null;
			}
			ArrayList<String> e1 = new ArrayList<String>();
			e1.add(SCAC);// 0
			e1.add(Trailer);// 1
			e1.add(CurrentTerminal);// 2
			e1.add(TTMS);// 3
			e1.add(LstRptdTime);// 4
			e1.add(ETA);// 5
			ProInfo.add(e1);
		}
		return ProInfo;
	}

}

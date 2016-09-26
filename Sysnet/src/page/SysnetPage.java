package page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

	/* SystemSummary */

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

	public LinkedHashSet<ArrayList<String>> GetTrailerReportList(WebElement TrailerInforGrid) {
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
				ProInfo.add(e1);
			}

			jse.executeScript("arguments[0].scrollIntoView(true);",
					TrailerInforGrid.findElement(By.xpath("div[" + line + "]")));
			lastLine = line;
			line = TrailerInforGrid.findElements(By.xpath("div")).size();
		} while (line >= lastLine && lastLine > 39);

		for (int j = 1; j <= line; j++) {
			// String[]
			// Proline1=ArrayUtils.remove(TrailerInforGrid.findElement(By.xpath("div["
			// + j + "]")).getText().split("\\n"),0);
			String[] Proline1 = TrailerInforGrid.findElement(By.xpath("div[" + j + "]")).getText().split("\\n");
			ArrayList<String> e1 = new ArrayList<String>(Arrays.asList(Proline1));
			if (!Proline1[2].equals("+"))
				e1.add(2, "");
			ProInfo.add(e1);
		}
		return ProInfo;
	}

	public LinkedHashSet<ArrayList<String>> GetTrailerReportLasReportTime(WebElement TrailerInforGrid) {
		driver.manage().window().maximize();
		int line = TrailerInforGrid.findElements(By.xpath("div")).size();
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		LinkedHashSet<ArrayList<String>> ProInfo = new LinkedHashSet<ArrayList<String>>();
		int lastLine;
		String SCAC;
		String Trailer;
		String CurrentTerminal;
		String LstRptdTime;
		do {
			for (int j = 1; j <= line; j++) {
				SCAC = TrailerInforGrid.findElement(By.xpath("div[" + j + "]/div/div[1]")).getText();
				Trailer = TrailerInforGrid.findElement(By.xpath("div[" + j + "]/div/div[2]")).getText();
				CurrentTerminal = TrailerInforGrid.findElement(By.xpath("div[" + j + "]/div/div[6]")).getText();
				LstRptdTime = TrailerInforGrid.findElement(By.xpath("div[" + j + "]/div/div[11]")).getText();
				ArrayList<String> e1 = new ArrayList<String>();
				e1.add(SCAC);
				e1.add(Trailer);
				e1.add(CurrentTerminal);
				e1.add(LstRptdTime);
				ProInfo.add(e1);
			}

			jse.executeScript("arguments[0].scrollIntoView(true);",
					TrailerInforGrid.findElement(By.xpath("div[" + line + "]")));
			lastLine = line;
			line = TrailerInforGrid.findElements(By.xpath("div")).size();
		} while (line >= lastLine);

		for (int j = 1; j <= line; j++) {
			SCAC = TrailerInforGrid.findElement(By.xpath("div[" + j + "]/div/div[1]")).getText();
			Trailer = TrailerInforGrid.findElement(By.xpath("div[" + j + "]/div/div[2]")).getText();
			CurrentTerminal = TrailerInforGrid.findElement(By.xpath("div[" + j + "]/div/div[6]")).getText();
			LstRptdTime = TrailerInforGrid.findElement(By.xpath("div[" + j + "]/div/div[11]")).getText();
			ArrayList<String> e1 = new ArrayList<String>();
			e1.add(SCAC);
			e1.add(Trailer);
			e1.add(CurrentTerminal);
			e1.add(LstRptdTime);
			ProInfo.add(e1);
		}
		return ProInfo;
	}

}

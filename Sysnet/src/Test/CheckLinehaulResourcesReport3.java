package Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import Data.CommonData;
import Utility.Function;
import Utility.SetUpBase.SetupBrowserAndTextReport;
import page.SysnetPage;

/*validate line haul resource page*/
public class CheckLinehaulResourcesReport3 extends SetupBrowserAndTextReport {

	private SysnetPage page;
	private WebDriverWait wait1;

	@BeforeClass
	public void Setup() throws IOException, SQLException {
		page = new SysnetPage(driver);
		if (!page.isVisable(page.LinehaulResourcesButton))
			page.Square.click();
		wait1 = new WebDriverWait(driver, 50);
		wait1.until(ExpectedConditions.visibilityOf(page.LinehaulResourcesButton));
		// click linehual resource button and navigate LR page
		page.LinehaulResourcesButton.click();
		wait1.until(ExpectedConditions.visibilityOf(page.LinehaulForm));
		wait1.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		if (page.isVisable(page.LinehaulResourcesButton) && page.isVisable(page.Square))
			page.Square.click();
		fw.write(Function.GetDisplayTime().replace(":", "-") + Nl);
	}

	@Test(description = "validate value of linehaul resource page for ALL locations  ")
	public void CheckLineHaulResourceAllLocation() throws SQLException, IOException {
		// click ALL location in for terminal
		page.LhAllLocTer.click();
		wait1.until(ExpectedConditions.visibilityOf(page.LhUsaCy));
		// get expected result
		LinkedHashSet<ArrayList<String>> ExpectedLineHualResourceList = CommonData.GetLinehaulForm();
		// get value from screen
		LinkedHashSet<ArrayList<String>> ActualLineHualResourceList = page.GetReport(page.LinehaulForm);
		// do validation and write report
		if (!ActualLineHualResourceList.equals(ExpectedLineHualResourceList)) {
			System.out.println("Linehaul Resource is wrong");
			System.out.println("expected : " + ExpectedLineHualResourceList);
			System.out.println("actual :   " + ActualLineHualResourceList);
			fw.write(Nl + "Linehaul Resource is wrong" + Nl);
			fw.write(Nl+"actual :   " + ActualLineHualResourceList+Nl);
			fw.write(Nl+"expected : " +  ExpectedLineHualResourceList+Nl);
			fw.write(Nl + "expected result but not show:" + Nl
					+ Function.GetDiffBetwForm(ExpectedLineHualResourceList, ActualLineHualResourceList) + Nl);
			fw.write(Nl + "actual result show but not expected:" + Nl
					+ Function.GetDiffBetwForm(ActualLineHualResourceList, ExpectedLineHualResourceList) + Nl);
		} else {
			System.out.println("Linehaul Resource is correct");
			fw.write(Nl + "Linehaul Resource is correct");
		}
	}

}

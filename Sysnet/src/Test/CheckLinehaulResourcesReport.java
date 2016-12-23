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

import Data.CommonData;
import Utility.Function;
import Utility.SetupBrowserAndReport;
import page.SysnetPage;

public class CheckLinehaulResourcesReport extends SetupBrowserAndReport {

	private SysnetPage page;

	@BeforeClass
	public void Setup() throws IOException, SQLException {
		page = new SysnetPage(driver);
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.visibilityOf(page.LinehaulResourcesButton));
		page.LinehaulResourcesButton.click();
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.visibilityOf(page.LinehaulForm));
		(new WebDriverWait(driver, 50)).until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
		 fw.write(Function.GetDisplayTime().replace(":", "-")+Nl);
	}

	@Test
	public void CheckLineHaulResourceAllLocation() throws SQLException, IOException {
	page.LhAllLocTer.click();
	(new WebDriverWait(driver, 50)).until(ExpectedConditions.visibilityOf(page.LhUsaCy));
	LinkedHashSet<ArrayList<String>>  ExpectedLineHualResourceList=CommonData.GetLinehaulForm();
	LinkedHashSet<ArrayList<String>>  ActualLineHualResourceList=page.GetReport(page.LinehaulForm);
	if (!ActualLineHualResourceList.equals(ExpectedLineHualResourceList)) {
		System.out.println("Linehaul Resource is wrong");
		System.out.println("expected : " + ExpectedLineHualResourceList);
		System.out.println("actual :   " + ActualLineHualResourceList);
		fw.write(Nl+"Linehaul Resource is wrong"+Nl);
		fw.write(Nl+"expected result but not show:"+Nl+Function.GetDiffBetwForm(ExpectedLineHualResourceList, ActualLineHualResourceList)+Nl);	
		fw.write(Nl+"actual result show but not expected:"+Nl+Function.GetDiffBetwForm(ActualLineHualResourceList,ExpectedLineHualResourceList)+Nl);	
		
	} else {
		System.out.println("Linehaul Resource is correct");
		fw.write(Nl+"Linehaul Resource is correct");
	}
	}

}

package Test;

import org.testng.annotations.Test;
import org.testng.internal.thread.TestNGThread;

import com.gargoylesoftware.htmlunit.Page;

import Data.CommonData;
import Utility.Function;
import Utility.SetUpBase.SetupBase;
import Utility.SetUpBase.SetupBrowserAndReport;
import page.SysnetPage;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;

public class CheckRoutePlanReport extends SetupBrowserAndReport {

	private SysnetPage page;
	private JavascriptExecutor js;
	private WebDriverWait w1;
	private WebDriverWait w2;
	
	@Test(priority = 1)
	public void CheckInterRoutePlan() throws SQLException, IOException {
		js.executeScript("arguments[0].scrollIntoView(false);", page.InterRadioButton);
		if(page.FilterView.isDisplayed()) 
			page.FilterView.findElement(By.cssSelector("i.fa.fa-chevron-down.pull-right.toggle-filter-view")).click();
		if (!page.InterRadioButton.isSelected())
			page.InterRadioButton.click();
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		LinkedHashSet<ArrayList<String>> ActualRoutePlanInfo = page.GetReport(page.RoutePlanForm);
		long d=Calendar.getInstance().getTime().getTime();
		
		LinkedHashSet<ArrayList<String>> ExpectedRoutePlanInfo = CommonData.GetRoutePlanForm("inter");
		
		long e=Calendar.getInstance().getTime().getTime();
		System.out.println((e-d)/1000);
		if (!ActualRoutePlanInfo.equals(ExpectedRoutePlanInfo)) {
			System.out.println("Inter Route Plan is wrong");
			System.out.println("expected : " + ExpectedRoutePlanInfo);
			System.out.println("actual :   " + ActualRoutePlanInfo);
			fw.write("Inter Route Plan is wrong:"+Nl);
			fw.write(Nl+"expected result but not show:"+Nl+Function.GetDiffBetwForm(ExpectedRoutePlanInfo, ActualRoutePlanInfo)+Nl);	
			fw.write(Nl+"actual result show but not expected:"+Nl+Function.GetDiffBetwForm(ActualRoutePlanInfo,ExpectedRoutePlanInfo)+Nl);
		} else {
			System.out.println("Inter Route Plan is correct");
			fw.write(Nl+"Inter Route Plan is correct");
		}

	}
	
	@Test(priority = 2)
	public void CheckIntraRoutePlan() throws SQLException, IOException {
		js.executeScript("arguments[0].scrollIntoView(false);", page.IntraRadioButton);
		if(page.FilterView.isDisplayed()) 
			page.FilterView.findElement(By.cssSelector("i.fa.fa-chevron-down.pull-right.toggle-filter-view")).click();
		if (!page.IntraRadioButton.isSelected())
			page.IntraRadioButton.click();
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		LinkedHashSet<ArrayList<String>> ActualRoutePlanInfo = page.GetReport(page.RoutePlanForm);
		LinkedHashSet<ArrayList<String>> ExpectedRoutePlanInfo = CommonData.GetRoutePlanForm("intra");
		if (!ActualRoutePlanInfo.equals(ExpectedRoutePlanInfo)) {
			System.out.println("Intra Route Plan is wrong");
			System.out.println("expected : " + ExpectedRoutePlanInfo);
			System.out.println("actual :   " + ActualRoutePlanInfo);
			fw.write(Nl+"Intra Route Plan is wrong"+Nl);
			fw.write(Nl+"expected result but not show:"+Nl+Function.GetDiffBetwForm(ExpectedRoutePlanInfo, ActualRoutePlanInfo)+Nl);	
			fw.write(Nl+"actual result show but not expected:"+Nl+Function.GetDiffBetwForm(ActualRoutePlanInfo,ExpectedRoutePlanInfo)+Nl);	
		} else {
			System.out.println("intra Route Plan is correct");
			fw.write(Nl+"Intra Route Plan is correct");
		}

	}

	@AfterMethod
	public void afterMethod() {
	}

	@BeforeClass
	public void SetUp() throws IOException, SQLException {
		page = new SysnetPage(driver);
		js = (JavascriptExecutor) driver;
		w1=new WebDriverWait(driver, 120);
		w2=new WebDriverWait(driver, 50);
		if(!page.isVisable(page.RoutePlanButton))
		page.Square.click();
		w1.until(ExpectedConditions.elementToBeClickable(page.RoutePlanButton));
		page.RoutePlanButton.click();
		// (new WebDriverWait(driver,
		// 20)).until(ExpectedConditions.visibilityOf(page.RoutePlanForm));
		w2.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar")));
	    fw.write(Function.GetDisplayTime().replace(":", "-")+Nl);
	    if(page.isVisable(page.RoutePlanButton)&& page.isVisable(page.Square))
			page.Square.click();
	}

	@AfterClass
	public void afterClass() {

	}

}

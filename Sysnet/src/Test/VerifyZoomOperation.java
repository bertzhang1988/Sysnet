package Test;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import Utility.ConfigRd;

import org.testng.annotations.BeforeTest;
import org.openqa.selenium.Keys;
import page.SysnetPage;

public class VerifyZoomOperation {

	private WebDriver driver;
	private SysnetPage p;
	
  //@Test(priority=1)
  public void ZoomByMouse() throws InterruptedException {
	  Thread.sleep(5000);
	  (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("html/body/div[3]/div/svg/line[3]")));
	  WebElement html = driver.findElement(By.xpath("html/body/div[3]/div/svg/line[3]"));
	  html.sendKeys(Keys.chord(Keys.CONTROL, Keys.ADD));
	  html.sendKeys(Keys.chord(Keys.CONTROL, Keys.SUBTRACT));
  }
 
  @BeforeTest
  @Parameters({"browser"})
  public void SetUp(@Optional("chrome")String browser) throws AWTException, InterruptedException, IOException { 
	ConfigRd Conf= new ConfigRd();	
   if (browser.equalsIgnoreCase("chrome")){
   System.setProperty("webdriver.chrome.driver", Conf.GetChromePath());
   driver = new ChromeDriver();            
    }else if(browser.equalsIgnoreCase("ie")){
	System.setProperty("webdriver.ie.driver", Conf.GetIEPath());
	driver=new InternetExplorerDriver();
	 	  }
    
   p= new SysnetPage(driver);
   driver.get(Conf.GetSysnetSitURL());
   driver.manage().window().maximize(); 
	  
  }

  
  //@Test
  public void d() throws InterruptedException{
	  Thread.sleep(5000);
	  JavascriptExecutor js = (JavascriptExecutor) driver;
	 //js.executeScript("document.getElementsByClassName('circle10').style.zoom='200%'");
	  js.executeScript("document.body.style.zoom='200%'");
	  js.executeScript("document.body.style.zoom='-200%'");
  }
  
  
  //@Test 
  public void e() throws InterruptedException{
	  Thread.sleep(5000);
	    JavascriptExecutor jse = (JavascriptExecutor)driver;
	   jse.executeScript("window.scrollBy(0,77350)", "");
	   
	  //  jse.executeScript("scroll(0, 99000000);");
  }
  
  
  
  //@Test
  public void i() throws InterruptedException{
	  Thread.sleep(5000);
	  JavascriptExecutor js = (JavascriptExecutor) driver;
	  js.executeScript("window.scrollTo(5000, document.body.scrollHeight);");
  }
  
  
  @Test 
  public void tt() throws InterruptedException, AWTException{
	  Thread.sleep(5000);
	  Robot robot = new Robot();
	  robot.mouseWheel(-3);
  }
  
  
  //@AfterTest
  public void Close() {
  driver.close();
  }

}

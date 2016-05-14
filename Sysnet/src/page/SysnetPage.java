package page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class SysnetPage {

private	WebDriver driver;
	
public SysnetPage(WebDriver driver){
		this.driver = driver;
	    PageFactory.initElements(driver,this);
	}
	
public String SysnetMap="http://javadev1.yrcw.com:3003/map";
	
	
	
}

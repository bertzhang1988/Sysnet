package page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;

import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class SysnetPage {

private	WebDriver driver;
	
public SysnetPage(WebDriver driver){
		this.driver = driver;
	    PageFactory.initElements(driver,this);
	}
	
	


/*Common */  

@FindBy(how = How.LINK_TEXT, using = "System Summary")
public WebElement SystemSummaryButton;
		
/*SystemSummary*/

@FindBy(how = How.ID, using = "inter_regional")
public WebElement InterRegionalform;

@FindBy(how = How.XPATH, using = ".//span[contains(text(), 'Average Time for LDD + ARV in Hours')]")
public WebElement ATLddArv;

@FindBy(how = How.CSS, using = "div#yrcinterLDDARV>div:nth-of-type(2)>div>div:nth-of-type(3) div.ui-grid-canvas")
public WebElement ATLddArvTrailerInforGrid;











public LinkedHashSet<ArrayList<String>> GetTrailerInfoList(WebElement TrailerInforGrid){
	driver.manage().window().maximize();
	int line=TrailerInforGrid.findElements(By.xpath("div")).size();
     //Set<ArrayList<String>> ProInfo= new HashSet<ArrayList<String>>();       // dont sort the pro list
	LinkedHashSet<ArrayList<String>> ProInfo= new LinkedHashSet<ArrayList<String>>();     // sort the prolist
	for(int i=1;i<=line;i++){
	//String[] Proline1= ArrayUtils.remove(TrailerInforGrid.findElement(By.xpath("div["+i+"]")).getText().split("\\n"), 0);
	String[] Proline1= TrailerInforGrid.findElement(By.xpath("div["+i+"]")).getText().split("\\n");
	ArrayList<String> e1= new ArrayList<String> (Arrays.asList(Proline1));
	ProInfo.add(e1); }
	if(line>=42){
	JavascriptExecutor jse = (JavascriptExecutor)driver;
	int additional=42;
	do{
		jse.executeScript("arguments[0].scrollIntoView(true);",TrailerInforGrid.findElement(By.xpath("div["+additional+"]")));
		additional=TrailerInforGrid.findElements(By.xpath("div")).size();
		for(int j=1;j<=additional;j++) {
		//String[] Proline1= ArrayUtils.remove(TrailerInforGrid.findElement(By.xpath("div["+j+"]")).getText().split("\\n"), 0);
		String[] Proline1= TrailerInforGrid.findElement(By.xpath("div["+j+"]")).getText().split("\\n");
		ArrayList<String> e1= new ArrayList<String> (Arrays.asList(Proline1));
		ProInfo.add(e1);}
		}
	while(additional>42);
	int Rest=TrailerInforGrid.findElements(By.xpath("div")).size();
	for(int j=1;j<=Rest;j++) {
	//String[] Proline1= ArrayUtils.remove(TrailerInforGrid.findElement(By.xpath("div["+j+"]")).getText().split("\\n"), 0);
	String[] Proline1= TrailerInforGrid.findElement(By.xpath("div["+j+"]")).getText().split("\\n");
	ArrayList<String> e1= new ArrayList<String> (Arrays.asList(Proline1));
	ProInfo.add(e1);} 	
    } 
	return ProInfo;
}

}

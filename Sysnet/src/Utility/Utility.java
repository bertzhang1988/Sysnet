package Utility;

import java.io.File;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Utility {

	public static String takescreenshot(WebDriver driver, String shotname) {

		try {
			TakesScreenshot scrShot = (TakesScreenshot) driver;

			// Call getScreenshotAs method to create image file

			File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);

			// Move image file to new destination

			String CDate = Function.GetTimeValue(TimeZone.getDefault().getID());

			File Defolder = new File("./SysnetScreenshot/" + CDate);
			Defolder.mkdir();
			String DestiFilePath = "./SysnetScreenshot/" + CDate + "/ " + shotname + ".png";
			File DestFile = new File(DestiFilePath);

			// Copy file at destination

			FileUtils.copyFile(SrcFile, DestFile);
			return DestiFilePath;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}

	}

}

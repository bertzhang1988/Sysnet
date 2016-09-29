package Utility;

import java.io.File;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Utility {

	public static void takescreenshot(WebDriver driver, String shotname) {

		try {
			TakesScreenshot scrShot = (TakesScreenshot) driver;

			// Call getScreenshotAs method to create image file

			File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);

			// Move image file to new destination

			String CDate = new SimpleDateFormat("YYYY-MM-dd--HH-mm-ss").format(Function.gettime("America/Chicago"));

			File DestFile = new File("./SysnetScreenshot/" + shotname + "/ " + CDate + ".png");

			// Copy file at destination

			FileUtils.copyFile(SrcFile, DestFile);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package Utility.SetUpBase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.TimeZone;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import Utility.Function;

public class SetupBrowserAndExcelReport extends SetupBase {

	protected XSSFWorkbook workbook;
	protected TimeZone defaultTimeZone;

	@BeforeClass
	public void SetUpExcel() {
		workbook = new XSSFWorkbook();
		defaultTimeZone = TimeZone.getDefault();
	}

	@AfterClass
	public void CloseExcel() {
		/*
		 * greate report folder by using the current time as folder name, and
		 * deliver report into it
		 */
		String CDate = Function.GetTimeValue(defaultTimeZone.getID());
		File file2 = new File("./Report/" + CDate);
		file2.mkdir();
		File file = new File(file2, this.getClass().getName() + ".xlsx");
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

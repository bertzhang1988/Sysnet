package Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;

public class NewTest {

	@Test
	public void excel() throws IOException {

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Sysnet Excel Write");

		Object[][] SysnetData = { { "Header 1", "Header 2", 23 }, { "data 1", "data2", 34 },

		};

		int rowCount = 0;

		for (Object[] sysnetdata : SysnetData) {
			Row row = sheet.createRow(rowCount);
			++rowCount;
			int columnCount = 0;

			for (Object field : sysnetdata) {
				Cell cell = row.createCell(columnCount);
				if (field instanceof String) {
					cell.setCellValue((String) field);
				} else if (field instanceof Integer) {
					cell.setCellValue((Integer) field);
				}
				++columnCount;
			}

		}
		File file2 = new File("./Report/" + rowCount);
		file2.mkdir();
		File file = new File(file2, rowCount + ".xlsx");
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			workbook.write(outputStream);
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
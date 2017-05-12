package Utility.SetUpBase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TimeZone;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import Utility.Function;

/*set Browser and text report*/
public class SetupBrowserAndTextReport extends SetupBase {

	protected FileWriter fw;
	protected String Nl;

	@BeforeClass
	public void SetUpTextReport() {
		// create text file
		String CDate = Function.GetTimeValue(TimeZone.getDefault().getID());
		File file2 = new File("./Report/" + CDate);
		file2.mkdir();
		File file = new File(file2, this.getClass().getName() + ".txt");
		try {
			fw = new FileWriter(file, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Nl = System.getProperty("line.separator");
	}

	@AfterClass
	public void CloseTextReport() {
		if (fw != null)
			try {
				fw.close();
			} catch (IOException e) {

				throw new RuntimeException("Fail to close file writter");
			}
	}

}

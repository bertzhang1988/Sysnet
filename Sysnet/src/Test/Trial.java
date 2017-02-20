package Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBContext;

import org.omg.CORBA.portable.InputStream;
import org.testng.annotations.Test;

public class Trial {

@Test
public void A(){

	  try {

		URL url = new URL("https://coderanch.com/t/592233/java/Write-Java-Client-consume-REST");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		//conn.setRequestProperty("Accept", "application/json");

//		if (conn.getResponseCode() != 200) {
//			throw new RuntimeException("Failed : HTTP error code : "
//					+ conn.getResponseCode());
//		}

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		String output;
		System.out.println("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			System.out.println(output);
		}
        br.close();
		conn.disconnect();

	  } catch (MalformedURLException e) {

		e.printStackTrace();

	  } catch (IOException e) {

		e.printStackTrace();

	  }

	}
}

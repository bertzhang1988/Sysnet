package Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBContext;

import org.omg.CORBA.portable.InputStream;
import static com.jayway.restassured.RestAssured.*;
import org.testng.annotations.Test;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class Trial {

	String s1 = "https://mvnrepository.com/artifact/com.jayway.restassured/json-schema-validator/2.9.0";
	String s2 = "https://sysnetsit1.yrcw.com/webapps/sysnet/driver/terminal/123/MBK/regionType/INTER/driverType/Bid/null/false,false,false/false,false/4";
	String s3 = "https://samples.openweathermap.org/data/2.5/find?lat=55.5&lon=37.5&cnt=10&appid=b1b15e88fa797225412429c1c50c122a1";
	String s4 = "https://spasit1.yrcw.com/webapps/yrc-equipstatus/api/v1/validations/trailer/12345";
	String s5 = "https://coderanch.com/t/592233/java/Write-Java-Client-consume-REST";

	@Test
	public void A() {

		try {

			URL url = new URL(s3);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			// conn.setRequestProperty("Accept", "application/json");

			// if (conn.getResponseCode() != 200) {
			// throw new RuntimeException("Failed : HTTP error code : "
			// + conn.getResponseCode());
			// }

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

	@Test
	
	public void f(){
		
		
		
		 try {

				URL url = new URL(" https://spasit1.yrcw.com/webapps/yrc-equipstatus/api/v1/validations/trailer/12345");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setRequestProperty("X-Authorization", "YRC eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpZCI6InV5cjI3YjAiLCJzaG9ydElkIjoidXlyMjdiMCIsImxvY2F0aW9uIjoiOTAzIiwibmFtZSI6IlpoYW5nLCBCZXJ0IiwiZW1haWwiOiJCZXJ0LlpoYW5nQFlSQ0ZyZWlnaHQuY29tIiwiZ3JvdXBzIjpbIkRvbWFpbiBVc2VycyIsIktDR08gVXNlcnMiLCJQQ0ludmVudG9yeSIsInlzaGxwZGtfcmVzdCIsIllUX0N1c3RvbWVyX1N2cyIsIllUX0N1c3RvbWVyX1N1cHBvcnQiLCJZVF9Vc2VycyIsIllUX1RlY2hfU2VydmljZXMiLCJZVEFMTFVTUiIsIllSVF9UZWNoX0luZnJhc3RydWN0dXJlX1N2Y3MiLCJZUlRfSVRfU3VwcG9ydF9TZXJ2aWNlcyIsIllUX0VuZF9Vc2VyX1NlcnZpY2VzIiwiWVRfVGVjaF9JbmZyYXN0cnVjdHVyZV9TdmNzIiwiUk9MRS1CSVpPQkotQWRtaW4iLCJST0xFLVRyYWluaW5nLVVzZXIiLCJST0xFLVlSVEVDSCIsIkluZlNlYyBDUyBMaXN0IiwiVVNGSExQREsiLCJZVEFMTFVTUl9LR08iLCJXMEltZ1VzZXJFbnRyeSIsIlcwQWRob2NFbnRyeSIsIkhEX0tub3dsZWRnZUdyb3VwIiwiVlBORVhDUF9ZVF9LR08iLCJDbGFyaXR5X1VzZXJzIiwiTE9DUmlza1Jlc2NTdnMiLCJXMEN1c3RTZXJ2TWFpbnRUcm1QcmludCIsIkxPQ1Jpc2tSZXNjU3ZzX0hlbHBEZXNrIiwiWVNITFBES19Td2l0Y2hib2FyZF9VcGRhdGUiLCJZU0hMUERLX0hvbWVEaXIiLCJYZW5BcHBfUmVtb3RlQWNjZXNzIiwiWVJUX0V4Y2hhbmdlXzIwMTBfVXNlcnMiLCJDdWx0dXJlQ2x1Yl9BdXRob3JzIiwiVXNyX1lSQ19QQV9HbG9iYWxQIiwiR1BPX0NSTV9Qcm9kIiwiREJfQWNjZXNzX1NRQV9TdGciLCJZUkNXX1lSQ0YgLSBDb2RlIG9mIENvbmR1Y3QiLCJEb2NrIENvbW1hbmQgQ2VudGVyIiwiWVRBTExfRUMiLCJEQl9BY2Nlc3NfRXF1aXBtZW50IiwiRXF1aXBTdGF0VXBkYXRlIiwiREJfQWNjZXNzX0VudEluZ3JhdGlvbiJdLCJlbXBsb3llZUlkIjoiMjUwNDI3MDA3IiwibWFpbmZyYW1lSWQiOiJ1eXIyN2IwIiwiaWF0IjoxNDg4NDkwNjM5LCJleHAiOjE0ODg0OTQyMzksImlzcyI6IllSQyJ9.YkGN_LMT15qiU8hS4oij-kscFJizuZdmTQfWvoTG4k6PDryL6-En11SJhwcLnTf9GkouNVO7O7L_BiiN6eYIVAuCfjVufBX3fLuBPCDqY1Ho7T54jNbAqdVwGJITV07e1NJdTPfd-eSRy6_qS-7JIXcPa5os55x9lLYPJehrYnOenaOUSNAZT5shpVThEkd1amR7ZC6avz74SYD8K5kceIIBEoL2wQUogUpEZJmJfKiiWMcNRknO86VVgIOgVE8v7K_ncD787GR8u3AVyJAf5IXNqgiDN4s5WwB-eJMyyt2T-Gu0L1uZ4QPW39TBwtPHijMxrC1wmBeTylL30HxqDw");

				String input = "{\"qty\":100,\"name\":\"iPad 4\"}";

				OutputStream os = conn.getOutputStream();
				os.write(input.getBytes());
				os.flush();

				if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
					throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
				}

				conn.disconnect();

			  } catch (MalformedURLException e) {

				e.printStackTrace();

			  } catch (IOException e) {

				e.printStackTrace();

			 }
	}
	@Test
	public void b() {

		Response res = when().get(s4);
		System.out.println(res.asString());
	}

	@Test
	public void c() {
		String s3 = "https://samples.openweathermap.org/data/2.5/find?lat=55.5&lon=37.5&cnt=10&appid=b1b15e88fa797225412429c1c50c122a1";
		Response res = given().get(s3);
		String result = res.then().contentType(ContentType.JSON).extract().path("cod[0]");
		System.out.println(result);

	}

	@Test
	public void d(){
		
		
		
	}
}

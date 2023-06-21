package com.henrynkuke.ezeeconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.xml.soap.SOAPException;
import java.io.IOException;

@SpringBootApplication
public class EzeeconsumerApplication {

	public static void main(String[] args) {

		SpringApplication.run(EzeeconsumerApplication.class, args);
		testAirtime();
		//testUmeme();
	}

	private static void testAirtime(){
		try {

			var api = new EzeeApiClient();

			String tranId    = String.valueOf(System.currentTimeMillis());
			String cutomerNo = "0705596470";
			String amount    = "1000";
			String paycode   = "1234";

			api.ezeeAirtime(tranId, cutomerNo, amount,paycode);

		} catch (SOAPException | IOException e) {
			e.printStackTrace();
		}
	}


	private static void testUmeme(){

		try {
			var api = new EzeeApiClient();

			String tranId    = String.valueOf(System.currentTimeMillis());
			String cutomerNo = "04500050590005";
			String amount    = "1000";
			String paycode   = "2103";

			api.umemeValidate(tranId, cutomerNo, amount,paycode);

		} catch (SOAPException | IOException e) {
			e.printStackTrace();
		}

	}
	


}

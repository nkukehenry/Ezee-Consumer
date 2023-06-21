package com.henrynkuke.ezeeconsumer;
import org.apache.commons.logging.Log;

import javax.xml.soap.*;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConnection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.soap.SAAJMetaFactory;

public class EzeeApiClient {

	private static final String EZEE_CODE = "14630160";
	private static final String EZEE_USERNAME = "4295448305";
	private static final String EZEE_PASSWORD = "27CBC22ACC";
	private static final String EZEE_URL = "http://mmpuchong.mobile-money.com:1668/EMTerminalAPI/API.svc?wsdl";

	private SOAPMessage createRequest(String method, Map<String, String> params) throws SOAPException {
		try {

			MessageFactory messageFactory = MessageFactory.newInstance();
			SOAPMessage soapMessage   = messageFactory.createMessage();
			SOAPEnvelope soapEnvelope = soapMessage.getSOAPPart().getEnvelope();
			soapEnvelope.addNamespaceDeclaration("tem", "http://tempuri.org/");

			SOAPBody soapBody       = soapEnvelope.getBody();
			SOAPElement soapElement = soapBody.addChildElement(method, "tem");

			for (Map.Entry<String, String> entry : params.entrySet()) {

				String paramName  = entry.getKey();
				String paramValue = entry.getValue();

				SOAPElement paramElement = soapElement.addChildElement(paramName);
				paramElement.setTextContent(paramValue);
			}

			soapMessage.saveChanges();

			return soapMessage;

		} catch (SOAPException e) {
			e.printStackTrace();
			throw e;
		}
	}

	private SOAPMessage callSoapApi(String method, Map<String, String> params) throws SOAPException, IOException {
		SOAPConnection connection = null;
		try {
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			connection = soapConnectionFactory.createConnection();

			SOAPMessage request = createRequest(method, params);

			// Set the appropriate SOAP action header
			MimeHeaders headers = request.getMimeHeaders();
			headers.addHeader("SOAPAction", "\"http://tempuri.org/IService/"+method+"\"");

			SOAPMessage response = connection.call(request, EZEE_URL);
			return response;

		} catch (SOAPException e) {
			e.printStackTrace();
			throw e;
		}
		finally {
			if (connection != null) {
				connection.close();
			}
		}
	}


	public void ezeeAirtime(String transactionId, String customerNo, String amount, String paycode) throws SOAPException, IOException {
		try {
			Map<String, String> params = new HashMap<>();
			params.put("sccode", EZEE_CODE);
			params.put("userid", EZEE_USERNAME);
			params.put("password", EZEE_PASSWORD);
			params.put("sctxnid", transactionId);
			params.put("trxtype", "1");
			params.put("phoneno", customerNo);
			params.put("amount", amount);
			params.put("pinKeyword", paycode);

			SOAPMessage response = callSoapApi("AirTimeTopUp", params);

			if (response.getSOAPBody().hasFault()) {
				SOAPFault fault = response.getSOAPBody().getFault();
				throw new RuntimeException("SOAP Fault: " + fault.getFaultString());
			}

			// Process the SOAP response here
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			response.writeTo(outputStream);

			String responseString = outputStream.toString();

			System.out.println(responseString);

		} catch (SOAPException | IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void umemeValidate(String transactionId, String customerNo, String amount, String paycode) throws SOAPException, IOException {
		try {

			Map<String, String> params = new HashMap<>();
			params.put("sccode", EZEE_CODE);
			params.put("userid", EZEE_USERNAME);
			params.put("password", EZEE_PASSWORD);
			params.put("sctxnid", transactionId);
			params.put("accountno", customerNo);
			params.put("amount", amount);
			params.put("payeecode","UMEME");

			SOAPMessage response = callSoapApi("ValidateBillAccount", params);

			if (response.getSOAPBody().hasFault()) {
				SOAPFault fault = response.getSOAPBody().getFault();
				throw new RuntimeException("SOAP Fault: " + fault.getFaultString());
			}

			// Process the SOAP response here
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			response.writeTo(outputStream);

			String responseString = outputStream.toString();

			System.out.println(responseString);

		} catch (SOAPException | IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void umemePay(String transactionId, String customerNo,String amount, String paycode,String phone,
						 String customerName,String umeme_code) throws SOAPException, IOException {
		try {
			Map<String, String> params = new HashMap<>();
			params.put("sccode", EZEE_CODE);
			params.put("userid", EZEE_USERNAME);
			params.put("password", EZEE_PASSWORD);
			params.put("sctxnid", transactionId);
			params.put("accountno", customerNo);
			params.put("amount", amount);
			params.put("payeecode","UMEME");
			params.put("phoneno'",phone);
			params.put("remark1",customerName); //
			params.put("txntype",umeme_code); //1 yaka ,postpaid 2

			SOAPMessage response = callSoapApi("PayBill", params);

			if (response.getSOAPBody().hasFault()) {
				SOAPFault fault = response.getSOAPBody().getFault();
				throw new RuntimeException("SOAP Fault: " + fault.getFaultString());
			}

			// Process the SOAP response here
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			response.writeTo(outputStream);

			String responseString = outputStream.toString();

			System.out.println(responseString);

		} catch (SOAPException | IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

}

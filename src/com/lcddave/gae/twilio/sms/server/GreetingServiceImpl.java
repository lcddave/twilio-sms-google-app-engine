package com.lcddave.gae.twilio.sms.server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;

import com.lcddave.gae.twilio.sms.client.GreetingService;
import com.lcddave.gae.twilio.sms.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	private final static String ORIGINATING_PHONE_NUMBER = "YOUR_NUMBER";
	private final static String TWILIO_SID = "YOUR_SID";
	private final static String TWILIO_AUTH_TOKEN = "YOUR_AUTH";
	
	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid.
		if (!FieldVerifier.isValidNANPPhoneNumber(input)) {
			// If the input is not valid, throw an IllegalArgumentException back
			// to
			// the client.
			throw new IllegalArgumentException(
					"Phone number doesn't match xxx-xxx-xxxx pattern");
		}

		// String serverInfo = getServletContext().getServerInfo();
		// String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script
		// vulnerabilities.
		// input = escapeHtml(input);
		// userAgent = escapeHtml(userAgent);

		String requestBody = "From=%2B1 " + ORIGINATING_PHONE_NUMBER + "&To=%2B1 " + input
				+ "&" + "Body=" + new Date() + "\n\n";

		try {
			System.out.println(requestBody);
			URL url = new URL(
					"https://api.twilio.com/2010-04-01/Accounts/" + TWILIO_SID + "/SMS/Messages.json");
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			setBasicAuth(connection, TWILIO_SID, TWILIO_AUTH_TOKEN);
			OutputStreamWriter writer = new OutputStreamWriter(
					connection.getOutputStream());
			writer.write(requestBody);
			writer.flush();
			writer.close();

			System.out.println(connection.getResponseCode());
			System.out.println(connection.getResponseMessage());
			byte[] buffer = new byte[8192];
			connection.getInputStream().read(buffer);
			System.out.println(new String(buffer));
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

			} else {
				// Server returned HTTP error code.
			}
		} catch (MalformedURLException e) {
			System.out.println(e);
			System.out.println(e.getStackTrace());
		} catch (IOException e) {
			System.out.println(e);
			System.out.println(e.getStackTrace());
		}

		return "I am sending the current time to " + input;
	}

	public static void setBasicAuth(HttpURLConnection connection,
			String username, String password) {
		StringBuilder buf = new StringBuilder(username);
		buf.append(':');
		buf.append(password);
		System.out.println(buf);
		byte[] bytes = null;
		try {
			bytes = buf.toString().getBytes("ISO-8859-1");
		} catch (java.io.UnsupportedEncodingException uee) {
			assert false;
		}

		String header = "Basic "
				+ new String(Base64.encodeBase64(bytes, false));
		System.out.println(header);
		connection.setRequestProperty("Authorization", header);
	}
}

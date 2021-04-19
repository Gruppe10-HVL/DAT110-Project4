package no.hvl.dat110.aciotdevice.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.Gson;

public class RestClient {

	public RestClient() {
		// TODO Auto-generated constructor stub
	}

	private static String logpath = "/accessdevice/log";

	public void doPostAccessEntry(String message) {

		try (Socket s = new Socket(Configuration.host, Configuration.port)) {
			Gson gson = new Gson();

			String body = gson.toJson(new AccessMessage(message));

			String httpPostReq = "POST " + logpath + " HTTP/1.1\r\n" + "Accept: application/json\r\n"
					+ "Content-length: " + body.length() + "\r\n" + "Host: localhost\r\n" + "Connection: close\r\n"
					+ "\r\n" + body + "\r\n";

			OutputStream output = s.getOutputStream();

			// Send response to TCP Connection
			PrintWriter writer = new PrintWriter(output, false);

			writer.print(httpPostReq);
			writer.flush();

			// read the HTTP response
			InputStream in = s.getInputStream();
			Scanner reader = new Scanner(in);
			StringBuilder jsonresponse = new StringBuilder();
			boolean header = true;

			while (reader.hasNext()) {
				String nextline = reader.nextLine();

				if (header) {
					System.out.println(nextline);
				} else {
					jsonresponse.append(nextline);
				}

				if (nextline.isEmpty()) {
					header = false;
				}
			}

			System.out.println("BODY:");
			System.out.println(jsonresponse.toString());

		} catch (IOException e) {
			System.err.println(e);
		}

	}

	private static String codepath = "/accessdevice/code";

	public AccessCode doGetAccessCode() {

		AccessCode code = null;

		// TODO: implement a HTTP GET on the service to get current access code

		try (Socket s = new Socket(Configuration.host, Configuration.port)) {

			// construct the GET request
			String httpGetReq = "GET " + codepath + " HTTP/1.1\r\n" 
					+ "Accept: application/json\r\n"
					+ "Host: localhost\r\n" 
					+ "Connection: close\r\n" 
					+ "\r\n";

			// send the HTTP request
			OutputStream output = s.getOutputStream();
			PrintWriter pwr = new PrintWriter(output, false);
			pwr.print(httpGetReq);
			pwr.flush();

			// read the HTTP response
			InputStream in = s.getInputStream();
			Scanner reader = new Scanner(in);
			StringBuilder jsonresponse = new StringBuilder();
			boolean header = true;

			while (reader.hasNext()) {
				String nextline = reader.nextLine();
				if (header) {
					System.out.println(nextline);
				} else {
					jsonresponse.append(nextline);
				}
				// simplified approach to identifying start of body: the empty line
				if (nextline.isEmpty()) {
					header = false;
				}
			}

			reader.close();

			Gson gson = new Gson();
			code = gson.fromJson(jsonresponse.toString(), AccessCode.class);

		} catch (IOException ex) {
			System.err.println(ex);
		}

		return code;
	}
}

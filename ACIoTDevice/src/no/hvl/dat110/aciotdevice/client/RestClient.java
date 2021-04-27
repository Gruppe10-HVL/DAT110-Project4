package no.hvl.dat110.aciotdevice.client;

import java.io.IOException;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RestClient {

	private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	
	public RestClient() {
		// TODO Auto-generated constructor stub
	}

	private static String logpath = "/accessdevice/log";

	public void doPostAccessEntry(String message) {

		OkHttpClient client = new OkHttpClient();
		Gson gson = new Gson();
		AccessMessage msg = new AccessMessage(message);
		RequestBody body = RequestBody.create(JSON, gson.toJson(msg));

		Request request = new Request.Builder()
				.url("http://"+ Configuration.host 
						+ ":" + Configuration.port 
						+ logpath)
				.post(body)
				.build();
		
		System.out.println(request);
		
		Response response;
		try {
			response = client.newCall(request).execute();
			System.out.println(response.body().string());
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static String codepath = "/accessdevice/code";

	public AccessCode doGetAccessCode() {

		AccessCode code = null;

		// TODO: implement a HTTP GET on the service to get current access code
		OkHttpClient client = new OkHttpClient();
		Gson gson = new Gson();
		
		Request request = new Request.Builder()
				.url("http://"+ Configuration.host 
						+ ":" + Configuration.port 
						+ codepath)
				.get()
				.build();
		
		System.out.println(request);
		
		Response response;
		
		try {
			response = client.newCall(request).execute();
			String body = response.body().string();
			System.out.println(body);
			code = gson.fromJson(body, AccessCode.class);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return code;
	}
}

package no.hvl.dat110.ac.restservice;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.put;
import static spark.Spark.post;
import static spark.Spark.delete;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Hello world!
 *
 */
public class App {
	
	static AccessLog accesslog = null;
	static AccessCode accesscode = null;
	
	public static void main(String[] args) {

		if (args.length > 0) {
			port(Integer.parseInt(args[0]));
		} else {
			port(8080);
		}

		// objects for data stored in the service
		
		accesslog = new AccessLog();
		accesscode  = new AccessCode();
		
		after((req, res) -> {
  		  res.type("application/json");
  		});
		
		// for basic testing purposes
		get("/accessdevice/hello", (req, res) -> {
			
		 	Gson gson = new Gson();
		 	
		 	return gson.toJson("IoT Access Control Device");
		});
		
		// TODO: implement the routes required for the access control service
		// as per the HTTP/REST operations describined in the project description
		
		// POST /accessdevice/log/
		post("accessdevice/log", (req, res) -> {
			Gson gson = new Gson();
			JsonObject jsonobj = gson.fromJson(req.body(), JsonObject.class);
			String msg = jsonobj.get("message").getAsString();
			int id = accesslog.add(msg);
			return gson.toJson(accesslog.get(id));
		});
		
		// GET /accessdevice/log/
		get("/accessdevice/log", (req, res) -> {
			return accesslog.toJson();
		});
		
		// GET /accessdevice/log/{id}
		get("/accessdevice/log/:id", (req, res) -> {
			int id = -1;
			
			try {
				id = Integer.parseInt(req.params(":id"));
			} catch (Exception e) {
				System.out.println("Invalid id.");
				e.printStackTrace();
			}
			
			Gson gson = new Gson();
			
			return gson.toJson(accesslog.get(id));
		});
		
		// PUT /accessdevice/code
		put("/accessdevice/code", (req, res) -> {
			Gson gson = new Gson();
			
			AccessCode code = gson.fromJson(req.body(), AccessCode.class);
			accesscode.setAccesscode(code.getAccesscode());
			
			return req.body();
		});
		
		// GET /accessdevice/code
		get("/accessdevice/code", (req, res) -> {
			Gson gson = new Gson();
			return gson.toJson(accesscode);
		});
		
		// DELETE /accessdevice/log
		delete("/accessdevice/log", (req, res) -> {
			accesslog.clear();
			
			return accesslog.toJson();
		});
    }
    
}

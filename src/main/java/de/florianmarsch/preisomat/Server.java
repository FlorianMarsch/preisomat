package de.florianmarsch.preisomat;

import static spark.Spark.exception;
import static spark.Spark.*;

import java.io.FileNotFoundException;
import java.util.UUID;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import spark.Route;
import spark.Spark;

public class Server {

	public Server() {
		// DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public void start(Integer aPort) {
		port(aPort);
		staticFileLocation("/public");
		exception(Exception.class, (e, request, response) -> {
			response.status(500);
			response.header("Content-Type", "application/json");
			try {
				String publicErrorMessage = "Unknown error : " + UUID.randomUUID().toString();
				Exception message = new RuntimeException(publicErrorMessage);
				response.body(mapper.writeValueAsString(message));
				new RuntimeException(publicErrorMessage, e).printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
				// should not happen
				response.body("{\"error\":\"error\"}");
			}
		});
		notFound((req, res) -> {
			res.status(404);
            res.redirect("/");
			res.type("application/json");
			return "{\"message\":\"Custom 404\"}";
		});
		
		before((req, res) -> {
			System.out.println(req.url());
		});
	}

	private ObjectMapper mapper;

	public void get(String path, Route route) {
		Spark.get(path, (request, response) -> {
			Object result = route.handle(request, response);
			response.status(200);
			response.type( "application/json");
			return mapper.writeValueAsString(result);
		});
	};
	
	public void getBinary(String path, Route route) {
		Spark.get(path, (request, response) -> {
			Object result = route.handle(request, response);
			response.status(200);
			return result;
		});
	};
	

	public void post(String path, Route route) {
		Spark.post(path, (request, response) -> {
			Object result = route.handle(request, response);
			response.status(200);
			response.type( "application/json");
			return mapper.writeValueAsString(result);
		});
	};

}

package org.elsys.netprog.rest.test;

import java.util.List;
import java.util.Map;

import org.junit.Assert;

import static io.restassured.RestAssured.*;

import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;

public class IntegrationTest {

	private final String serverBaseAddress = "jersey-rest-homework/";
	public void generateHash() {
			RestAssured.port = 8080;
			RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
			RestAssured.defaultParser = Parser.JSON;
			RestAssured.baseURI = "http://localhost";
			
			
			Response info = given().basePath(serverBaseAddress).get("info");
			info.then().statusCode(200);
			String infoString = info.getBody().asString();
			System.out.println(infoString);
		}
		
}

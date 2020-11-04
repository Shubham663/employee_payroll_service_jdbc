package com.bridgelabz.employee_payroll_service_jdbc;

import java.sql.Date;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * Tests JSON Server Operations on the Employee Payroll
 * @author Shubham
 *
 */
public class EmployeeJsonServerTest {

	@Before
	public void init() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
		
	}
	@Test
	public void addEmployeeToJsonServerTest() {
		Date date = Date.valueOf("2020-05-01");
		Employees employees = new Employees("Harleen Kaur", 15,20000, date ,"Female",20000,10000,10000,1000,19000,9878767651l);
		
		Response response = RestAssured.given()
								.contentType(ContentType.JSON)
								.accept(ContentType.JSON)
								.body("{\"name\": \"Harleen Kaur\",\"salary\": \"20000\"}")
								.when()
								.post("/employees/create");
		
		String responseAsString = response.asString();
		System.out.println(responseAsString);
		JsonObject jsonObject = new Gson().fromJson(responseAsString, JsonObject.class);
		int id = jsonObject.get("id").getAsInt();
		response.then().body("id", Matchers.any(Integer.class));
		response.then().body("name", Matchers.is("Harleen Kaur"));
		response.then().body("salary",Matchers.is("20000"));
	}
}

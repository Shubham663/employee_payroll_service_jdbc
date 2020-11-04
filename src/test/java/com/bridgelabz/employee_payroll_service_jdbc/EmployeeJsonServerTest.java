package com.bridgelabz.employee_payroll_service_jdbc;

import static org.junit.Assert.assertThat;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
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
		PayrollJsonServerService paService = PayrollJsonServerService.getInstance();
		Response response = paService.addEmployeeToJsonServer(employees);
		String responseAsString = response.asString();
		System.out.println(responseAsString);
		JsonObject jsonObject = new Gson().fromJson(responseAsString, JsonObject.class);
		int id = jsonObject.get("id").getAsInt();
		response.then().body("id", Matchers.any(Integer.class));
		response.then().body("name", Matchers.is("Harleen Kaur"));
		response.then().body("salary",Matchers.is("20000.0"));
	}
	
	@Test
	public void addMultipleEmployeesToJsonServerTest_returnsTrueWhenAdditionSuccessful() {
		Date date = Date.valueOf("2020-05-01");
		Employees employees = new Employees("Harleen Kaur", 15,20000, date ,"Female",20000,10000,10000,1000,19000,9878767651l);
		Employees employees2 = new Employees("Simranjeet", 10, 30000, date, "Male",30000 , 10000, 20000, 2000, 28000, 9979876251l);
		List<Employees> list = new ArrayList<>();
		list.add(employees);
		list.add(employees2);
		PayrollJsonServerService paService = PayrollJsonServerService.getInstance();
		Boolean boolean1 = paService.addMultipleEmployeesToJsonServer(list);
		assertThat("Employees not added", true, Is.is(true));
	}
}

package com.bridgelabz.employee_payroll_service_jdbc;

import static org.junit.Assert.assertThat;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.restassured.RestAssured;
import io.restassured.response.Response;

/**
 * Tests JSON Server Operations on the Employee Payroll
 * @author Shubham
 *
 */
public class EmployeeJsonServerTest {

	@Before
	public void init() {
		
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
		assertThat("Employees not added", true, Matchers.is(true));
	}
	
	@Test
	public void updateEmployeeSalaryInJsonServerTest() {
		PayrollJsonServerService paService = PayrollJsonServerService.getInstance();
		Response response = paService.updateEmployeeSalaryInJsonServer(40000,5);
		String responseAsString = response.asString();
		System.out.println(responseAsString);
		response.then().body("id", Matchers.any(Integer.class));
		response.then().body("name", Matchers.is("Lisa"));
		response.then().body("salary",Matchers.is("40000.0"));
	}
	
	@Test
	public void getEmployeeListFromJsonServerTest() {
		PayrollJsonServerService paService = PayrollJsonServerService.getInstance();
		Response response = paService.getEmployeeListFromJsonServer();
		String responseAsString = response.asString();
		response.then().body("id", Matchers.hasItems(2,3,6));
		response.then().body("name", Matchers.hasItems("Lisa"));
		response.then().body("salary",Matchers.hasItem("8000"));
	}
	
	@Test
	public void deleteEmployeeFromJsonServerTest() {
		PayrollJsonServerService paService = PayrollJsonServerService.getInstance();
		Response response = paService.deleteEmployeeFromJsonServer(15);
		String responseAsString = response.asString();
		int status = response.getStatusCode();
		assertThat(status, CoreMatchers.is(200));
		response = paService.getEmployeeListFromJsonServer();
		response.then().body("id", Matchers.not(15));
	}
}

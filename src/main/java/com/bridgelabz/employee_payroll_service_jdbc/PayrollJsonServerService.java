package com.bridgelabz.employee_payroll_service_jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class PayrollJsonServerService {
	private static PayrollJsonServerService paJsService;
	Logger logger;

	private PayrollJsonServerService() {
		logger = LogManager.getLogger();
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
	}

	public static PayrollJsonServerService getInstance() {
		if(paJsService == null)
			paJsService = new PayrollJsonServerService();
		return paJsService;
	}

	public synchronized Response addEmployeeToJsonServer(Employees employees) {
		Response response = RestAssured.given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body("{\"name\": \""+employees.getName()+"\",\"salary\": \""+ employees.getSalary() + "\",\"id\": " + employees.getEmployeeID() + "}")
				.when()
				.post("/employees/create");

		return response;
	}

	public Boolean addMultipleEmployeesToJsonServer(List<Employees> list) {
		Response response = null;
		Map<Integer,Boolean> employeeAddStatus = new HashMap<>();
		list.forEach(employee -> {
			Runnable task = () -> {
				employeeAddStatus.put(employee.hashCode(), false);
				final Response response2 = addEmployeeToJsonServer(employee);
				
				employeeAddStatus.put(employee.hashCode(), true);
			};
			Thread thread = new Thread(task,employee.getName());
			thread.start();
		});
		while(employeeAddStatus.size()<list.size() || employeeAddStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				logger.error("Error while waiting for threads to finish " + e.getMessage());
				return false;
			}
		}
		return true;
	}

	public Response updateEmployeeSalaryInJsonServer(double salary, int id) {
		Response response = RestAssured.given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body("{\"name\": \"Lisa\",\"salary\": \""+ salary + "\"}")
				.when()
				.put("/employees/update/"+id);
		return response;
	}
	
	
}

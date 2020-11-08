package com.bridgelabz.employee_payroll_service_jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Connect {
	static Logger logger = LogManager.getLogger();
	public static void main(String[] args) {
		Connection connection = null;
		PayrollDatabaseService payDataService = PayrollDatabaseService.getInstance();
		try {
			payDataService.loadDriver();
			payDataService.listOfDrivers();
			connection = payDataService.connectToDatabase(connection);
			List<Employees> listEmployees =  payDataService.getListFromDatabase(connection);
			payDataService.updateDetails(connection);
			List<Employees> list = payDataService.getListFromDatabase(connection);
			double salary = 0;
			for (Employees employee : list) {
				if (employee.getName().equals("Terisa"))
					salary = employee.getSalary();
			}
			if (salary == 3000000)
				logger.info("Local list updated successfully " + salary);
			payDataService.updateDetailsPrepared(connection,4000000);
			list = payDataService.getListFromDatabase(connection);
			salary = 0;
			for (Employees employee : list) {
				if (employee.getName().equals("Terisa"))
					salary = employee.getSalary();
			}
			if (salary == 4000000)
				logger.info("Local list updated successfully " + salary);
		} catch (JDBCException exception) {
			logger.error(exception.getMessage());
		}
		if (connection != null)
			try {
				connection.close();
			} catch (SQLException e) {
				logger.error("Error closing connection to database");
			}
	}
}

package com.bridgelabz.employee_payroll_service_jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;

public class Connect 
{
    public static void main( String[] args ){
    	Connection connection = null;
    	
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver Loaded");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
    	listOfDrivers();
    	try {
    		System.out.println("Connecting to database");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/employee_payroll_service?useSSL=false", "root", "Anjali@05");
			System.out.println("Connection Established " + connection);
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	try {
			List<Employees> listOfObjects = getListFromDatabase(connection);
			System.out.println("Successfull retrieval of list");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	if(connection != null)
			try {
				connection.close();
			} catch (SQLException e) {
				System.out.println("Error closing connection to database");
				e.printStackTrace();
			}
    }
    
    private static List<Employees> getListFromDatabase(Connection connection) throws SQLException {
    	Statement stmt = connection.createStatement();
		ResultSet result = stmt.executeQuery("select * from employees");
		List<Employees> listEmployees = new ArrayList<>();
		while(result.next()) {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
			Employees employee = new Employees();
			employee.setEmployeeID(result.getInt(1));
			employee.setName(result.getString(2));
			employee.setSalary( result.getDouble(3));
			employee.setStart_date(result.getDate(4));
			employee.setGender(result.getString(5));
			employee.setBasicPay(result.getDouble(6));
			employee.setDeductions(result.getDouble(7));
			employee.setTaxablePay(result.getDouble(8));
			employee.setIncomeTax(result.getDouble(9));
			employee.setNetPay(result.getDouble(10));
			employee.setPhoneNumber(result.getLong(11));
			listEmployees.add(employee);
			
		}
		result.close();
		stmt.close();
		return listEmployees;
	}

	private static void listOfDrivers() {
		Enumeration<java.sql.Driver> driverList = DriverManager.getDrivers();
		while(driverList.hasMoreElements()) {
			Driver driverClass = driverList.nextElement();
			System.out.println("	"+ driverClass.getClass().getName());
		}
	}
}

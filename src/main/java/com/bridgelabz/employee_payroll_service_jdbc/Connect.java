package com.bridgelabz.employee_payroll_service_jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class Connect 
{
    public static void main( String[] args ){
    	Connection connection;
    	
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
    }
    
    private static void listOfDrivers() {
		Enumeration<java.sql.Driver> driverList = DriverManager.getDrivers();
		while(driverList.hasMoreElements()) {
			Driver driverClass = driverList.nextElement();
			System.out.println("	"+ driverClass.getClass().getName());
		}
	}
}

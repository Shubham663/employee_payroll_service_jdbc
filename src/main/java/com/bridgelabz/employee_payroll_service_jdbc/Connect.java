package com.bridgelabz.employee_payroll_service_jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

import com.bridgelabz.repos.Repos;

public class Connect 
{
    public static void main( String[] args ) throws SQLException{
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
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/employee_payroll_service?useSSL=false", "root", Repos.returnPassword());
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

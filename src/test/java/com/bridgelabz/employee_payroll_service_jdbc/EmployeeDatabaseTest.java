package com.bridgelabz.employee_payroll_service_jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for Employees Payroll Database.
 */
public class EmployeeDatabaseTest 
{
	Logger logger = null;
	PayrollDatabaseService payDataService = null;
	Connection connection = null; 
	List<Employees> listEmployees =  null;
	@Before
	public void init() throws JDBCException {
		logger = LogManager.getLogger();
		payDataService = PayrollDatabaseService.getInstance();
		payDataService.loadDriver();
		connection = payDataService.connectToDatabase(connection);
		listEmployees =  payDataService.getListFromDatabase(connection);
	}
	
    /**
     * Check for data changes inside database
     * @throws JDBCException 
     */
    @Test
    public void updateDataUsingPreparedStatement() throws JDBCException
    {
    	payDataService.updateDetailsPrepared(connection);
		listEmployees = payDataService.getListFromDatabase(connection);
		double salary = 0;
		for (Employees employee : listEmployees) {
			if (employee.getName().equals("Terisa"))
				salary = employee.getSalary();
		}
        assertEquals(4000000, salary,0);
    }
    
    /**
     * Employees joining in particular date range are returned
     * @throws JDBCException 
     */
    @Test
    public void getEmployeesJoininigInParticularRange() throws JDBCException
    {
    	Date date = Date.valueOf("2020-01-02");
    	Date date2 = Date.valueOf("2020-11-31");
    	List<Employees> list = payDataService.getDateRange(connection,date,date2);
    	assertEquals(5, list.size());
    }
    
    /**
     * Gets no of male employees
     */
    @Test
    public void groupFunctionCount() throws JDBCException
    {
    	int value = payDataService.groupFunctionCount(connection);
    	assertEquals(3, value,0);
    }
    
    /**
     * Gets sum of salary of male employees
     */
    @Test
    public void groupFunctionSum() throws JDBCException
    {
    	double value = payDataService.groupFunctionSum(connection);
    	assertEquals(160000, value,0);
    }
    
    /**
     * Gets minimum of salary of male employees
     */
    @Test
    public void groupFunctionMin() throws JDBCException
    {
    	double value = payDataService.groupFunctionMin(connection);
    	assertEquals(45000, value,0);
    }
    
    /**
     * Gets maximum of salary of male employees
     */
    @Test
    public void groupFunctionMax() throws JDBCException
    {
    	double value = payDataService.groupFunctionMax(connection);
    	assertEquals(65000, value,0);
    }
    
    /**
     * Gets average of salary of Female employees
     */
    @Test
    public void groupFunctionAvg() throws JDBCException
    {
    	double value = payDataService.groupFunctionAvg(connection);
    	assertEquals(1300000, value,0);
    }
    
    /**
     * Check for data changes inside database
     * @throws JDBCException 
     */
    @Test
    public void updateDataUsingStatement() throws JDBCException
    {
    	payDataService.updateDetails(connection);
		listEmployees = payDataService.getListFromDatabase(connection);
		double salary = 0;
		for (Employees employee : listEmployees) {
			if (employee.getName().equals("Terisa"))
				salary = employee.getSalary();
		}
        assertEquals(3000000, salary,0);
    }
    
    @Test
    public void addEmployeeInDatabase() throws JDBCException
    {
    	Employees employees = new Employees();
    	employees.setEmployeeID(7);
    	employees.setName("Garry");
    	employees.setBasicPay(50000);
    	employees.setSalary(50000);
    	employees.setDeductions(30000);
    	employees.setGender("Male");
    	employees.setIncomeTax(3600);
    	employees.setNetPay(16400);
    	employees.setPhoneNumber(8676754675l);
    	Date date = Date.valueOf("2020-08-01");
    	employees.setStart_date(date);
    	employees.setTaxablePay(20000);
		payDataService.addEmployee(connection, employees);
		List<Employees> employees2 = payDataService.getListFromDatabase(connection);
		Employees employees3 = (Employees)employees2.get(employees2.size()-1);
		assertTrue(employees3.equals(employees));
    }
    
    @After
    public void endMethod() throws JDBCException {
    	try {
    			if(payDataService.getPreparedStatement() != null)
    				payDataService.getPreparedStatement().close();
			}catch(SQLException exception) {
				throw new JDBCException("Error while closing resources when updating database prepared database" + connection + exception.getMessage());
			}
    }
}

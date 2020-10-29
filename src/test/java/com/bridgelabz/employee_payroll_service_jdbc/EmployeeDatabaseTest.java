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
    
    @After
    public void endMethod() throws JDBCException {
    	try {
				payDataService.getPreparedStatement().close();
			}catch(SQLException exception) {
				throw new JDBCException("Error while closing resources when updating database prepared database" + connection + exception.getMessage());
			}
    }
}

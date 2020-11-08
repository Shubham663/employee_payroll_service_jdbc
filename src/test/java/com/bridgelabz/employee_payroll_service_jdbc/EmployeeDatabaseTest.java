package com.bridgelabz.employee_payroll_service_jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
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
    public void updateDataUsingPreparedStatement()
    {
    	try {
	    	payDataService.updateDetailsPrepared(connection,4000000);
			listEmployees = payDataService.getListFromDatabase(connection);
			double salary = 0;
			for (Employees employee : listEmployees) {
				if (employee.getName().equals("Terisa"))
					salary = employee.getSalary();
			}
	        assertEquals(4000000, salary,0);
	    }catch(JDBCException exception) {
	    	assertTrue(exception.getMessage().equals("Error while updating with prepared Statement "));
	    }
    }
    
    /**
     * Employees joining in particular date range are returned
     * @throws JDBCException 
     */
    @Test
    public void getEmployeesJoininigInParticularRange()
    {
    	try {
	    	Date date = Date.valueOf("2020-01-02");
	    	Date date2 = Date.valueOf("2020-11-31");
	    	List<Employees> list = payDataService.getDateRange(connection,date,date2);
	    	assertEquals(5, list.size());
    	}catch(JDBCException exception) {
    		assertTrue(exception.getMessage().contains("Error while retrieving"));
    	}
    }
    
    /**
     * Gets no of male employees
     */
    @Test
    public void groupFunctionCount()
    {
    	try {
	    	int value = payDataService.groupFunctionCount(connection);
	    	assertEquals(3, value,0);
	    }catch(JDBCException exception) {
	    	assertEquals("Error while getting count using group statements with prepared Statement ", exception.getMessage());
	    }
    }
    
    /**
     * Gets sum of salary of male employees
     */
    @Test
    public void groupFunctionSum()
    {
    	try {
	    	double value = payDataService.groupFunctionSum(connection);
	    	assertEquals(160000, value,0);
	    }catch(JDBCException exception) {
	    	assertEquals("Error while getting sum using group statements with prepared Statement ", exception.getMessage());
	    }
    }
    
    /**
     * Gets minimum of salary of male employees
     */
    @Test
    public void groupFunctionMin()
    {
    	try {
	    	double value = payDataService.groupFunctionMin(connection);
	    	assertEquals(45000, value,0);
    	}catch(JDBCException exception) {
    		assertEquals("Error while getting min using group statements with prepared Statement ", exception.getMessage());
    	}
    }
    
    /**
     * Gets maximum of salary of male employees
     */
    @Test
    public void groupFunctionMax()
    {
    	try {
	    	double value = payDataService.groupFunctionMax(connection);
	    	assertEquals(65000, value,0);
	    }catch(JDBCException exception){
	    	assertEquals("Error while getting max using group statements with prepared Statement ", exception.getMessage());
	    }
    }
    
    /**
     * Gets average of salary of Female employees
     */
    @Test
    public void groupFunctionAvg()
    {
    	try {
	    	double value = payDataService.groupFunctionAvg(connection);
	    	assertEquals(1650000, value,0);
    	}catch(JDBCException exception) {
    		assertEquals("Error while getting avg using group statements with prepared Statement ", exception.getMessage());
    	}
    }
    
    /**
     * Check for data changes inside database
     * @throws JDBCException 
     */
    @Test
    public void updateDataUsingStatement()
    {
    	try {
	    	int recordsUpdated = payDataService.updateDetails(connection);
	        assertEquals(1, recordsUpdated,0);
    	}catch(JDBCException exception) {
    		assertEquals("Error while updating salary", exception.getMessage());
    	}
    }
    
    @Test
    public void addEmployeeInDatabase()
    {
    	List<Employees> employees2 = null;
    	try {
	    	Date date = Date.valueOf("2020-08-01");
	    	Employees employees = new Employees("Garry", 7, 50000, date, "Male", 50000, 30000, 20000, 3600, 46400, 8676754675l);
			payDataService.addEmployee(connection, employees);
			employees2 = payDataService.getListFromDatabase(connection);
			Employees employees3 = (Employees)employees2.get(employees2.size()-1);
			assertTrue(employees3.equals(employees));
    	}catch(JDBCException exception) {
    		assertEquals("Error while adding employee to database with prepared Statement ", exception.getMessage());
    	}
    	try {
			payDataService.deleteRecord(connection,employees2.size());
    	}catch(JDBCException exception) {
    		assertEquals("Error while deleting record from database with prepared Statement ", exception.getMessage());
    	}
    }
    
    @Test
    public void addEmployeeInDatabasePayroll()
    {
    	Employees employees = null;
    	try {
	    	Date date = Date.valueOf("2020-08-01");
	    	employees = new Employees("Garry", 7, 50000, date, "Male", 50000, 30000, 20000, 3600, 46400, 8676754675l);
			payDataService.addEmployeePayroll(connection, employees);
			List<EmployeesNoPayroll> employeesNoPayrolls = payDataService.getListFromDatabaseEmployeeNoPayroll(connection);
			EmployeesNoPayroll employeesNoPayroll = new EmployeesNoPayroll(employees.getName(), employees.getEmployeeID(), employees.getStart_date(), employees.getGender(), employees.getPhoneNumber());
			EmployeesNoPayroll employeesNoPayroll2 = employeesNoPayrolls.get(employeesNoPayrolls.size()-1);
			assertTrue(employeesNoPayroll.equals(employeesNoPayroll2));
    	}catch(JDBCException exception) {
    		assertEquals("Error while inserting data into multiple tables with prepared Statement ", exception.getMessage());
    	}
    	try {
    		payDataService.deleteRecordPayroll(connection,employees.getEmployeeID());
    	}catch(JDBCException exception) {
    		assertEquals("Error while deleting record from database with prepared Statement ", exception.getMessage());
    	}
    }
    
    @Test
    public void addEmployeeInDatabasePayrollTransactions() throws JDBCException
    {
    	Employees employees = null;
    	try {
	    	Date date = Date.valueOf("2020-08-01");
	    	employees = new Employees("Garry", 7, 50000, date, "Male", 50000, 30000, 20000, 3600, 46400, 8676754675l);
			payDataService.addEmployeePayroll(connection, employees);
			List<EmployeesNoPayroll> employeesNoPayrolls = payDataService.getListFromDatabaseEmployeeNoPayroll(connection);
			EmployeesNoPayroll employeesNoPayroll = new EmployeesNoPayroll(employees.getName(), employees.getEmployeeID(), employees.getStart_date(), employees.getGender(), employees.getPhoneNumber());
			EmployeesNoPayroll employeesNoPayroll2 = employeesNoPayrolls.get(employeesNoPayrolls.size()-1);
			assertTrue(employeesNoPayroll.equals(employeesNoPayroll2));
    	}catch(JDBCException exception) {
    		assertEquals("Error while inserting data into multiple tables with prepared Statement ", exception.getMessage());
    	}
    	try {
			payDataService.deleteRecordPayrollAfterCascade(connection,employees.getEmployeeID());
    	}catch(JDBCException exception) {
    		assertEquals("Error while deleting record from database with prepared Statement ", exception.getMessage());
    	}
    }
    
    @Test
    public void addEmployeePayrollInDatabaseTransactions()
    {
    	Employees employees = null;
    	Date date = Date.valueOf("2020-08-01");
    	employees = new Employees("Garry", 7, 50000, date, "Male", 50000, 30000, 20000, 3600, 46400, 8676754675l);
    	EmployeePayroll employeePayroll = new EmployeePayroll();
    	
    	Departments departments = new Departments();
    	departments.setAddress("Lucknow");
    	departments.setDepartmentName("HR");
    	departments.setId(7);
    	
    	EmployeesNoPayroll employeesNoPayroll = new EmployeesNoPayroll(employees.getName(), employees.getEmployeeID(), employees.getStart_date(), employees.getGender(), employees.getPhoneNumber());
    	Payroll payroll = new Payroll(employees.getBasicPay(), employees.getDeductions(), employees.getTaxablePay(), employees.getIncomeTax(), employees.getNetPay());
    	
    	employeePayroll.setEmployee(employeesNoPayroll);
    	employeePayroll.setPayroll(payroll);
    	employeePayroll.getDepartments().add(departments);
    	try {
	    	payDataService.addEmployeePayrollWhole(connection,employeePayroll,employees);
	    	
			List<EmployeesNoPayroll> employeesNoPayrolls = payDataService.getListFromDatabaseEmployeeNoPayroll(connection);
			EmployeesNoPayroll employeesNoPayroll2 = new EmployeesNoPayroll(employees.getName(), employees.getEmployeeID(), employees.getStart_date(), employees.getGender(), employees.getPhoneNumber());
			EmployeesNoPayroll employeesNoPayroll3 = employeesNoPayrolls.get(employeesNoPayrolls.size()-1);
			assertTrue(employeesNoPayroll3.equals(employeesNoPayroll2));
    	}catch(JDBCException exception) {
    		assertEquals("Error while inserting data into multiple tables with prepared Statement ", exception.getMessage());
    	}
    	try {
    		payDataService.deleteRecordEmployeePayrollAfterCascade(connection,employees.getEmployeeID(),departments.getId());
    	}catch(JDBCException exception) {
    		assertEquals("Error while deleting record ", exception.getMessage());
    	}
    }
    
    @Test
    public void deleteEmployeePayrollInDatabaseTransactions()
    {
    	Employees employees = null;
    	Date date = Date.valueOf("2020-08-01");
    	employees = new Employees("Garry", 7, 50000, date, "Male", 50000, 30000, 20000, 3600, 46400, 8676754675l);
    	EmployeePayroll employeePayroll = new EmployeePayroll();
    	Departments departments = new Departments();
    	departments.setAddress("Lucknow");
    	departments.setDepartmentName("HR");
    	departments.setId(7);
    	EmployeesNoPayroll employeesNoPayroll = new EmployeesNoPayroll(employees.getName(), employees.getEmployeeID(), employees.getStart_date(), employees.getGender(), employees.getPhoneNumber());
    	Payroll payroll = new Payroll(employees.getBasicPay(), employees.getDeductions(), employees.getTaxablePay(), employees.getIncomeTax(), employees.getNetPay());
    	employeePayroll.setEmployee(employeesNoPayroll);
    	employeePayroll.setPayroll(payroll);
    	employeePayroll.getDepartments().add(departments);
    	try {
	    	payDataService.addEmployeePayrollWhole(connection,employeePayroll,employees);
			payDataService.removeEmployeePayroll(connection,employees.getEmployeeID(),departments.getId());
			List<EmployeesNoPayroll> employeesNoPayrolls = payDataService.getListFromDatabaseIsActive(connection);
			System.out.println(employeesNoPayrolls);
    	}catch(JDBCException exception) {
    		assertEquals("Error while running removing data with prepared Statement ", exception.getMessage());
    	}
    }
    
    @Test
    public void addEmployeesNoPayrollInDatabase() throws JDBCException
    {
    	Date date = Date.valueOf("2020-08-01");
    	Employees employees = new Employees("Garry", 7, 50000, date, "Male", 50000, 10000, 40000, 4000, 46000, 8676754675l);
    	Employees employees2 = new Employees("Harry", 8, 50000, date, "Male", 50000, 10000, 40000, 4000, 46000, 8698754675l);
    	listEmployees.clear();
    	listEmployees.add(employees);
    	listEmployees.add(employees2);
    	Instant start = Instant.now();
    	payDataService.addMultipleEmployees(connection,listEmployees);
		Instant end = Instant.now();
		System.out.println("Execution time : " + Duration.between(start, end));
		for(Employees employees3 : listEmployees) {
			payDataService.deleteRecord(connection,employees3.getEmployeeID());
		}
		listEmployees = payDataService.getListFromDatabase(connection);
		assertEquals(6, listEmployees.size());
    }
    
    @Test
    public void addEmployeesNoPayrollInDatabaseThreads() throws JDBCException
    {
    	Date date = Date.valueOf("2020-08-01");
    	Employees employees = new Employees("Garry", 9, 50000, date, "Male", 50000, 10000, 40000, 4000, 46000, 8676754675l);
    	Employees employees2 = new Employees("Harry", 10, 50000, date, "Male", 50000, 10000, 40000, 4000, 46000, 8698754675l);
    	listEmployees.clear();
    	listEmployees.add(employees);
    	listEmployees.add(employees2);
    	Instant start = Instant.now();
    	payDataService.addMultipleEmployeesThreads(connection,listEmployees);
		Instant end = Instant.now();
		System.out.println("Execution time : " + Duration.between(start, end));
		for(Employees employees3 : listEmployees) {
			payDataService.deleteRecord(connection,employees3.getEmployeeID());
		}
		listEmployees = payDataService.getListFromDatabase(connection);
		assertEquals(6, listEmployees.size());
    }
    
    @Test
    public void addMultipleEmployeePayrollInDatabaseTransactionsThreads() throws JDBCException
    {
    	Date date = Date.valueOf("2020-08-01");
    	Employees employees = new Employees("Garry", 9, 50000, date, "Male", 50000, 10000, 40000, 4000, 46000, 8676754675l);
    	Employees employees2 = new Employees("Harry", 10, 50000, date, "Male", 50000, 10000, 40000, 4000, 46000, 8698754675l);
    	
    	Departments departments = new Departments();
    	departments.setAddress("Lucknow");
    	departments.setDepartmentName("HR");
    	departments.setId(7);
    	
    	Departments departments2 = new Departments();
    	departments2.setAddress("Ludhiana");
    	departments2.setDepartmentName("HR");
    	departments2.setId(8);
    	
    	EmployeePayroll employeePayroll1 = new EmployeePayroll();
    	EmployeesNoPayroll employeesNoPayroll1 = new EmployeesNoPayroll(employees.getName(), employees.getEmployeeID(), employees.getStart_date(), employees.getGender(), employees.getPhoneNumber());
    	Payroll payroll1 = new Payroll(employees.getBasicPay(), employees.getDeductions(), employees.getTaxablePay(), employees.getIncomeTax(), employees.getNetPay());
    	employeePayroll1.setEmployee(employeesNoPayroll1);
    	employeePayroll1.setPayroll(payroll1);
    	employeePayroll1.getDepartments().add(departments);
    	
    	EmployeePayroll employeePayroll2 = new EmployeePayroll();
    	EmployeesNoPayroll employeesNoPayroll2 = new EmployeesNoPayroll(employees2.getName(), employees2.getEmployeeID(), employees2.getStart_date(), employees2.getGender(), employees2.getPhoneNumber());
    	Payroll payroll2 = new Payroll(employees2.getBasicPay(), employees2.getDeductions(), employees2.getTaxablePay(), employees2.getIncomeTax(), employees2.getNetPay());
    	employeePayroll2.setEmployee(employeesNoPayroll2);
    	employeePayroll2.setPayroll(payroll2);
    	employeePayroll2.getDepartments().add(departments2);
    	
    	List<EmployeePayroll> listEmployeePayrolls = new ArrayList<>();
    	listEmployeePayrolls.add(employeePayroll1);
    	listEmployeePayrolls.add(employeePayroll2);
		
    	List<EmployeesNoPayroll> employeesNoPayrolls = payDataService.getListFromDatabaseEmployeeNoPayroll(connection);
		int initial = employeesNoPayrolls.size();
    	
		payDataService.addMultipleEmployeePayrollWholeThreads(connection,listEmployeePayrolls);
    	
		for(EmployeePayroll employeePayroll : listEmployeePayrolls) {
			EmployeesNoPayroll employees3 = employeePayroll.getEmployee();
			Departments departments3 = employeePayroll.getDepartments().get(0);
			payDataService.deleteRecordEmployeePayrollAfterCascade(connection,employees3.getEmployeeID(),departments3.getId());
		}

		employeesNoPayrolls = payDataService.getListFromDatabaseEmployeeNoPayroll(connection);
		assertEquals(initial, employeesNoPayrolls.size());
    }
    
    @Test
    public void updateMultipleEmployeePayrollInDatabaseTransactionsThreads() throws JDBCException
    {
    	Date date = Date.valueOf("2020-11-01");
    	List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		payDataService.updateMultipleEmployeePayrollThreads(connection,list,date);

		List<EmployeesNoPayroll> employeesNoPayrolls = payDataService.getListFromDatabaseEmployeeNoPayroll(connection);
		for(EmployeesNoPayroll employeesNoPayroll : employeesNoPayrolls) {
			if( !list.contains( employeesNoPayroll.getEmployeeID() ) )
				continue;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
			java.util.Date date2 = null;
			try {
				date2 = simpleDateFormat.parse(employeesNoPayroll.getStart_date().toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			String stringDate = simpleDateFormat.format(date2);
			Date date3 = Date.valueOf(stringDate);
			assertEquals(date, date3);
		}
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

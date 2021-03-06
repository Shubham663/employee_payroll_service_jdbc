package com.bridgelabz.employee_payroll_service_jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bridgelabz.repos.Repos;

/**
 * Provides service for employee payroll database
 * 
 * @author Shubham
 */
public class PayrollDatabaseService {
	Logger logger = null;
	private PreparedStatement preparedStatement;
	private static PayrollDatabaseService pdService;
	int connectionCounter = 0;
	
	/**
	 * @return the preparedStatement
	 */
	public PreparedStatement getPreparedStatement() {
		return preparedStatement;
	}

	private PayrollDatabaseService() {
		logger = LogManager.getLogger();
	}

	public static PayrollDatabaseService getInstance() {
		if (pdService == null)
			pdService = new PayrollDatabaseService();
		return pdService;
	}

	public void loadDriver() throws JDBCException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			logger.info("Driver Loaded");
		} catch (ClassNotFoundException e1) {
			throw new JDBCException("Error while loading Driver " + e1.getMessage());
		}
	}

	public void listOfDrivers() {
		Enumeration<java.sql.Driver> driverList = DriverManager.getDrivers();
		while (driverList.hasMoreElements()) {
			Driver driverClass = driverList.nextElement();
			System.out.println("	" + driverClass.getClass().getName());
		}
	}

	public Connection connectToDatabase(Connection connection) throws JDBCException {
		try {
			connectionCounter++;
			logger.info("Connecting to database with connection no " + connectionCounter);
			connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/employee_payroll_service?useSSL=false", "root",
					Repos.returnPassword());
			logger.info("Connection Established " + connection + " for the thread " + Thread.currentThread().getName() + " running with connection no " + connectionCounter);
		} catch (SQLException e) {
			throw new JDBCException("Error while connecting to " + connection + e.getMessage());
		}
		return connection;
	}

	public List<Employees> getListFromDatabase(Connection connection) throws JDBCException {
		Statement stmt = null;
		ResultSet result = null;
		List<Employees> listEmployees = null;
		try {
			stmt = connection.createStatement();
			result = stmt.executeQuery("select * from employees");
			listEmployees = new ArrayList<>();
			while (result.next()) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
				Employees employee = new Employees();
				employee.setEmployeeID(result.getInt(1));
				employee.setName(result.getString(2));
				employee.setSalary(result.getDouble(3));
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
			logger.info("List successfully retrieved from database");
		} catch (SQLException exception) {
			throw new JDBCException("Error while retrieving data");
		} finally {
			try {
				if (result != null)
					result.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				throw new JDBCException(
						"Error while closing resources when retrieving data" + connection + e.getMessage());
			}
		}
		return listEmployees;
	}

	public int updateDetails(Connection connection) throws JDBCException {
		Statement stmt = null;
		int val = 0;
		try {
			stmt = connection.createStatement();
			val = stmt.executeUpdate("Update employees set salary = 3000000 where name = \"Terisa\"");
			return val;
		} catch (SQLException exception) {
			throw new JDBCException("Error while updating salary");
		} finally {
			try {
				stmt.close();
			} catch (SQLException exception) {
				throw new JDBCException(
						"Error while closing resources when updating database " + connection + exception.getMessage());
			}
		}
	}

	public void updateDetailsPrepared(Connection connection,double val) throws JDBCException {
		try {
			preparedStatement = connection.prepareStatement("Update employees set salary = ? where name = ?");
			preparedStatement.setDouble(1, val);
			preparedStatement.setString(2, "Terisa");
			preparedStatement.execute();
		} catch (SQLException exception) {
			throw new JDBCException("Error while updating with prepared Statement ");
		}
	}

	public List<Employees> getDateRange(Connection connection, Date date1, Date date2) throws JDBCException {

		List<Employees> listEmployees = null;
		try {
			preparedStatement = connection.prepareStatement(
					"Select * from employees where start_date between cast(? as date) and cast(? as date)");
			preparedStatement.setDate(1, date1);
			preparedStatement.setDate(2, date2);
			ResultSet result = preparedStatement.executeQuery();
			listEmployees = getListFromResult(result);
		} catch (SQLException exception) {
			throw new JDBCException("Error while retrieving recods in partcular range with prepared Statement ");
		}
		return listEmployees;
	}

	private List<Employees> getListFromResult(ResultSet result) throws JDBCException {
		List<Employees> listEmployees = new ArrayList<>();
		try {
			while (result.next()) {
				Employees employee = new Employees();
				employee.setEmployeeID(result.getInt(1));
				employee.setName(result.getString(2));
				employee.setSalary(result.getDouble(3));
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
			logger.info("List successfully retrieved from database");
		} catch (SQLException exception) {
			throw new JDBCException("Error while retrieving data");
		}
		return listEmployees;
	}

	public int groupFunctionCount(Connection connection) throws JDBCException {
		int count = 0;
		try {
			preparedStatement = connection.prepareStatement("select count(gender) from employees where gender = \"Male\"");
			ResultSet result = preparedStatement.executeQuery();
			while(result.next()) {
				count = result.getInt(1);
				break;
			}
		} catch (SQLException exception) {
			throw new JDBCException("Error while getting count using group statements with prepared Statement ");
		}
		return count;
	}

	public double groupFunctionSum(Connection connection) throws JDBCException {
		double salarySum = 0;
		try {
			preparedStatement = connection.prepareStatement("select sum(salary) from employees where gender = \"Male\"");
			ResultSet result = preparedStatement.executeQuery();
			while(result.next()) {
				salarySum = result.getInt(1);
				break;
			}
		} catch (SQLException exception) {
			throw new JDBCException("Error while getting sum using group statements with prepared Statement ");
		}
		return salarySum;
	}

	public double groupFunctionMin(Connection connection) throws JDBCException {
		double minSalary = 0;
		try {
			preparedStatement = connection.prepareStatement("select min(salary) from employees where gender = \"Male\"");
			ResultSet result = preparedStatement.executeQuery();
			while(result.next()) {
				minSalary = result.getInt(1);
				break;
			}
		} catch (SQLException exception) {
			throw new JDBCException("Error while getting min using group statements with prepared Statement ");
		}
		return minSalary;
	}

	public double groupFunctionMax(Connection connection) throws JDBCException {
		double maxSalary = 0;
		try {
			preparedStatement = connection.prepareStatement("select max(salary) from employees where gender = \"Male\"");
			ResultSet result = preparedStatement.executeQuery();
			while(result.next()) {
				maxSalary = result.getInt(1);
				break;
			}
		} catch (SQLException exception) {
			throw new JDBCException("Error while getting max using group statements with prepared Statement ");
		}
		return maxSalary;
	}

	public double groupFunctionAvg(Connection connection) throws JDBCException {
		double avgSalary = 0;
		try {
			preparedStatement = connection.prepareStatement("select avg(salary) from employees where gender = \"Female\"");
			ResultSet result = preparedStatement.executeQuery();
			while(result.next()) {
				avgSalary = result.getInt(1);
				break;
			}
		} catch (SQLException exception) {
			throw new JDBCException("Error while getting avg using group statements with prepared Statement ");
		}
		return avgSalary;
	}

	public synchronized void addEmployee(Connection connection, Employees employees) throws JDBCException {
		Connection connection2 = null;
		try {
			 connection2 = connectToDatabase(connection2);
		} catch (JDBCException e1) {
			logger.error("Error while getting another connection");
		}
		try {
			preparedStatement = connection2.prepareStatement("insert into employees values(?,?,?,?,?,?,?,?,?,?,?)");
			preparedStatement.setInt(1, employees.getEmployeeID());
			preparedStatement.setString(2, employees.getName());
			preparedStatement.setDouble(3, employees.getSalary());
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
			java.util.Date date0 = null;
			try {
				date0 = simpleDateFormat.parse(employees.getStart_date().toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			String stringDate = simpleDateFormat.format(date0);
			Date date = Date.valueOf(stringDate);
			preparedStatement.setDate(4, date);
			preparedStatement.setString(5, employees.getGender());
			preparedStatement.setDouble(6, employees.getBasicPay());
			preparedStatement.setDouble(7, employees.getDeductions());
			preparedStatement.setDouble(8, employees.getTaxablePay());
			preparedStatement.setDouble(9, employees.getIncomeTax());
			preparedStatement.setDouble(10, employees.getNetPay());
			preparedStatement.setLong(11, employees.getPhoneNumber());
			preparedStatement.execute();
			connection2.close();
		} catch (SQLException exception) {
			throw new JDBCException("Error while adding employee to database with prepared Statement " + exception.getMessage());
		}
	}

	public void deleteRecord(Connection connection, int size) throws JDBCException {
		try {
			preparedStatement = connection.prepareStatement("delete from employees where employee_id = ? ");
			preparedStatement.setInt(1, size);
			preparedStatement.execute();
		} catch (SQLException exception) {
			throw new JDBCException("Error while deleting record from database with prepared Statement " + exception.getMessage());
		}
	}

	public void addEmployeePayroll(Connection connection, Employees employees) throws JDBCException {
		ResultSet result = null;
		try {
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement("select name from employees_no_payroll where employee_id = ?");
			preparedStatement.setInt(1, employees.getEmployeeID());
			result = preparedStatement.executeQuery();
			boolean isPresent = result.next();
			if(!isPresent) {
				preparedStatement = connection.prepareStatement("insert into employees_no_payroll values(?,?,?,?,?,?)");
				preparedStatement.setInt(1, employees.getEmployeeID());
				preparedStatement.setString(2, employees.getName());
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
				java.util.Date date0 = null;
				try {
					date0 = simpleDateFormat.parse(employees.getStart_date().toString());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				String stringDate = simpleDateFormat.format(date0);
				Date date = Date.valueOf(stringDate);
				preparedStatement.setDate(3, date);
				preparedStatement.setString(4, employees.getGender());
				preparedStatement.setLong(5, employees.getPhoneNumber());
				preparedStatement.setInt(6, 1);
			}
			else {
				logger.info("The details are already present inside addressbook. If were deactivated, once again active");
				preparedStatement = connection.prepareStatement("update employees_no_payroll set is_active = 1 where employee_id = ?");
				preparedStatement.setInt(1, employees.getEmployeeID());
			}
			preparedStatement.execute();
			logger.info("Successfully added data to table employees no payroll");
			try {
				preparedStatement = connection.prepareStatement("insert into payroll values(?,?,?,?,?)");
				preparedStatement.setDouble(1, employees.getBasicPay());
				preparedStatement.setDouble(2, employees.getDeductions());
				preparedStatement.setDouble(3, employees.getTaxablePay());
				preparedStatement.setDouble(4, employees.getIncomeTax());
				preparedStatement.setDouble(5, employees.getNetPay());
				preparedStatement.execute();
				logger.info("Successfully added data to table payroll");
			}
			catch(SQLException exception) {
				logger.info("Data already present inside the table " + exception.getMessage());
			}
			preparedStatement = connection.prepareStatement("insert into employee_payroll_map values(?,?)");
			preparedStatement.setDouble(1, employees.getBasicPay());
			preparedStatement.setInt(2, employees.getEmployeeID());
			preparedStatement.execute();
			logger.info("Successfully added data to table employees payroll map");
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException exception) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				throw new JDBCException("Error when trying to perform rollback on connection " + e.getMessage());
			}
			throw new JDBCException("Error while inserting data into multiple tables with prepared Statement " + exception.getMessage());
		}
	}

	public List<Payroll> getListFromDatabasePayroll(Connection connection) throws JDBCException {
		ResultSet result = null;
		List<Payroll> listPayrolls = null;
		try {
			preparedStatement = connection.prepareStatement("select * from payroll");
			result = preparedStatement.executeQuery();
			listPayrolls = new ArrayList<>();
			while (result.next()) {
				Payroll payroll = new Payroll();
				payroll.setBasicPay(result.getDouble(1));
				payroll.setDeductions(result.getDouble(2));
				payroll.setTaxablePay(result.getDouble(3));
				payroll.setIncomeTax(result.getDouble(4));
				payroll.setNetPay(result.getDouble(5));
				listPayrolls.add(payroll);
			}
			logger.info("List successfully retrieved from database");
		} catch (SQLException exception) {
			throw new JDBCException("Error while retrieving data");
		} finally {
			try {
				if (result != null)
					result.close();
			} catch (SQLException e) {
				throw new JDBCException(
						"Error while closing resources when retrieving data" + connection + e.getMessage());
			}
		}
		return listPayrolls;
	}

	public List<EmployeesNoPayroll> getListFromDatabaseEmployeeNoPayroll(Connection connection) throws JDBCException {
		ResultSet result = null;
		List<EmployeesNoPayroll> listEmployees = null;
		try {
			preparedStatement = connection.prepareStatement("select * from employees_no_payroll");
			result = preparedStatement.executeQuery();			
			listEmployees = new ArrayList<>();
			while (result.next()) {
				EmployeesNoPayroll employee = new EmployeesNoPayroll();
				employee.setEmployeeID(result.getInt(1));
				employee.setName(result.getString(2));
				employee.setStart_date(result.getDate(3));
				employee.setGender(result.getString(4));
				employee.setPhoneNumber(result.getLong(5));
				listEmployees.add(employee);
			}
			logger.info("List successfully retrieved from database");
		} catch (SQLException exception) {
			throw new JDBCException("Error while retrieving data");
		} finally {
			try {
				if (result != null)
					result.close();
			} catch (SQLException e) {
				throw new JDBCException(
						"Error while closing resources when retrieving data" + connection + e.getMessage());
			}
		}
		return listEmployees;
	}

	public void deleteRecordPayroll(Connection connection, int employeeID) throws JDBCException {
		try {
			preparedStatement = connection.prepareStatement("delete from employee_payroll_map where employee_id = ?");
			preparedStatement.setInt(1, employeeID);
			preparedStatement.execute();
			preparedStatement = connection.prepareStatement("delete from employees_no_payroll where employee_id = ?");
			preparedStatement.setInt(1, employeeID);
			preparedStatement.execute();
		} catch (SQLException exception) {
			throw new JDBCException("Error while running group statements with prepared Statement " + exception.getMessage());
		}
	}

	public void deleteRecordPayrollAfterCascade(Connection connection, int employeeID) throws JDBCException {
		try {
			preparedStatement = connection.prepareStatement("delete from employees_no_payroll where employee_id = ?");
			preparedStatement.setInt(1, employeeID);
			preparedStatement.execute();
		} catch (SQLException exception) {
			throw new JDBCException("Error while running group statements with prepared Statement " + exception.getMessage());
		}
	}

	public synchronized void addEmployeePayrollWhole(Connection connection, EmployeePayroll employeePayroll,Employees employees) throws JDBCException {
		Connection connection2 = null;
		try {
			 connection2 = connectToDatabase(connection2);
		} catch (JDBCException e1) {
			logger.error("Error while getting another connection");
		}
		this.addEmployeePayroll(connection2, employees);
		
		try {
			connection2.setAutoCommit(false);
			for(Departments departments : employeePayroll.getDepartments()) {
				preparedStatement = connection2.prepareStatement("insert into department values(?,?,?)");
				preparedStatement.setString(1, departments.getDepartmentName());
				preparedStatement.setString(2, departments.getAddress());
				preparedStatement.setInt(3, departments.getId());
				preparedStatement.execute();
				logger.info("Successfully added data to table department");
				preparedStatement = connection2.prepareStatement("insert into empid_departmentid values(?,?)");
				preparedStatement.setInt(1, employees.getEmployeeID());
				preparedStatement.setInt(2, departments.getId());
				preparedStatement.execute();
				logger.info("Successfully added data to table empid_departmentid");
				connection2.commit();
			}
			connection2.setAutoCommit(true);
		} catch (SQLException exception) {
			try {
				connection2.rollback();
			} catch (SQLException e) {
				throw new JDBCException("Error when trying to perform rollback on connection " + e.getMessage());
			}
			throw new JDBCException("Error while inserting data into multiple tables with prepared Statement " + exception.getMessage());
		}
	}

	public void deleteRecordEmployeePayrollAfterCascade(Connection connection, int employeeID, int departmentID) throws JDBCException {
		try {
			preparedStatement = connection.prepareStatement("delete from employees_no_payroll where employee_id = ?");
			preparedStatement.setInt(1, employeeID);
			preparedStatement.execute();
			preparedStatement = connection.prepareStatement("delete from department where id = ?");
			preparedStatement.setInt(1, departmentID);
			preparedStatement.execute();
			logger.info("Successfull deletion from employee_no_payroll, employee_payroll_map, department, empid_departmentid tables");
		} catch (SQLException exception) {
			throw new JDBCException("Error while deleting record " + exception.getMessage());
		}
	}

	public void removeEmployeePayroll(Connection connection, int employeeID, int departmentID) throws JDBCException {
		try {
			preparedStatement = connection.prepareStatement("update employees_no_payroll set is_active = false where employee_id = ?");
			preparedStatement.setInt(1, employeeID);
			preparedStatement.execute();
			preparedStatement = connection.prepareStatement("delete from department where id = ?");
			preparedStatement.setInt(1, departmentID);
			preparedStatement.execute();
			logger.info("Successfully set employee_no_payroll is_active to false");
//			preparedStatement.close();
		} catch (SQLException exception) {
			throw new JDBCException("Error while running removing data with prepared Statement ");
		}
	}

	public List<EmployeesNoPayroll> getListFromDatabaseIsActive(Connection connection) throws JDBCException {
		ResultSet result = null;
		List<EmployeesNoPayroll> listEmployees = null;
		try {
			preparedStatement = connection.prepareStatement("select * from employees_no_payroll where is_active = true");
			result = preparedStatement.executeQuery();			
			listEmployees = new ArrayList<>();
			while (result.next()) {
				EmployeesNoPayroll employee = new EmployeesNoPayroll();
				employee.setEmployeeID(result.getInt(1));
				employee.setName(result.getString(2));
				employee.setStart_date(result.getDate(3));
				employee.setGender(result.getString(4));
				employee.setPhoneNumber(result.getLong(5));
				listEmployees.add(employee);
			}
			logger.info("List successfully retrieved from database");
		} catch (SQLException exception) {
			throw new JDBCException("Error while retrieving data");
		} finally {
			try {
				if (result != null)
					result.close();
			} catch (SQLException e) {
				throw new JDBCException(
						"Error while closing resources when retrieving data" + connection + e.getMessage());
			}
		}
		return listEmployees;
	}

	public void addMultipleEmployees(Connection connection, List<Employees> listEmployees2) throws JDBCException {
		List<Employees> listEmployees = null;
		try {
			for(Employees employees : listEmployees2) {
				addEmployee(connection, employees);
			}
		} catch (JDBCException exception) {
			throw new JDBCException("Error while adding multiple elements to database " + exception.getMessage());
		}
	}
	

	public void addMultipleEmployeesThreads(Connection connection, List<Employees> listEmployees2) {
		Map<Integer, Boolean> employeeAddStatus = new HashMap<>();
		listEmployees2.forEach(employee ->{
			Runnable task = () -> {
					
				logger.info("Employee being added " + Thread.currentThread().getName());
				employeeAddStatus.put(employee.hashCode(), false);
				try {
					addEmployee(connection, employee);
				} catch (JDBCException e) {
					logger.error("Error when adding employee " + e.getMessage());
				}
				logger.info("Employee added " + Thread.currentThread().getName());
				employeeAddStatus.put(employee.hashCode(), true);
			};
			Thread thread = new Thread(task,employee.getName());
			thread.start();
		});
			while(employeeAddStatus.size() < listEmployees2.size() || employeeAddStatus.containsValue(false)) {
				try{
					Thread.sleep(10);
				}catch(InterruptedException exception) {
					logger.error("Error while waiting for ");
				}
			}
	}

	public void addMultipleEmployeePayrollWholeThreads(Connection connection, List<EmployeePayroll> listEmployeePayrolls) {
		Map<Integer, Boolean> employeeAddStatus = new HashMap<>();
		listEmployeePayrolls.forEach(employeePayroll ->{
			Runnable task = () -> {
					
				logger.info("Employee Payroll being added " + Thread.currentThread().getName());
				employeeAddStatus.put(employeePayroll.hashCode(), false);
				try {
					Employees employees = new Employees(employeePayroll.getEmployee().getName(), employeePayroll.getEmployee().getEmployeeID(), employeePayroll.getPayroll().getBasicPay(), employeePayroll.getEmployee().getStart_date(), employeePayroll.getEmployee().getGender(), employeePayroll.getPayroll().getBasicPay(), employeePayroll.getPayroll().getDeductions(), employeePayroll.getPayroll().getTaxablePay(), employeePayroll.getPayroll().getIncomeTax(), employeePayroll.getPayroll().getNetPay(), employeePayroll.getEmployee().getPhoneNumber());
					addEmployeePayrollWhole(connection, employeePayroll, employees);
				} catch (JDBCException e) {
					logger.error("Error when adding employee payroll " + e.getMessage());
				}
				logger.info("Employee Payroll added " + Thread.currentThread().getName());
				employeeAddStatus.put(employeePayroll.hashCode(), true);
			};
			Thread thread = new Thread(task,employeePayroll.getEmployee().getName());
			thread.start();
		});
			while(employeeAddStatus.size() < listEmployeePayrolls.size() || employeeAddStatus.containsValue(false)) {
				try{
					Thread.sleep(10);
				}catch(InterruptedException exception) {
					logger.error("Error while waiting for ");
				}
			}
	}

	public void updateMultipleEmployeePayrollThreads(Connection connection, List<Integer> list, Date date) {
		Map<Integer, Boolean> employeeAddStatus = new HashMap<>();	
		list.forEach(id -> {
			Runnable task = () -> {
				try {
					Connection connection2 = null;
					try {
						 connection2 = connectToDatabase(connection2);
					} catch (JDBCException e1) {
						logger.error("Error while getting another connection");
					}
					employeeAddStatus.put(id, false);
					preparedStatement = connection2.prepareStatement("Update employees_no_payroll set start_date = ? where employee_id = ?");
					preparedStatement.setDate(1, date);
					preparedStatement.setInt(2, id);
					preparedStatement.execute();
					employeeAddStatus.put(id, true);
				} catch (SQLException exception) {
					logger.error("Error while updating employee payroll in tables");
				}
			};
			Thread thread = new Thread(task,"thread id: " + id.toString());
			thread.start();
		});
		while(employeeAddStatus.size() < list.size() || employeeAddStatus.containsValue(false)) {
			try{
				Thread.sleep(10);
			}catch(InterruptedException exception) {
				logger.error("Error while waiting for threads to finish");
			}
		}
	}
}

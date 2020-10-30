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
import java.util.List;

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
			logger.info("Connecting to database");
			connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/employee_payroll_service?useSSL=false", "root",
					Repos.returnPassword());
			logger.info("Connection Established " + connection);
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

	public void updateDetails(Connection connection) throws JDBCException {
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			stmt.execute("Update employees set salary = 3000000 where name = \"Terisa\"");
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

	public void updateDetailsPrepared(Connection connection) throws JDBCException {
		try {
			preparedStatement = connection.prepareStatement("Update employees set salary = ? where name = ?");
			preparedStatement.setDouble(1, 4000000);
			preparedStatement.setString(2, "Terisa");
			preparedStatement.execute();
			preparedStatement.close();
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
			listEmployees = new ArrayList<>();
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
			preparedStatement.close();
		} catch (SQLException exception) {
			throw new JDBCException("Error while getting recods with prepared Statement ");
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
			preparedStatement.close();
		} catch (SQLException exception) {
			throw new JDBCException("Error while running group statements with prepared Statement ");
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
			preparedStatement.close();
		} catch (SQLException exception) {
			throw new JDBCException("Error while running group statements with prepared Statement ");
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
			preparedStatement.close();
		} catch (SQLException exception) {
			throw new JDBCException("Error while running group statements with prepared Statement ");
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
			preparedStatement.close();
		} catch (SQLException exception) {
			throw new JDBCException("Error while running group statements with prepared Statement ");
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
			preparedStatement.close();
		} catch (SQLException exception) {
			throw new JDBCException("Error while running group statements with prepared Statement ");
		}
		return avgSalary;
	}

	public void addEmployee(Connection connection, Employees employees) throws JDBCException {
		List<Employees> listEmployees = null;
		try {
			preparedStatement = connection.prepareStatement("insert into employees values(?,?,?,?,?,?,?,?,?,?,?)");
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
			preparedStatement.close();
		} catch (SQLException exception) {
			throw new JDBCException("Error while running group statements with prepared Statement " + exception.getMessage());
		}
	}

	public void deleteRecord(Connection connection, int size) throws JDBCException {
		try {
			preparedStatement = connection.prepareStatement("delete from employees where employee_id = ?");
			preparedStatement.setInt(1, size);
			preparedStatement.execute();
			preparedStatement.close();
		} catch (SQLException exception) {
			throw new JDBCException("Error while running group statements with prepared Statement " + exception.getMessage());
		}
	}

	public void addEmployeePayroll(Connection connection, Employees employees) throws JDBCException {
		try {
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement("insert into employees_no_payroll values(?,?,?,?,?)");
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
			preparedStatement.close();
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
			preparedStatement.close();
		} catch (SQLException exception) {
			throw new JDBCException("Error while running group statements with prepared Statement " + exception.getMessage());
		}
	}

	public void deleteRecordPayrollAfterCascade(Connection connection, int employeeID) throws JDBCException {
		try {
//			preparedStatement = connection.prepareStatement("delete from employee_payroll_map where employee_id = ?");
//			preparedStatement.setInt(1, employeeID);
//			preparedStatement.execute();
			preparedStatement = connection.prepareStatement("delete from employees_no_payroll where employee_id = ?");
			preparedStatement.setInt(1, employeeID);
			preparedStatement.execute();
			preparedStatement.close();
		} catch (SQLException exception) {
			throw new JDBCException("Error while running group statements with prepared Statement " + exception.getMessage());
		}
	}
}

package com.bridgelabz.employee_payroll_service_jdbc;

import java.util.Date;

/**
 * Employees class with no payroll information
 * @author Shubham
 *
 */
public class EmployeesNoPayroll {
	private String name;
	private int employeeID;
	private Date start_date;
	private String gender;
	private long phoneNumber;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the employeeID
	 */
	public int getEmployeeID() {
		return employeeID;
	}

	/**
	 * @param employeeID the employeeID to set
	 */
	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}


	/**
	 * @return the start_date
	 */
	public Date getStart_date() {
		return start_date;
	}

	/**
	 * @param start_date the start_date to set
	 */
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the phoneNumber
	 */
	public long getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	
	
	@Override
	public String toString() {
		return "EmployeesNoPayroll [name=" + name + ", employeeID=" + employeeID + ", start_date=" + start_date
				+ ", gender=" + gender + ", phoneNumber=" + phoneNumber + "]\n";
	}

	@Override
	public boolean equals(Object obj) {
		EmployeesNoPayroll employees = (EmployeesNoPayroll)obj;
		if(this.getEmployeeID() != employees.getEmployeeID())
			return false;
		if(this.getName().compareTo(employees.getName()) != 0)
			return false;
		if(this.getStart_date().compareTo(employees.getStart_date()) != 0)
			return false;
		if(this.getGender().compareTo(employees.getGender()) != 0)
			return false;
		if(this.getPhoneNumber() != employees.getPhoneNumber() )
			return false;
		return true;
	}

	/**
	 * @param name
	 * @param employeeID
	 * @param start_date
	 * @param gender
	 * @param phoneNumber
	 */
	public EmployeesNoPayroll(String name, int employeeID, Date start_date, String gender, long phoneNumber) {
		super();
		this.name = name;
		this.employeeID = employeeID;
		this.start_date = start_date;
		this.gender = gender;
		this.phoneNumber = phoneNumber;
	}

	public EmployeesNoPayroll() {
	}
	
	
}

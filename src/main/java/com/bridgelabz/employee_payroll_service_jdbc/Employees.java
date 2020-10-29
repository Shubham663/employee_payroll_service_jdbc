package com.bridgelabz.employee_payroll_service_jdbc;

import java.util.Date;

public class Employees {
	private String name;
	private int employeeID;
	private double salary;
	private Date start_date;
	private String gender;
	private double basicPay;
	private double deductions;
	private double taxablePay;
	private double incomeTax;
	private double netPay;
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
	 * @return the salary
	 */
	public double getSalary() {
		return salary;
	}

	/**
	 * @param salary the salary to set
	 */
	public void setSalary(double salary) {
		this.salary = salary;
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
	 * @return the basicPay
	 */
	public double getBasicPay() {
		return basicPay;
	}

	/**
	 * @param basicPay the basicPay to set
	 */
	public void setBasicPay(double basicPay) {
		this.basicPay = basicPay;
	}

	/**
	 * @return the deductions
	 */
	public double getDeductions() {
		return deductions;
	}

	/**
	 * @param deductions the deductions to set
	 */
	public void setDeductions(double deductions) {
		this.deductions = deductions;
	}

	/**
	 * @return the taxablePay
	 */
	public double getTaxablePay() {
		return taxablePay;
	}

	/**
	 * @param taxablePay the taxablePay to set
	 */
	public void setTaxablePay(double taxablePay) {
		this.taxablePay = taxablePay;
	}

	/**
	 * @return the incomeTax
	 */
	public double getIncomeTax() {
		return incomeTax;
	}

	/**
	 * @param incomeTax the incomeTax to set
	 */
	public void setIncomeTax(double incomeTax) {
		this.incomeTax = incomeTax;
	}

	/**
	 * @return the netPay
	 */
	public double getNetPay() {
		return netPay;
	}

	/**
	 * @param netPay the netPay to set
	 */
	public void setNetPay(double netPay) {
		this.netPay = netPay;
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
		return "name=" + name + ", employeeID=" + employeeID + ", salary=" + salary + ", start_date="
				+ start_date + ", gender=" + gender + ", basicPay=" + basicPay + ", deductions=" + deductions
				+ ", taxablePay=" + taxablePay + ", incomeTax=" + incomeTax + ", netPay=" + netPay + ", phoneNumber="
				+ phoneNumber + "\n";
	}
}

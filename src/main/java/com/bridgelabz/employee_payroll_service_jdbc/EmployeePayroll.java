package com.bridgelabz.employee_payroll_service_jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Shubham
 *
 */
public class EmployeePayroll {
	private Payroll payroll;
	private EmployeesNoPayroll employee;
	private List<Departments> departments;
	
	@Override
		public int hashCode() {
			return Objects.hash(payroll,employee,departments);
		}
	
	public EmployeePayroll() {
		departments = new ArrayList<>();
	}
	/**
	 * @return the payroll
	 */
	public Payroll getPayroll() {
		return payroll;
	}
	/**
	 * @param payroll the payroll to set
	 */
	public void setPayroll(Payroll payroll) {
		this.payroll = payroll;
	}
	/**
	 * @return the employee
	 */
	public EmployeesNoPayroll getEmployee() {
		return employee;
	}
	/**
	 * @param employee the employee to set
	 */
	public void setEmployee(EmployeesNoPayroll employee) {
		this.employee = employee;
	}
	/**
	 * @return the departments
	 */
	public List<Departments> getDepartments() {
		return departments;
	}
	/**
	 * @param departments the departments to set
	 */
	public void setDepartments(List<Departments> departments) {
		this.departments = departments;
	}
	
}

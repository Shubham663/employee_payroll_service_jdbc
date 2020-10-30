package com.bridgelabz.employee_payroll_service_jdbc;

/**
 * Class storing payroll details for employees
 * 
 * @author Shubham
 *
 */
public class Payroll {
	private double basicPay;
	private double deductions;
	private double taxablePay;
	private double incomeTax;
	private double netPay;

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

	@Override
	public String toString() {
		return "Payroll [basicPay=" + basicPay + ", deductions=" + deductions + ", taxablePay=" + taxablePay
				+ ", incomeTax=" + incomeTax + ", netPay=" + netPay + "]\n";
	}

	public Payroll(double basicPay, double deductions, double taxablePay, double incomeTax, double netPay) {
		this.basicPay = basicPay;
		this.deductions = deductions;
		this.taxablePay = taxablePay;
		this.incomeTax = incomeTax;
		this.netPay = netPay;
	}

	public Payroll() {
	}

	@Override
	public boolean equals(Object obj) {
		Payroll employees = (Payroll) obj;
		if (this.getBasicPay() != employees.getBasicPay())
			return false;
		if (this.getDeductions() != employees.getDeductions())
			return false;
		if (this.getTaxablePay() != employees.getTaxablePay())
			return false;
		if (this.getIncomeTax() != employees.getIncomeTax())
			return false;
		if (this.getNetPay() != employees.getNetPay())
			return false;
		return true;
	}
}

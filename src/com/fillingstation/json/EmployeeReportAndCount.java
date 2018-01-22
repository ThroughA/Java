package com.fillingstation.json;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class EmployeeReportAndCount {

	private ArrayList<EmployeeReportJSON> employeeRetrievelist ;
	private ArrayList<EmployeeReportJSON> employeeCountRetrievelist;
	private ArrayList<EmployeeConfigJSON> employeeDepartmentlist;
	private ArrayList<EmployeeConfigJSON> employeeRolelist;
	private ArrayList<EmployeeConfigJSON> employeePermisionlist;
	private ArrayList<EmployeeConfigJSON> employeeList;
	private ArrayList<EmployeeConfigJSON> lockList;
	
	private String Role;
	
	private String userName;
	private String employeeId;
	private String companyId;
	private String companyName;
	
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public ArrayList<EmployeeConfigJSON> getEmployeePermisionlist() {
		return employeePermisionlist;
	}
	public void setEmployeePermisionlist(ArrayList<EmployeeConfigJSON> employeePermisionlist) {
		this.employeePermisionlist = employeePermisionlist;
	}
	
	public ArrayList<EmployeeReportJSON> getEmployeeRetrievelist() {
		return employeeRetrievelist;
	}
	public void setEmployeeRetrievelist(ArrayList<EmployeeReportJSON> employeeRetrievelist) {
		this.employeeRetrievelist = employeeRetrievelist;
	}
	public ArrayList<EmployeeReportJSON> getEmployeeCountRetrievelist() {
		return employeeCountRetrievelist;
	}
	public void setEmployeeCountRetrievelist(ArrayList<EmployeeReportJSON> employeeCountRetrievelist) {
		this.employeeCountRetrievelist = employeeCountRetrievelist;
	}
	public ArrayList<EmployeeConfigJSON> getEmployeeDepartmentlist() {
		return employeeDepartmentlist;
	}
	public void setEmployeeDepartmentlist(ArrayList<EmployeeConfigJSON> employeeDepartmentlist) {
		this.employeeDepartmentlist = employeeDepartmentlist;
	}
	public ArrayList<EmployeeConfigJSON> getEmployeeRolelist() {
		return employeeRolelist;
	}
	public void setEmployeeRolelist(ArrayList<EmployeeConfigJSON> employeeRolelist) {
		this.employeeRolelist = employeeRolelist;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public ArrayList<EmployeeConfigJSON> getEmployeeList() {
		return employeeList;
	}
	public void setEmployeeList(ArrayList<EmployeeConfigJSON> employeeList) {
		this.employeeList = employeeList;
	}
	public String getRole() {
		return Role;
	}
	public void setRole(String role) {
		Role = role;
	}
	
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public ArrayList<EmployeeConfigJSON> getLockList() {
		return lockList;
	}
	public void setLockList(ArrayList<EmployeeConfigJSON> lockList) {
		this.lockList = lockList;
	}
	
	
	}

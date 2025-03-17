package com.example.employeemgmt.employees;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.employeemgmt.employee.Employee;

/**
 * A container class for a list of Employees with a constructor,
 * getter and setter method.
 */
@Component
public class Employees {

    private List<Employee> employeeList = new ArrayList<>();

    public Employees(List<Employee> employeeList2) {
        employeeList = employeeList2;
    }

    public List<Employee>  getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

 }

package com.example.employeemgmt.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.example.employeemgmt.employee.Employee;
import com.example.employeemgmt.employees.Employees;

/** A DAO class that facilitates data handling.*/
@Repository
public class EmployeeManager {
    
    /**
     * A sample list of employees are created for initialization.
     */
    private final Employees employees;
    @Autowired
    public EmployeeManager(Employees employees) {
        this.employees = employees;
        if(employees.getEmployeeList().isEmpty()) {
            employees.getEmployeeList().add(new Employee(1, "Min", "Rahm", "mrahm1@gmail.com", "Developer"));
            employees.getEmployeeList().add(new Employee(2, "Suga", "Sally", "ssalt1@gmail.com", "Project Manager"));
            employees.getEmployeeList().add(new Employee(3, "Summer", "Winnie", "summawin@gmail.com", "Risk Analyst"));
        }
    }

    // DAO method to return all employees.
    public Employees getAllEmployees() {
        return employees;
    }

    // DAO method to add a new employee.
    public Employee addEmployee(Employee employeeToAdd) {
        employees.getEmployeeList().add(employeeToAdd);
        return employeeToAdd;
    }

    // DAO method to delete an employee.
    public void deleteEmployee(Integer id) {
        Employee employeeToDelete = findById(id);
        employees.getEmployeeList().remove(employeeToDelete);
    }

    // DAO method to update an employee and return the employee object with updated details.
    public Employee updateEmployee(Employee employeeToUpdate) {
        Employee updatedEmployee = findById(employeeToUpdate.getId());
        if(updatedEmployee == null) {
            return null;
        }
        updatedEmployee.setFirstName(employeeToUpdate.getFirstName());
        updatedEmployee.setLastName(employeeToUpdate.getLastName());
        updatedEmployee.setEmail(employeeToUpdate.getEmail());
        updatedEmployee.setTitle(employeeToUpdate.getTitle());
        return updatedEmployee;
    }

    // DAO method to find an employee by id.
    public Employee findById(Integer id) {
        for( Employee emp : employees.getEmployeeList()) {
            if(emp.getId() == id) {
                return emp;
            }
        } return null;

    }

    // DAO method to validate wheather an employee exists.
    public Boolean ifExists(Employee employee) {
        for(Employee emp : employees.getEmployeeList()) {
            if(emp.getId() == employee.getId()) {
                return true;
            }
        } return false;
    }

    // Overloaded DAO method to validate wheather an employee exists by id.
    public Boolean ifExists(Integer id) {
        for( Employee emp : employees.getEmployeeList()) {
            if(emp.getId() == id) {
                return true;
            }
        } return false;

    }
}

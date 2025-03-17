package com.example.employeemgmt.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.employeemgmt.DAO.EmployeeManager;
import com.example.employeemgmt.employee.Employee;
import com.example.employeemgmt.employees.Employees;
import com.example.employeemgmt.exception.IdExistsException;
import com.example.employeemgmt.exception.IdNotFoundException;
import com.example.employeemgmt.exception.InvalidInputException;


/** Service class added for business logic. */
@Service
public class EmployeeService {
    
    private final EmployeeManager employeeManager;

    /**
     * Constructor that injects the necessary EmployeeManager class for data access. 
     * @param employeeManager
     */
    @Autowired 
    public EmployeeService(EmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }

    /**
     * A service layer method to get a list of all employees.
     * @return A list of all employees.
     */
    public Employees getAllEmployees() {
        return employeeManager.getAllEmployees();
    }

    /**
     * Service layer method to add an employee.
     * @param employee
     * @return The employee object if create operation is successful.
     * @throws InvalidInputException If fields are empty.
     * @throws IdExistsException If id already exists as it would break database integrity.
     */
    public Employee addEmployee(Employee employee) throws InvalidInputException, IdExistsException{
        if(!employeeManager.ifExists(employee.getId())) {
            if(employee.getId() != null && !employee.getFirstName().isBlank() && !employee.getLastName().isBlank() &&
                !employee.getEmail().isBlank() && !employee.getTitle().isBlank() ) {
                employeeManager.addEmployee(employee);
                return employee;
            } else {
                throw new InvalidInputException("All Fields Must Be Completed.");
            }
        } else {
            throw new IdExistsException("ID Must Be Unique.");
        }
    }

    /**
     * A service layer method to delete an employee given id.
     * @param id
     * @return Integer 1 indication one row was deleted.
     * @throws IdNotFoundException if id does not exist.
     */
    public Integer deleteEmployee(Integer id) throws IdNotFoundException {
        if(employeeManager.ifExists(id)) {
            employeeManager.deleteEmployee(id);
            return 1;
        }
        throw new IdNotFoundException("Id Not Found");
    }

    /**
     * A service layer method to update an employee given employee id exists.
     * @param employeeToUpdate
     * @return An employee object with updated details.
     * @throws IdNotFoundException If the id does not exist.
     */
    public Employee updateEmployee(Employee employeeToUpdate) throws IdNotFoundException {
        if(employeeManager.ifExists(employeeToUpdate.getId())) {
            Employee updatedEmployee = employeeManager.updateEmployee(employeeToUpdate);
            return updatedEmployee;
        } throw new IdNotFoundException("Id Not Found");
    }

}

package com.example.employeemgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employeemgmt.employee.Employee;
import com.example.employeemgmt.employees.Employees;
import com.example.employeemgmt.exception.IdExistsException;
import com.example.employeemgmt.exception.IdNotFoundException;
import com.example.employeemgmt.exception.InvalidInputException;
import com.example.employeemgmt.service.EmployeeService;


// Controller class to facilitate http requests.
// Class level mapping is done to expose "/employees" as all methods navigate to this URI.
@RestController
@RequestMapping("/employees")
public class EmployeeController {
    
    private final EmployeeService employeeService;

    // Injecting service class using @Autowired.
    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Method to map incoming GET requests to "/employees".
     * @return A list of all employees and status code 200.
     */
    @GetMapping
    public ResponseEntity<Employees> getAllEmployees() {
        Employees employees =  employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    /**
     * Method to map incoming POST requests to "/employees" to facilitate adding an employee to list.
     * @param employee
     * @return Employee object if operation successful and status code 200.
     * @throws InvalidInputException If some fields are empty and status code 400.
     * @throws IdExistsException If id already exists in system and status code 400.
     */
    @PostMapping
    public ResponseEntity<?> addEmployee(@RequestBody Employee employee) throws InvalidInputException, IdExistsException{
        try{
        Employee addedEmployee = employeeService.addEmployee(employee);
        
        return new ResponseEntity<>(addedEmployee, HttpStatus.OK);
        } catch(InvalidInputException i) {
            return new ResponseEntity<>(i.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(IdExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * A method to map DELETE requests to "/employees/{id}".
     * @param id
     * @return Integer 1 if operation is successful indicating 1 row was changed and 200 status code..
     * @throws IdNotFoundException If id does not exist and 404 status code.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable Integer id) throws IdNotFoundException{
        try{
            Integer rowsDeleted = employeeService.deleteEmployee(id);
            return new ResponseEntity<>("Rows Deleted: " + rowsDeleted, HttpStatus.OK);
        } catch(IdNotFoundException i ) {
            return new ResponseEntity<>(i.getMessage() + "\nRows Deleted: " + 0, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * A method to map PUT requests to/"employees" for the purpose of updating an employee.
     * @param employee
     * @return The employee object with updated fields as confirmation and HTTP status OK.
     * @throws IdNotFoundException If id could not be found and sends 404 status code.
     */
    @PutMapping
    public ResponseEntity<?> updateEmployee(@RequestBody Employee employee) throws IdNotFoundException{
        try{

        Employee updatedEmployee = employeeService.updateEmployee(employee);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
        } catch (IdNotFoundException i) {
            return new ResponseEntity<>(i.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}

package com.example.employeemgmt.EmployeeManagerTests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.employeemgmt.DAO.EmployeeManager;
import com.example.employeemgmt.employee.Employee;
import com.example.employeemgmt.employees.Employees;

/**
 * A test class to test the DAO layer.
 */
public class EmployeeManagerTests {
    private EmployeeManager employeeManager;
    private Employees employees;

    /**
     * Setting up a mock list of employees to use before each test is run.
     */
    @BeforeEach
    void setup() {
        employees = new Employees(new ArrayList<>(List.of(
            new Employee(1,"Min", "Ran", "mran@gmail.com", "Developer"),
            new Employee(2,"Sue", "Matthew", "smatt@gmail.com", "Project Manager"),
            new Employee(3,"Winter", "Wong", "wwong@gmail.com", "Risk Analyst")
        )));
        employeeManager = new EmployeeManager(employees);
    }
    
    /**
     * Validates method by asserting that the expected size of employees list is returned 
     * when method is called.
     */
    @Test
    void testGetAllEmployees() {
        assertEquals(3, employeeManager.getAllEmployees().getEmployeeList().size());
    }

    /**
     * Test validates that add operation is successful by asserting that new employee id exists in database
     * and the size of list increases by one.
     */
    @Test
    void testAddEmployeeSuccessful() {
    Employee employeeToAdd = new Employee(4, "Joe", "Wonder", "wonder@gmail.com", "Architect");
    
    Employee addedEmployee = employeeManager.addEmployee((employeeToAdd));

    assertEquals(4, addedEmployee.getId());
    assertEquals(4, employeeManager.getAllEmployees().getEmployeeList().size());
    assertTrue(employeeManager.ifExists(4));
    }

    /**
     * Test validates a delete operation is successful by asserting that deleted employee id no longer exists 
     * in database and size of list decreases by one.
     */
    @Test
    void testDeleteEmployeesuccessful() {
        employeeManager.deleteEmployee(3);
        assertFalse(employeeManager.ifExists(3));
        assertEquals(2, employeeManager.getAllEmployees().getEmployeeList().size());
    }

    /**
     * Test vaidates method finds an employee correctly by asserting the employee 
     * returned by method has same first name as expected.
     */
    @Test
    void testFindByIdSuccessful() {
        Employee foundEmployee = employeeManager.findById(2);
        assertNotNull(foundEmployee);
        assertEquals("Sue", foundEmployee.getFirstName());
    }

    /**
     * Test validates null employee is returned if an id that does not exist in database is sent as param.
     */
    @Test
    void testFindByIdUnsuccesful() {
        Employee foundEmployee = employeeManager.findById(10);
        assertNull(foundEmployee);
    }

    /**
     * Test validates employee is updated successfully given requirements are met by asserting field values of employee match what is expected.
     */
    @Test
    void testUpdateEmployeeSuccessful() {
        Employee updatedEmployeeDetails = new Employee(3, "Spring", "Saint", "saint@gmail.com", "Project Manager");
        Employee updatedEmployee = employeeManager.updateEmployee(updatedEmployeeDetails);

        assertNotNull(updatedEmployee);
        assertEquals(3,updatedEmployee.getId());
        assertEquals("Spring", updatedEmployee.getFirstName());
        assertEquals("Saint", updatedEmployee.getLastName());
        assertEquals("saint@gmail.com", updatedEmployee.getEmail());
        assertEquals("Project Manager", updatedEmployee.getTitle());
    }

    /**
     * Test validates employee update is unsuccessful if requirements are not met.
     */
    @Test
    void testUpdateEmployeeUnsuccessful() {
        Employee updatedEmployeeDetails = new Employee(5, "Song", "Smith", "smith@gmail.com", "Project Manager");
        Employee updatedEmployee = employeeManager.updateEmployee(updatedEmployeeDetails);

        assertNull(updatedEmployee);
    }

    /**
     * Test validates method returns true if employee id exists in database.
     */
    @Test
    void testIfExists_True() {
        assertTrue(employeeManager.ifExists(1));
    }

    /**
     * Test validaes method returns false if employee id not existing in database is sent in as param.
     */
    @Test
    void testIfExists_False() {
        assertFalse(employeeManager.ifExists(14));
    }

}

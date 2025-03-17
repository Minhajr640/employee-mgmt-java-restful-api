package com.example.employeemgmt.EmployeeServiceTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.employeemgmt.DAO.EmployeeManager;
import com.example.employeemgmt.employee.Employee;
import com.example.employeemgmt.employees.Employees;
import com.example.employeemgmt.exception.IdExistsException;
import com.example.employeemgmt.exception.IdNotFoundException;
import com.example.employeemgmt.exception.InvalidInputException;
import com.example.employeemgmt.service.EmployeeService;

/**A testing class to unit test the Service layer methods. */
@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    /**
     * EmployeeManager class is mocked and injected as service layer directly interacts with the 
     * DAO layer.
     */
    @Mock
    private EmployeeManager employeeManager;

    @InjectMocks
    private EmployeeService employeeService;

    /**
     * Test to validate service layer method to get all employees returns list with size and fields as expected.
     */
    @Test 
    void testGetAllEmployees() {
        Employees employeesMock = new Employees(
            List.of(new Employee(1,"Sue", "Summer","summer@gmail.com", "Developer"),
                    new Employee(2, "Sam", "Thomas", "thomas@gmail.com", "Architect"),
                    new Employee(3, "David", "Shu", "shu@gmail.com", "Project Manager"))
        );
        when(employeeManager.getAllEmployees()).thenReturn(employeesMock);

        Employees employeesMockList = employeeService.getAllEmployees();
        assertEquals(3, employeesMockList.getEmployeeList().size());
        assertEquals("Sue", employeesMockList.getEmployeeList().get(0).getFirstName());
    }

    /**
     * Test to validate service layer method to add employee is successful if all requirements are met and 
     * the returned object has fields that match expected fields.
     */
    @Test
    void testAddEmployeeSuccessful() {
        Employee employeeToAdd = new Employee(4, "Crystal", "Kurt", "kurt@gmail.com", "Analyst");

        when(employeeManager.addEmployee(employeeToAdd)).thenReturn(employeeToAdd);
        Employee addedEmployee = employeeManager.addEmployee(employeeToAdd);

        assertNotNull(addedEmployee);
        assertEquals(4, addedEmployee.getId());
        assertEquals("Crystal", addedEmployee.getFirstName());
        assertEquals("Kurt", addedEmployee.getLastName());
        assertEquals("kurt@gmail.com", addedEmployee.getEmail());
        assertEquals("Analyst", addedEmployee.getTitle());
    }

    /**
     * Test to validate add operation throws an exception if existing id is sent in as a param.
     */
    @Test
    void testAddEmployeeUnsuccessfulId() {
        Employee employeeToAdd = new Employee(3, "Donna", "Smith", "smith@gmail.com", "Analyst");
        when(employeeManager.ifExists(employeeToAdd.getId())).thenReturn(true);
        
        assertThrows(IdExistsException.class, () -> employeeService.addEmployee(employeeToAdd));

    }

    /**
     * Test to validate add operation throws exception if fields are empty.
     */
    @Test
    void testAddEmployeeUnsuccessfulField() {
        Employee employeeToAdd = new Employee(4, "Donna", "Smith", "smith@gmail.com", " ");
        when(employeeManager.ifExists(employeeToAdd.getId())).thenReturn(false);
        
        assertThrows(InvalidInputException.class, () -> employeeService.addEmployee(employeeToAdd));
    }

    /**
     * Test to validate employee is deleted when method is called and value returend 
     * is 1 indicating one row was deleted.
     */
    @Test
    void testDeleteEmployeesuccessful() {
        when(employeeManager.ifExists(3)).thenReturn(true);

        Integer rowsDeleted = employeeService.deleteEmployee(3);
        assertEquals(1, rowsDeleted);
    }

    /**
     * Test to validate delete operation is unsuccessful if id does not exist and asserts that
     * exception is thrown.
     */
    @Test
    void testDeleteEmployeeunsucessfulId() {
        when(employeeManager.ifExists(7)).thenReturn(false);

        assertThrows(IdNotFoundException.class, () -> employeeService.deleteEmployee(7));
    }

    /**
     * Test to validate update employee operation is successful if requiremts are met and 
     * expected employee object is returned back.
     */
    @Test
    void testUpdateEmployeeSuccessful() {
        Employee updatedEmployeeDetails = new Employee(3, "John", "Jill", "jill@gmail.com", "Jr. Developer");

        when(employeeManager.ifExists(3)).thenReturn(true);
        when(employeeManager.updateEmployee(updatedEmployeeDetails)).thenReturn(updatedEmployeeDetails);

        Employee updatedEmployee = employeeService.updateEmployee(updatedEmployeeDetails);
        assertNotNull(updatedEmployee);
        assertEquals(3, updatedEmployee.getId());
        assertEquals("John", updatedEmployee.getFirstName());
        assertEquals("Jill", updatedEmployee.getLastName());
        assertEquals("jill@gmail.com", updatedEmployee.getEmail());
        assertEquals("Jr. Developer", updatedEmployee.getTitle());
    }

    /**
     * Test to validate employee add operation is unsuccessful and exception is thrown if id that doesn't exist is sent as param.
     */
    @Test
    void testUpdateEmployeeUnsuccessful() {
        Employee updatedEmployeeDetails = new Employee(7, "Joseph", "Jakarta", "jakarta@gmail.com", "Sr. Developer");
        
        when(employeeManager.ifExists(7)).thenReturn(false);
        
        assertThrows(IdNotFoundException.class, () -> employeeService.updateEmployee(updatedEmployeeDetails));
    }
}

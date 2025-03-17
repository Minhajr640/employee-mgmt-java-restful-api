package com.example.employeemgmt.EmployeeControllerTests;
import static org.mockito.Mockito.when;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.employeemgmt.controller.EmployeeController;
import com.example.employeemgmt.employee.Employee;
import com.example.employeemgmt.employees.Employees;
import com.example.employeemgmt.exception.IdExistsException;
import com.example.employeemgmt.exception.IdNotFoundException;
import com.example.employeemgmt.exception.InvalidInputException;
import com.example.employeemgmt.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hamcrest.Matchers;

/**
 * This class mocks the controller class and tests it in isolation.
 */
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTests {

    /**
     * MockMvc is injected to simulate HTTP requests.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Employee service mock is injected as it is needed for the controller class.
     */
    @SuppressWarnings("removal")
    @MockBean
    private EmployeeService employeeService;

    /** Object Mapper is used in this class mainly to convert objects into proper JSON format. */
    ObjectMapper om = new ObjectMapper();

    /**
     * Test to validate employees are added successfully when a POST request 
     * is sent to "/employees" and all requirements are met.
     * @throws Exception
     */
    @Test
    void testAddEmployeeSuccessful() throws Exception{
        Employee employeeMock = new Employee(2, "Zel", "Zimmer","zimmer@gmail.com","Manager");
        when(employeeService.addEmployee(Mockito.any(Employee.class))).thenReturn(employeeMock);
        this.mockMvc
            .perform(MockMvcRequestBuilders
            .post("/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(employeeMock)))

            .andExpect(MockMvcResultMatchers.status().isOk()) 
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2));
    }

    /**
     * Test to validate employee is not added if id already exists.
     * @throws Exception
     */
    @Test
    void testAddEmployeeUnsuccessfulId() throws Exception{
        Employee employeeMock = new Employee(1, "Moe", "Min", "moe@gmail.com", "Developer");
        when(employeeService.addEmployee(Mockito.any(Employee.class))).thenThrow(new IdExistsException("ID Must Be Unique."));

        this.mockMvc
        .perform(MockMvcRequestBuilders
        .post("/employees")
        .contentType(MediaType.APPLICATION_JSON)
        .content(om.writeValueAsString(employeeMock)))

        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().string("ID Must Be Unique."));
    }

    /**
     * Test to validate employee is not added if fields are missing.
     * @throws Exception
     */
    @Test
    void testAddEmployeeUnsuccessfulField() throws Exception{
        Employee employeeMock = new Employee(5, "Moe", "Min", "moe@gmail.com", "Developer");
        when(employeeService.addEmployee(Mockito.any(Employee.class))).thenThrow(new InvalidInputException("Fields Can Not Be Empty."));

        this.mockMvc
        .perform(MockMvcRequestBuilders
        .post("/employees")
        .contentType(MediaType.APPLICATION_JSON)
        .content(om.writeValueAsString(employeeMock)))

        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().string("Fields Can Not Be Empty."));
    }

    /**
     * Test to validate list of all employees are returned when a get request is sent to "/employees".
     * @throws Exception
     */
    @Test
    void testGetAllEmployeesSuccessful() throws Exception {
        List<Employee> employeeList = List.of (new Employee(1,"sal","Sam","ssal@gmail.com","Manager"));
        Employees employeesMock = new Employees(employeeList);
        when(employeeService.getAllEmployees()).thenReturn(employeesMock);

        String jsonResponse = this.mockMvc.perform(MockMvcRequestBuilders.get("/employees"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

        System.out.println("Response JSON: " + jsonResponse);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/employees"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.employeeList", Matchers.hasSize(1)));
    }

    /**
     * Test to validate an employee is deleted successfully when a DELETE request
     * is sent to "/employees/{id}" given id exists.
     * @throws Exception
     */
    @Test
    void testDeleteEmployeeSuccessful() throws Exception {
        Employee employeeMock = new Employee(3,"Sid", "Soul", "ssoul@gmail.com", "Architect");
        when(employeeService.deleteEmployee(employeeMock.getId())).thenReturn(1);

        this.mockMvc.perform(MockMvcRequestBuilders
        .delete("/employees/{id}", employeeMock.getId()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("Rows Deleted: 1"));
    }

    /**
     * Test to validate exception is thrown and 404 status is sent whena DELETE 
     * request is sent to "employees/{id}" but id does not exist.
     */
    @Test
    void testDeleteEmployeeUnsuccessful() throws Exception {
        Employee employeeMock = new Employee(3,"Sid", "Soul", "ssoul@gmail.com", "Architect");
        when(employeeService.deleteEmployee(employeeMock.getId())).thenThrow(new IdNotFoundException("Id Not Found."));

        this.mockMvc.perform(MockMvcRequestBuilders
        .delete("/employees/{id}", employeeMock.getId()))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.content().string("Id Not Found.\nRows Deleted: 0"));
    }

    /**
     * Test to validate employee is updated successfully when a PUT request is sent to 
     * /"employees" with valid fields.
     */
    @Test
    void updateEmployeeSuccessful() throws Exception {
        Employee employeeMock = new Employee(1,"Sid", "Soul", "ssoul@gmail.com", "Architect");
        when(employeeService.updateEmployee(Mockito.any(Employee.class))).thenReturn(employeeMock);

        this.mockMvc.perform(MockMvcRequestBuilders
        .put("/employees")
        .contentType(MediaType.APPLICATION_JSON)
        .content(om.writeValueAsString(employeeMock)))

        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    /**
     * Test to validate IdNotFound exception is thrown when an PUT request is sent to
     * "/employees" but id can not be found.
     * @throws Exception
     */
    @Test
    void updateEmployeeUnsuccessful() throws Exception {
        Employee employeeMock = new Employee(1,"Sid", "Soul", "ssoul@gmail.com", "Architect");
        when(employeeService.updateEmployee(Mockito.any(Employee.class))).thenThrow(new IdNotFoundException("Id Not Found."));

        this.mockMvc.perform(MockMvcRequestBuilders
        .put("/employees")
        .contentType(MediaType.APPLICATION_JSON)
        .content(om.writeValueAsString(employeeMock)))

        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.content().string("Id Not Found."));
    }
}

package sg.edu.nus.iss.day22_lecture.controller;

import java.util.List;

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

import sg.edu.nus.iss.day22_lecture.model.Employee;
import sg.edu.nus.iss.day22_lecture.repository.EmployeeRepo;

@RequestMapping ("/api/employees")
@RestController
public class EmployeeController {
    
    // Autowiring the EmployeeRepo to access its methods
    @Autowired
    EmployeeRepo empRepo;

    // This method maps the GET request "/api/employees/" and returns a list of employees
    @GetMapping("/")
    public List<Employee> retreiveEmployees() {

        // Get the list of employees from EmployeeRepo
        List<Employee> employees = empRepo.retrieveEmployeeList();

        // If the list is empty, return null
        if (employees.isEmpty()) {
            return null;
        } else {
            // Otherwise, return the list of employees
            return employees;
        }
       
    }

    // This method maps the POST request "/api/employees/" and creates a new employee
    @PostMapping("/")
    public ResponseEntity<Boolean> createEmployee(@RequestBody Employee employee) {
        Employee emp = employee;
        // Save the employee to the database and get the result of the operation
        Boolean result = empRepo.save(emp);

        // If the save was successful, return a ResponseEntity with the result and status code CREATED
        if (result) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else {
            // If the save was not successful, return a ResponseEntity with null and status code INTERNAL_SERVER_ERROR
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    // This method maps the PUT request "/api/employees/" and updates an existing employee
    @PutMapping("/")
    public ResponseEntity<Integer> updateEmployee(@RequestBody Employee employee) {
        Employee emp = employee;
        // Update the employee in the database and get the number of updated records
        int updated = empRepo.update(emp);

        // If the update was successful (updated == 1), return a ResponseEntity with the result and status code OK
        if (updated == 1) {
            return new ResponseEntity<>(1, HttpStatus.OK);
        } else {
            // If the update was not successful, return a ResponseEntity with null and status code INTERNAL_SERVER_ERROR
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }        
    }

    // This method maps the DELETE request "/api/employees/{id}" and deletes an employee by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteEmployee(@PathVariable("id") Integer id) {
        // Initialize deleteResult to 0
        int deleteResult = 0;

        // Delete the employee by id from the database and get the result of the operation
        deleteResult = empRepo.deleteById(id);

        // If the delete was not successful (deleteResult == 0), return a ResponseEntity with 0 and status code NOT_FOUND
        if (deleteResult == 0) {
            return new ResponseEntity<>(0, HttpStatus.NOT_FOUND);
        } else {
            // If the delete was successful, return a ResponseEntity with the value of 1 and HTTP status code OK
            return new ResponseEntity<>(1, HttpStatus.OK);
        }       
    }


}

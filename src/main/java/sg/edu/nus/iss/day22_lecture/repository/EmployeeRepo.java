package sg.edu.nus.iss.day22_lecture.repository;

import java.util.List;

import sg.edu.nus.iss.day22_lecture.model.Employee;

public interface EmployeeRepo {
    
    // List of Employee objects representing all employees stored in the repository.
    List<Employee> retrieveEmployeeList();

    // Create
    Boolean save(Employee employee);

    // Update
    int update(Employee employee);

    // Delete
    int deleteById(Integer id);

}

package sg.edu.nus.iss.day22_lecture.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.day22_lecture.model.Dependant;
import sg.edu.nus.iss.day22_lecture.model.Employee;

@Repository
public class EmployeeRepoImpl implements EmployeeRepo {

    // Autowired JdbcTemplate for database operations
    @Autowired
    JdbcTemplate jdbcTemplate;

    // SQL statements for CRUD operations on employee table
    String selectSQL = "select * from employee";
    String selectByIdSQL = "select * from employee where id = ?";
    String insertSQL = "insert into employee (first_name, last_name, salary) values (?, ?, ?)";
    String updateSQL = "update employee set first_name = ?, last_name = ?, salary = ? where id = ?";
    String deleteSQL = "delete from employee where id = ?";


    // Implementation of retrieveEmployeeList method from EmployeeRepo interface
    @Override
    public List<Employee> retrieveEmployeeList() {
        // Modified SQL statement to join employee and dependant tables
        String selectSQL = "select e.id emp_id, e.first_name, e.last_name, e.salary, " +
                    "d.id dep_id, d.fullname, d.relationship, d.birth_date " + 
                    "from employee e " + 
                    "inner join dependant d " + 
                    "on e.id = d.employee_id ";

                    // Return the result of the query by calling the query method on JDBC template
                    return jdbcTemplate.query(selectSQL, new ResultSetExtractor<List<Employee>>() {

                        // Overriding extractData method of ResultSetExtractor to process result set
                        @Override
                        public List<Employee> extractData(ResultSet rs) throws SQLException, DataAccessException {
                            // List to store employees with dependants
                            List<Employee> employees = new ArrayList<Employee>();

                            // Loop through result set and extract data for employees and dependants
                            while (rs.next()) {

                                Employee emp = new Employee();
                                emp.setId(rs.getInt("emp_id"));
                                emp.setFirstName(rs.getString("first_name"));
                                emp.setLastName(rs.getString("last_name"));
                                emp.setSalary(rs.getInt("salary"));
                                emp.setDependants(new ArrayList<Dependant>());

                                Dependant dep = new Dependant();
                                dep.setId(rs.getInt("dep_id"));
                                dep.setFullName(rs.getString("fullname"));
                                dep.setRelationship(rs.getString("relationship"));
                                dep.setBirthDate(rs.getDate("birth_date"));

                                // Checking if employee already exists in the list, adding dependant to that employee
                                if (employees.size() == 0) {
                                    // If the employees list is empty, add the dependent to the first employee and add the employee to the list
                                    emp.getDependants().add(dep);
                                    employees.add(emp);
                                } else {
                                    // If the employees list is not empty, search for an employee with the same ID
                                    List<Employee> eList = employees.stream().filter(e -> e.getId() == emp.getId()).collect(Collectors.toList());

                                    if (eList.size() == 0) {
                                        // If no employee with the same ID is found, add the dependent to the first employee and add the employee to the list
                                        emp.getDependants().add(dep);
                                        employees.add(emp);
                                    } else {
                                        // If an employee with the same ID is found, get the index of that employee in the list
                                        int idx = employees.indexOf(eList.get(0));
                                        
                                        if (idx >= 0) {
                                            // If the index is found, add the dependent to that employee
                                            employees.get(idx).getDependants().add(dep);
                                        }
                                    }
                                }

                            }
                            return employees;
                        }
                        
                    });

    }



    @Override
    public Boolean save(Employee employee) {
        Boolean saved = false;

        // Call the execute method on the JDBC template with the insert SQL query and a prepared statement callback
        saved = jdbcTemplate.execute(insertSQL, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                // Set the first name, last name, and salary in the prepared statement
                ps.setString(1, employee.getFirstName());
                ps.setString(2, employee.getLastName());
                ps.setInt(3, employee.getSalary());
                // Execute the prepared statement
                Boolean rslt = ps.execute();
                return rslt;
            }
            
        });
        // Return the result of the save operation
        return saved;
    }

    @Override
    public int update(Employee employee) {
        int updated = 0;

        // Call the update method on the JDBC template with the update SQL query and a prepared statement setter
        updated = jdbcTemplate.update(updateSQL, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                // Set the first name, last name, salary, and ID in the prepared statement
                ps.setString(1, employee.getFirstName());
                ps.setString(2, employee.getLastName());
                ps.setInt(3, employee.getSalary());
                ps.setInt(4, employee.getId());
                
            }            
        });
        // Return the number of updated records
        return updated;
    }

    @Override
    public int deleteById(Integer id) {
        int deleted = 0;

        // Create a prepared statement setter to set the ID in the prepared statement
        PreparedStatementSetter pss = new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                // Set the first parameter to the ID passed to the method
                ps.setInt(1, id);               
            }            
        };

        // Execute the delete statement using the prepared statement setter
        deleted = jdbcTemplate.update(deleteSQL, pss);
        
        // Return the number of deleted rows
        return deleted;
    }
    
}

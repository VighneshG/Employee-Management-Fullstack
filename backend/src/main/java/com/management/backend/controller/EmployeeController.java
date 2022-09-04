package com.management.backend.controller;

import com.management.backend.exceptions.ResourceNotFound;
import com.management.backend.model.Employee;
import com.management.backend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    // Get Employee List
    @GetMapping("/employees")
    public List<Employee> getEmployeeList(){
        return employeeRepository.findAll();
    }

    // Create Employee
    @PostMapping("/createEmployee")
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    // Get Employee by id

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id){

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Resource Not found by given ID"));
        return ResponseEntity.ok(employee);

    }

    // Update Employee details
    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeNew){
        // fetch employee by Id first, and then update the details
        Employee employeeOld = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Invalid Id"));

        // Set all the parameters
        employeeOld.setFirstName(employeeNew.getFirstName());
        employeeOld.setLastName(employeeNew.getLastName());
        employeeOld.setEmailId(employeeNew.getEmailId());

        // Save the details in Repository
        Employee updatedEmployee = employeeRepository.save(employeeOld);

        return ResponseEntity.ok(updatedEmployee);
    }

    // Delete Employee
    @DeleteMapping("/employees/{id}")
    public Map<String, Boolean> deleteEmployee(@PathVariable Long id){
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Invalid Id"));
        employeeRepository.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("Employee with Id: "+employee.getId()+" has been deleted.", Boolean.TRUE);
        return response;
    }
}

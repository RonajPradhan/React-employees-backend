package app.backend.Controller;

import app.backend.Exception.ResourceNotFoundException;
import app.backend.Model.Employee;
import app.backend.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/")
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;


//    get all employees
    @GetMapping("/employees")
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

//    get employee by id
    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id does not exist!!"));
        return ResponseEntity.ok(employee);
    }

//    add Employee to the table

        @PostMapping("/employees")
                public Employee addEmployee(@RequestBody Employee employee){
                    return employeeRepository.save(employee);
        }

//        Update employee
        @PutMapping("/employees/{id}")
            public ResponseEntity<Employee> updateEmployee(@PathVariable Long id,@RequestBody Employee employeeDetails){
            Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id does not exist!!"));
            employee.setFirstName(employeeDetails.getFirstName());
            employee.setLastName(employeeDetails.getLastName());
            employee.setEmailId(employeeDetails.getEmailId());

            Employee updatedEmployee = employeeRepository.save(employee);
            return ResponseEntity.ok(updatedEmployee);

        }

        @DeleteMapping("/employees/{id}")
            public ResponseEntity<Map<String,Boolean>> deleteEmployee(@PathVariable Long id){
                Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id Not Found!!"));
                employeeRepository.delete(employee);
                Map<String,Boolean> response = new HashMap<String,Boolean>();
                response.put("Deleted",Boolean.TRUE);
                return ResponseEntity.ok(response);

        }
    }



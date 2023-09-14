package app.backend.Controller;

import app.backend.Service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import app.backend.dto.EmployeeDto;
import java.util.List;


@CrossOrigin(origins ="*")
@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class EmployeeController {
    private EmployeeService employeeService;


//    get all employees
    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees(){
       List<EmployeeDto> employees = employeeService.getAllEmployees();
       return ResponseEntity.ok(employees);
    }

//    get employee by id
    @GetMapping("/employees/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
        EmployeeDto employeeDto = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employeeDto);
    }

//    add Employee to the table

        @PostMapping("/employees")
                public ResponseEntity<EmployeeDto>  addEmployee(@RequestBody EmployeeDto employeeDto){
                    EmployeeDto savedEmployee = employeeService.createEmployee(employeeDto);
                    return ResponseEntity.ok(savedEmployee);
        }

//        Update employee
        @PutMapping("/employees/{id}")
            public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id,@RequestBody EmployeeDto updatedEmployee){
            EmployeeDto employeeDto = employeeService.updateEmployee(id,updatedEmployee);
            return ResponseEntity.ok(employeeDto);
        }

        @DeleteMapping("/employees/{id}")
            public ResponseEntity<String> deleteEmployee(@PathVariable Long id){
                employeeService.deleteEmployeeById(id);
                return ResponseEntity.ok("Employee Deleted successfully");

        }
    }



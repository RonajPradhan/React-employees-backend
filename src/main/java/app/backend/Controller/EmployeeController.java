package app.backend.Controller;

import app.backend.Model.Employee;
import app.backend.Service.EmployeeService;
import app.backend.dto.payload.Request.EmployeeRequest;
import app.backend.dto.payload.Response.EmployeeResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;



@CrossOrigin(value ="*")

@RestController
@RequestMapping("/api/v1/")
public class EmployeeController {
    private EmployeeService employeeService;

//    get all employees
    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees(){
       List<EmployeeResponse> employees = employeeService.getAllEmployees();
       return ResponseEntity.ok(employees);
    }

//    get employee by id
    @GetMapping("/employees/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
        EmployeeResponse employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

//    add Employee to the table

    @PostMapping("/employees")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<EmployeeResponse>  addEmployee(@RequestBody EmployeeRequest employeeRequest){
        EmployeeResponse savedEmployee = employeeService.createEmployee(employeeRequest);
        return ResponseEntity.ok(savedEmployee);
    }

//        Update employee
    @PutMapping("/employees/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable Long id,@RequestBody EmployeeRequest employeeRequest){
        EmployeeResponse updatedEmployee = employeeService.updateEmployee(id,employeeRequest);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/employees/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id){
        employeeService.deleteEmployeeById(id);
        return ResponseEntity.ok("Employee Deleted successfully");

    }

    @GetMapping("/employeesPagingSorting")
    public ResponseEntity<Map<String,Object>> getPaginatedEmployeeOnly(@RequestParam(name="page",defaultValue = "1") int pageNumber,
                                                                       @RequestParam(name="size",defaultValue = "3") int pageSize){
        return employeeService.getEmployeesByPageOnly(pageNumber,pageSize);
    }

    @GetMapping("/employeesPagingSortingTwo")
    public ResponseEntity<Map<String,Object>> getPaginatedEmployee(@RequestParam(name="page",defaultValue = "1") int pageNumber,
                                               @RequestParam(name="size",defaultValue = "3") int pageSize,
                                               @RequestParam(name="isAsc",required = false) String sortType,
                                               @RequestParam(name="sort",required = false) String sortProperty){
        return employeeService.getEmployeesByPage(pageNumber,pageSize,sortType,sortProperty);
    }
}



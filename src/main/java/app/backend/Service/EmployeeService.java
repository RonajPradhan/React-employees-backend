package app.backend.Service;

import app.backend.Model.Employee;

import app.backend.dto.payload.Request.EmployeeRequest;
import app.backend.dto.payload.Response.EmployeeResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface EmployeeService {

    List<EmployeeResponse> getAllEmployees();
    EmployeeResponse createEmployee(EmployeeRequest employeeRequest);
    EmployeeResponse updateEmployee(Long id,EmployeeRequest employeeRequest);
    EmployeeResponse getEmployeeById(long id);
    void deleteEmployeeById(long id);

    ResponseEntity<Map<String,Object>> getEmployeesByPageOnly(Integer pageNumber, Integer pageSize);

    ResponseEntity<Map<String,Object>> getEmployeesByPage(Integer pageNumber, Integer pageSize, String sortType, String sortProperty);

    
}

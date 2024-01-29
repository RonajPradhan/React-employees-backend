package app.backend.Service;

import app.backend.Model.Employee;
import app.backend.dto.EmployeeDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface EmployeeService {

    List<EmployeeDto> getAllEmployees();
    EmployeeDto createEmployee(EmployeeDto employeeDto);
    EmployeeDto updateEmployee(Long id,EmployeeDto updatedEmployee);
    EmployeeDto getEmployeeById(long id);
    void deleteEmployeeById(long id);

    ResponseEntity<Map<String,Object>> getEmployeesByPageOnly(Integer pageNumber, Integer pageSize);

    ResponseEntity<Map<String,Object>> getEmployeesByPage(Integer pageNumber, Integer pageSize, String sortType, String sortProperty);

    
}

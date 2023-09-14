package app.backend.Service;

import app.backend.Model.Employee;
import app.backend.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {

    List<EmployeeDto> getAllEmployees();
    EmployeeDto createEmployee(EmployeeDto employeeDto);
    EmployeeDto updateEmployee(Long id,EmployeeDto updatedEmployee);
    EmployeeDto getEmployeeById(long id);
    void deleteEmployeeById(long id);

    
}

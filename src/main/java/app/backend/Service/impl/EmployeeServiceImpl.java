package app.backend.Service.impl;

import app.backend.Exception.ResourceNotFoundException;
import app.backend.Mapper.EmployeeMapper;
import app.backend.Model.Employee;
import app.backend.Repository.EmployeeRepository;
import app.backend.Service.EmployeeService;
import app.backend.dto.EmployeeDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    private ModelMapper modelMapper;

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map((employee) -> modelMapper.map(employee,EmployeeDto.class)).collect(Collectors.toList());
    }

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto){
        Employee employee = modelMapper.map(employeeDto, Employee.class);
        Employee savedEmployee = employeeRepository.save(employee);
        EmployeeDto savedEmployeeDto = modelMapper.map(savedEmployee,EmployeeDto.class);

//        Why didn't this work??

//        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
//        Employee savedEmployee = employeeRepository.save(employee);
//        return EmployeeMapper.mapToEmployeeDto(savedEmployee);
        return savedEmployeeDto;

    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto updatedEmployee) {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Employee is not exists with given id: " + id)
        );

        employee.setFirstName(updatedEmployee.getFirstName());
        employee.setLastName(updatedEmployee.getLastName());
        employee.setEmailId(updatedEmployee.getEmailId());

        Employee updatedEmployeeObj = employeeRepository.save(employee);

        return modelMapper.map(updatedEmployeeObj,EmployeeDto.class);
    }

    @Override
    public EmployeeDto getEmployeeById(long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee is not exists with given id : " + id));

        return modelMapper.map(employee,EmployeeDto.class);
    }

    @Override
    public void deleteEmployeeById(long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Employee is not exists with given id: " + id)
        );

        employeeRepository.deleteById(id);

    }
}

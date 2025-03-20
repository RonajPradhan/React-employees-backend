package app.backend.Service.impl;

import app.backend.Exception.GlobalExceptionHandler;
import app.backend.Exception.ResourceNotFoundException;
import app.backend.Mapper.EmployeeMapper;
import app.backend.Model.Employee;
import app.backend.Repository.EmployeeRepository;
import app.backend.Service.EmployeeService;

import app.backend.dto.payload.Request.EmployeeRequest;
import app.backend.dto.payload.Response.EmployeeResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    private EmployeeMapper employeeMapper;

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map((employee) -> employeeMapper.fromEmployee(employee)).collect(Collectors.toList());
    }

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) {
        Employee employee = employeeMapper.toEmployee(employeeRequest);
        Employee savedEmployee = employeeRepository.save(employee);

        return employeeMapper.fromEmployee(savedEmployee);
    }

    @Override
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest employeeRequest) {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Employee does not exists with given id: " + id)
        );

        employee.setFirstName(employeeRequest.firstName());
        employee.setLastName(employeeRequest.lastName());
        employee.setEmailId(employeeRequest.emailId());

        Employee updatedEmployee = employeeRepository.save(employee);
        return employeeMapper.fromEmployee(updatedEmployee);
    }

    @Override
    public EmployeeResponse getEmployeeById(long id) {
        System.out.println(id);
        System.out.println(employeeRepository.findById(id).isPresent());
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee does not exists with given id : " + id));
        return employeeMapper.fromEmployee(employee);
    }

    @Override
    public void deleteEmployeeById(long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Employee does not exists with given id: " + id)
        );
        employeeRepository.deleteById(id);
    }

    @Override
    public ResponseEntity<Map<String,Object>> getEmployeesByPageOnly(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        Page<Employee> pageEmps = employeeRepository.findAll(pageable);
        Map<String,Object> response = new HashMap<>();
        response.put("employees",pageEmps.getContent());
        response.put("currentPage",pageEmps.getNumber() + 1);
        response.put("totalItems",pageEmps.getTotalElements());
        response.put("totalPages",pageEmps.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<String,Object>> getEmployeesByPage(Integer pageNumber, Integer pageSize, String sortType, String sortProperty) {
        if(sortType == null && sortProperty == null){
            return getEmployeesByPageOnly(pageNumber,pageSize);
        }
        Sort sort = sortType.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortProperty).ascending() :
                Sort.by(sortProperty).descending();
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort); //pageNumber is -1 to balance out the 0 and 1
        Page<Employee> pageEmps = employeeRepository.findAll(pageable);
        Map<String,Object> response = new HashMap<>();
        response.put("employees",pageEmps.getContent());
        response.put("currentPage",pageEmps.getNumber() + 1);
        response.put("totalItems",pageEmps.getTotalElements());
        response.put("totalPages",pageEmps.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
        }

    }



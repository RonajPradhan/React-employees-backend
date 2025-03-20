package app.backend.Mapper;

import app.backend.Model.Employee;

import app.backend.dto.payload.Request.EmployeeRequest;
import app.backend.dto.payload.Response.EmployeeResponse;
import org.springframework.stereotype.Service;

@Service
public class EmployeeMapper {

    public Employee toEmployee(EmployeeRequest request) {
        if(request == null) return null;

        return Employee.builder()
                .id(request.id())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .emailId(request.emailId())
                .build();
    }

    public EmployeeResponse fromEmployee(Employee employee){
        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmailId(),
                employee.getCreatedAt()
        );
    }
}

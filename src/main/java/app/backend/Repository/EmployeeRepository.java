package app.backend.Repository;

import app.backend.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    Optional<Employee> findByfirstName(String employeeFirstName);

    Optional<Employee> findByLastName(String employeeLastName);

}

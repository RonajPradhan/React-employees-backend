package app.backend.component;

import app.backend.Model.ERole;
import app.backend.Model.Role;
import app.backend.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DBOperationRunner implements CommandLineRunner {
    @Autowired RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        roleRepository.saveAll(Arrays.asList(
                new Role(1,ERole.ROLE_ADMIN),
                new Role(2, ERole.ROLE_MODERATOR),
                new Role(3,ERole.ROLE_USER)
        ));
    }
}

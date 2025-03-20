package app.backend.Mapper;

import app.backend.Model.ERole;
import app.backend.Model.Role;
import app.backend.Model.User;
import app.backend.Repository.RoleRepository;
import app.backend.dto.payload.Request.RegistrationRequest;
import app.backend.dto.payload.Response.RegistrationResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class RegistrationMapper {

    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;


    public User toRegister(RegistrationRequest registrationRequest) {
        if(registrationRequest == null) return null;

        return User.builder()
                .firstName(registrationRequest.firstName())
                .lastName(registrationRequest.lastName())
                .userName(registrationRequest.userName())
                .email(registrationRequest.email())
                .password(passwordEncoder.encode(registrationRequest.password()))
                .roles(toRoles(registrationRequest.role()))
                .build();
    }

    public Set<Role> toRoles(Set<String> strRole) {

        Set<Role> role = new HashSet<>();

        if(strRole == null){
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            role.add(userRole);
        }

        else{
            strRole.forEach(auth -> {
                switch (auth){
                    case "admin" -> {
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Role not found"));
                    role.add(adminRole);
                    }

                    case "mod" -> {
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Role not found"));
                        role.add(modRole);
                    }

                    default -> {
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Role not found"));
                        role.add(userRole);
                    }
                }
            });
        }
        return role;
    }

    public RegistrationResponse fromRegister(User user) {
        return new RegistrationResponse(
                user.getFirstName(),
                user.getLastName()
        );
    }
}

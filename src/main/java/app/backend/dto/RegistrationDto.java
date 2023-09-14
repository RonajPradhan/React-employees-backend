package app.backend.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegistrationDto {

    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;

    private Set<String> role;

}

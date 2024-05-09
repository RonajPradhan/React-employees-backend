package app.backend.Service.impl;

import app.backend.Exception.ToDoAPIException;
import app.backend.Exception.TokenRefreshException;
import app.backend.Model.ERole;
import app.backend.Model.RefreshToken;
import app.backend.Model.Role;
import app.backend.Model.User;
import app.backend.Repository.RoleRepository;
import app.backend.Repository.UserRepository;
import app.backend.Security.JwtTokenProvider;
import app.backend.Security.RefreshTokenService;
import app.backend.Security.userService.UserDetailsImpl;
import app.backend.Service.AuthService;
import app.backend.dto.payload.Request.TokenRefreshRequest;
import app.backend.dto.payload.Response.JwtAuthResponse;
import app.backend.dto.LoginDto;
import app.backend.dto.RegistrationDto;
import app.backend.dto.payload.Response.TokenRefreshResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    private RefreshTokenService refreshTokenService;
    private JwtTokenProvider jwtTokenProvider;
    @Override
    public String register(RegistrationDto registrationDto) {
//        check if username already exists in the database

        if(userRepository.existsByUserName(registrationDto.getUserName())){
            throw new ToDoAPIException(HttpStatus.BAD_REQUEST,"Username already exists!");
        }
//
        if(userRepository.existsByEmail(registrationDto.getEmail())){
            throw new ToDoAPIException(HttpStatus.BAD_REQUEST,"Email already exists");
        }

        User user = new User();
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setUserName(registrationDto.getUserName());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

        Set<String> strRoles = registrationDto.getRole();

        Set<Role> roles = new HashSet<>();

        if(strRoles == null) {

            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role not found."));
            roles.add(userRole);

        } else{
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found."));
                        roles.add(adminRole);
                    }
                    case "mod" -> {
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found."));
                        roles.add(modRole);
                    }
                    default -> {
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found."));
                        roles.add(userRole);
                    }
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return "User Registered Successfully";

    }

    @Override
    public ResponseEntity<?> login(LoginDto loginDto){

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));


            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String jwt = jwtTokenProvider.generateJwtToken(authentication);

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            Long userId = userDetails.getId();

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userId);

            return ResponseEntity.ok(new JwtAuthResponse(
                    jwt,
                    userDetails.getUsername(),
                    refreshToken.getToken(),
                    roles
            ));
    }


    @Override
    public ResponseEntity<?> refreshToken(TokenRefreshRequest request){
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::validateExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtTokenProvider.generateTokenFromUsername(user.getUserName());
                    return ResponseEntity.ok(new TokenRefreshResponse(token,requestRefreshToken));
        }).orElseThrow(() -> new TokenRefreshException(requestRefreshToken,"Refresh Token is not in database."));
    }

    @Override
    public ResponseEntity<?> logout(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
//        What about access token? Should they not be invalidated when user logs out?
        refreshTokenService.deleteByUserId(userId);
        return ResponseEntity.ok("Logout successful");
    }
}

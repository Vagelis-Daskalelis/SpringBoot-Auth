package com.vaggelis.UserManagementSystem.services;

import com.vaggelis.UserManagementSystem.dtos.JWTAuthenticationResponse;
import com.vaggelis.UserManagementSystem.dtos.SignInRequest;
import com.vaggelis.UserManagementSystem.dtos.SignUpRequest;
import com.vaggelis.UserManagementSystem.exceptions.UserAlreadyExistsException;
import com.vaggelis.UserManagementSystem.maps.Map;
import com.vaggelis.UserManagementSystem.models.Role;
import com.vaggelis.UserManagementSystem.models.User;
import com.vaggelis.UserManagementSystem.repositories.IUserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService{

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IJWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final IAuditLogService auditLogService;



    //Signs up a user
    @Override
    public void signUp(SignUpRequest request) throws UserAlreadyExistsException {
        User user;

        try {
            Optional<User> userFind = userRepository.findByEmail(request.getEmail());

            if (userFind.isPresent()) throw new UserAlreadyExistsException(request.getEmail());

            user = Map.mapFromInsertUser(request, passwordEncoder);
            auditLogService.log(request.getEmail(), "signup", request.getUname());

            userRepository.save(user);

        } catch (UserAlreadyExistsException e) {
            throw e;
        }
    }

    //Signs in a user
    @Override
    public JWTAuthenticationResponse SignIn(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        var jwt = jwtService.generateToken(user);
        auditLogService.log(user.getEmail(), "signin", user.getUname());
        return JWTAuthenticationResponse.builder().token(jwt).build();
    }

    //Signs up a manager
    @Override
    public void managerSignUp(SignUpRequest request) throws UserAlreadyExistsException {
        User user;

        try {
            Optional<User> userFind = userRepository.findByEmail(request.getEmail());

            if (userFind.isPresent()) throw new UserAlreadyExistsException(request.getEmail());

            user = Map.mapFromInsertUser(request, passwordEncoder);
            auditLogService.log(request.getEmail(), "signup", request.getUname());

            userRepository.save(user);

        } catch (UserAlreadyExistsException e) {
            throw e;
        }
    }

    // creates an admin
    @PostConstruct
    public void createAnAdminAccount(){
        Optional<User> adminAccount = userRepository.findByRole(Role.ADMIN);
        if (adminAccount.isEmpty()){
            User user = new User();
            user.setUname("admin");
            user.setEmail("admin@test.com");
            user.setRole(Role.ADMIN);
            user.setPassword(passwordEncoder.encode("admin"));
            user.setCreatedAt(LocalDateTime.now());
            userRepository.save(user);
            System.out.println("Admin created successfully");
        }else {
            System.out.println("Admin already exists");
        }
    }
}

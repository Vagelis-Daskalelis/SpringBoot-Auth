package com.vaggelis.UserManagementSystem.services;

import com.vaggelis.UserManagementSystem.dtos.JWTAuthenticationResponse;
import com.vaggelis.UserManagementSystem.dtos.SignInRequest;
import com.vaggelis.UserManagementSystem.dtos.SignUpRequest;
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


//    @Override
//    public JWTAuthenticationResponse signUp(SignUpRequest request) {
//        var user = User.builder().username(request.getUsername())
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .role(Role.USER)
//                .createdAt(LocalDateTime.now()).build();
//        userRepository.save(user);
//        var jwt = jwtService.generateToken(user);
//        return JWTAuthenticationResponse.builder().token(jwt).build();
//    }

    @Override
    public JWTAuthenticationResponse signUp(SignUpRequest request) {
        User user = Map.mapFromInsertUser(request, passwordEncoder);

        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return JWTAuthenticationResponse.builder().token(jwt).build();
    }


    @Override
    public JWTAuthenticationResponse SignIn(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        var jwt = jwtService.generateToken(user);
        return JWTAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public JWTAuthenticationResponse managerSignUp(SignUpRequest request) {
        User user = Map.mapFromInsertManager(request, passwordEncoder);

        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return JWTAuthenticationResponse.builder().token(jwt).build();
    }

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

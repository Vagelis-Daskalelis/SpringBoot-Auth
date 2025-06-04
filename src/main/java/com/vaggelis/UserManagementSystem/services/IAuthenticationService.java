package com.vaggelis.UserManagementSystem.services;

import com.vaggelis.UserManagementSystem.dtos.JWTAuthenticationResponse;
import com.vaggelis.UserManagementSystem.dtos.SignInRequest;
import com.vaggelis.UserManagementSystem.dtos.SignUpRequest;

public interface IAuthenticationService {
    JWTAuthenticationResponse signUp(SignUpRequest request);
    JWTAuthenticationResponse SignIn(SignInRequest request);
    JWTAuthenticationResponse managerSignUp(SignUpRequest request);
}

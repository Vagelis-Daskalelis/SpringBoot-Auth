package com.vaggelis.UserManagementSystem.services;

import com.vaggelis.UserManagementSystem.dtos.JWTAuthenticationResponse;
import com.vaggelis.UserManagementSystem.dtos.SignInRequest;
import com.vaggelis.UserManagementSystem.dtos.SignUpRequest;
import com.vaggelis.UserManagementSystem.exceptions.UserAlreadyExistsException;

public interface IAuthenticationService {
    void signUp(SignUpRequest request) throws UserAlreadyExistsException;
    JWTAuthenticationResponse SignIn(SignInRequest request);
    void managerSignUp(SignUpRequest request) throws UserAlreadyExistsException;
}

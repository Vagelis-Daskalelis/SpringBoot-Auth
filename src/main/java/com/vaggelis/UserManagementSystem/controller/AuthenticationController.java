package com.vaggelis.UserManagementSystem.controller;

import com.vaggelis.UserManagementSystem.dtos.JWTAuthenticationResponse;
import com.vaggelis.UserManagementSystem.dtos.ResetPasswordRequest;
import com.vaggelis.UserManagementSystem.dtos.SignInRequest;
import com.vaggelis.UserManagementSystem.dtos.SignUpRequest;
import com.vaggelis.UserManagementSystem.services.IAuthenticationService;
import com.vaggelis.UserManagementSystem.services.PasswordResetServiceImpl;
import com.vaggelis.UserManagementSystem.validator.SignUpValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final IAuthenticationService authenticationService;
    private final SignUpValidator validator;
    private final PasswordResetServiceImpl resetService;

    @PostMapping("/signup")
    public ResponseEntity<JWTAuthenticationResponse> signup(@Valid @RequestBody SignUpRequest request, BindingResult bindingResult) {
        validator.validate(request, bindingResult);
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(authenticationService.signUp(request));
    }

    @PostMapping("/signup/manager")
    public  ResponseEntity<JWTAuthenticationResponse> signupManager(@Valid @RequestBody SignUpRequest request, BindingResult bindingResult) {
        validator.validate(request, bindingResult);
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(authenticationService.managerSignUp(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<JWTAuthenticationResponse> signIn(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(authenticationService.SignIn(request));
    }


    @PostMapping("/request-password-reset")
    public ResponseEntity<String> requestReset(@RequestParam String email) {
        resetService.createPasswordResetToken(email);
        return ResponseEntity.ok("Reset link sent");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest req) {
        resetService.resetPassword(req.getToken(), req.getNewPassword());
        return ResponseEntity.ok("Password updated");
    }

}

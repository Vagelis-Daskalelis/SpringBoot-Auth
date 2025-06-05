package com.vaggelis.UserManagementSystem.controller;

import com.vaggelis.UserManagementSystem.dtos.JWTAuthenticationResponse;
import com.vaggelis.UserManagementSystem.dtos.SignUpRequest;
import com.vaggelis.UserManagementSystem.dtos.UpdateRequest;
import com.vaggelis.UserManagementSystem.exceptions.UserAlreadyExistsException;
import com.vaggelis.UserManagementSystem.exceptions.UserNotFoundException;
import com.vaggelis.UserManagementSystem.models.AuditLog;
import com.vaggelis.UserManagementSystem.models.User;
import com.vaggelis.UserManagementSystem.services.AuthenticationServiceImpl;
import com.vaggelis.UserManagementSystem.services.IAuditLogService;
import com.vaggelis.UserManagementSystem.services.UsersCrudServiceImpl;
import com.vaggelis.UserManagementSystem.validator.SignUpValidator;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resource")
@RequiredArgsConstructor
public class AuthorizationController {

    private final UsersCrudServiceImpl usersCrudService;
    private final SignUpValidator validator;
    private final IAuditLogService auditLogService;

    private final AuthenticationServiceImpl authenticationService;


    @PostMapping("/manager/user/create")
    public ResponseEntity<User> createUser(@Valid @RequestBody SignUpRequest request, BindingResult bindingResult){
        validator.validate(request, bindingResult);
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            User user = usersCrudService.insertUser(request);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/manager/user/update")
    public ResponseEntity<User> updateUser(@Valid @RequestBody UpdateRequest request, BindingResult bindingResult){
        validator.validate(request, bindingResult);
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            User user = usersCrudService.updateUser(request);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/manager/user/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id){

        try {
            User user = usersCrudService.deleteUser(id);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/manager/user/all")
    public ResponseEntity<List<User>> findAllUsers(){
        List<User> users;

        try {
            users = usersCrudService.findAll();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/manager/user/{email}")
    public ResponseEntity<User> findUser(@PathVariable String email){
        try {
            User user = usersCrudService.findUser(email);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/user/your/update/{id}")
    public ResponseEntity<User> updateYourUser(@Valid @PathVariable Long id,
                                           @RequestBody UpdateRequest request,
                                           Authentication authentication,
                                               BindingResult bindingResult) {
        validator.validate(request, bindingResult);
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User currentUser = (User) authentication.getPrincipal();
        Long currentUserId = currentUser.getId();

        try {
            User updatedUser = usersCrudService.updateYourUser(request, id, currentUserId);
            return new ResponseEntity<>(currentUser, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/user/your/delete/{id}")
    public ResponseEntity<User> deleteYourUser(@PathVariable Long id,
                                           Authentication authentication){
        User currentUser = (User) authentication.getPrincipal();
        Long currentUserId = currentUser.getId();


        try {
            User deleted = usersCrudService.deleteYourUser(id, currentUserId);
            return ResponseEntity.ok(deleted);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }


    @PostMapping("/admin/signup/manager")
    public  ResponseEntity<JWTAuthenticationResponse> signupManager(@Valid @RequestBody SignUpRequest request, BindingResult bindingResult) {
        validator.validate(request, bindingResult);
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            authenticationService.managerSignUp(request);
            return  new ResponseEntity<>(HttpStatus.OK);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/admin/logs/all")
    public ResponseEntity<List<AuditLog>> findAllLogs(){
        List<AuditLog> logs;

        try {
            logs = auditLogService.findAll();
            return new ResponseEntity<>(logs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}

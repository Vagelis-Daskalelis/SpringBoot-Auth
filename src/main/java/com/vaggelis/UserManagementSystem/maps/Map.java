package com.vaggelis.UserManagementSystem.maps;

import com.vaggelis.UserManagementSystem.dtos.SignUpRequest;
import com.vaggelis.UserManagementSystem.dtos.UpdateRequest;
import com.vaggelis.UserManagementSystem.models.Role;
import com.vaggelis.UserManagementSystem.models.Status;
import com.vaggelis.UserManagementSystem.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

public class Map {

    @Autowired
    private PasswordEncoder passwordEncoder;



    public Map() {
    }

    public static User mapFromInsertUser(SignUpRequest request, PasswordEncoder passwordEncoder){
        return new User(null, request.getUname(), request.getEmail(), passwordEncoder.encode(request.getPassword()) , Status.ACTIVE, Role.USER,LocalDateTime.now());
    }

    public static User mapFromUpdateUser(UpdateRequest request, PasswordEncoder passwordEncoder){
        return new User(request.getId(), request.getUname(), request.getEmail(), passwordEncoder.encode(request.getPassword()), Status.ACTIVE, Role.USER );
    }

    public static User mapFromInsertManager(SignUpRequest request, PasswordEncoder passwordEncoder){
        return new User(null, request.getUname(), request.getEmail(), passwordEncoder.encode(request.getPassword()) , Status.ACTIVE, Role.MANAGER,LocalDateTime.now());
    }

    public static User mapFromInsertAdmin(SignUpRequest request, PasswordEncoder passwordEncoder){
        return new User(null, request.getUname(), request.getEmail(), passwordEncoder.encode(request.getPassword()) , Status.ACTIVE, Role.ADMIN,LocalDateTime.now());
    }


}

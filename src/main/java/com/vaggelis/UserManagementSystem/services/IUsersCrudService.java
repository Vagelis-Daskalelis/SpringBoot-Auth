package com.vaggelis.UserManagementSystem.services;

import com.vaggelis.UserManagementSystem.dtos.SignUpRequest;
import com.vaggelis.UserManagementSystem.dtos.UpdateRequest;
import com.vaggelis.UserManagementSystem.exceptions.UserAlreadyExistsException;
import com.vaggelis.UserManagementSystem.exceptions.UserNotFoundException;
import com.vaggelis.UserManagementSystem.models.User;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface IUsersCrudService {
    User insertUser(SignUpRequest request) throws UserAlreadyExistsException;
    User updateUser(UpdateRequest request) throws UserNotFoundException;
    User deleteUser(Long id) throws UserNotFoundException;
    List<User> findAll() throws Exception;
    User findUser(String email) throws UserNotFoundException;
    User updateYourUser(UpdateRequest request, Long targetUserId, Long currentUserId) throws UserNotFoundException;
    User deleteYourUser(Long targetUserId, Long currentUserId) throws UserNotFoundException;
}

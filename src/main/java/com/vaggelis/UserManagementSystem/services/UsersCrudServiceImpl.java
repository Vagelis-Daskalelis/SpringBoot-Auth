package com.vaggelis.UserManagementSystem.services;

import com.vaggelis.UserManagementSystem.dtos.SignUpRequest;
import com.vaggelis.UserManagementSystem.dtos.UpdateRequest;
import com.vaggelis.UserManagementSystem.exceptions.UserAlreadyExistsException;
import com.vaggelis.UserManagementSystem.exceptions.UserNotFoundException;
import com.vaggelis.UserManagementSystem.maps.Map;
import com.vaggelis.UserManagementSystem.models.Role;
import com.vaggelis.UserManagementSystem.models.Status;
import com.vaggelis.UserManagementSystem.models.User;
import com.vaggelis.UserManagementSystem.repositories.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersCrudServiceImpl implements IUsersCrudService{

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // inserts a new user
    @Override
    public User insertUser(SignUpRequest request) throws UserAlreadyExistsException {
        User user;

        try {
            Optional<User> userFind = userRepository.findByEmail(request.getEmail());

            if (userFind.isPresent()) throw new UserAlreadyExistsException(request.getEmail());

            user = Map.mapFromInsertUser(request, passwordEncoder);

            userRepository.save(user);


        } catch (UserAlreadyExistsException e) {
            throw e;
        }
        return user;

    }

    // updated an user
    @Override
    public User updateUser(UpdateRequest request) throws UserNotFoundException {
        User user;
        User updatedUser;

        try {
            user = userRepository.findUserById(request.getId());
            if (user == null) throw new UserNotFoundException(User.class, request.getId());

            updatedUser = Map.mapFromUpdateUser(request, passwordEncoder);

            userRepository.save(updatedUser);
        } catch (UserNotFoundException e) {
            throw e;
        }
        return updatedUser;
    }

    // deletes an user
    @Override
    public User deleteUser(Long id) throws UserNotFoundException {
        User user = null;

        try {
            user = userRepository.findUserById(id);
            if (user == null) throw new UserNotFoundException(User.class, id);
            userRepository.delete(user);
        } catch (UserNotFoundException e) {
            throw e;
        }
        return user;

    }

    // finds all users
    @Override
    public List<User> findAll() throws Exception {
        return userRepository.findAll();
    }

    // find a user from his email
    @Override
    public User findUser(String email) throws UserNotFoundException {
        User user;

        try {
            user = userRepository.findUserByEmail(email);
            if (user == null) throw new UserNotFoundException(User.class, user.getId());
        } catch (Exception e) {
            throw e;
        }
        return user;
    }

    // updates the user that you are logged in
    @Override
    public User updateYourUser(UpdateRequest request, Long targetUserId, Long currentUserId) throws UserNotFoundException {
        if (!targetUserId.equals(currentUserId)){
            throw new SecurityException("You are not authorized to update this user.");
        }
        User targetUser = null;
        try {
            targetUser = userRepository.findById(targetUserId)
                    .orElseThrow(() -> new UserNotFoundException(User.class, targetUserId));

            targetUser.setUname(request.getUname());
            targetUser.setEmail(request.getEmail());
            targetUser.setPassword(passwordEncoder.encode(request.getPassword()));

            userRepository.save(targetUser);
        } catch (UserNotFoundException e) {
            throw e;
        }

        return targetUser;
    }

    // deletes the user that you are logged in
    @Override
    public User deleteYourUser(Long targetUserId, Long currentUserId) throws UserNotFoundException {
        if (!targetUserId.equals(currentUserId)){
            throw new SecurityException("You are not authorized to update this user.");
        }
        try {
            User targetUser = userRepository.findById(targetUserId)
                    .orElseThrow(() -> new UserNotFoundException(User.class, targetUserId));

            userRepository.delete(targetUser);
            return targetUser;
        } catch (UserNotFoundException e) {
            throw e;
        }

    }
}

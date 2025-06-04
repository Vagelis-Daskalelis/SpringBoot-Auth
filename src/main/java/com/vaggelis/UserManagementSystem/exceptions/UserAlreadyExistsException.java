package com.vaggelis.UserManagementSystem.exceptions;

public class UserAlreadyExistsException extends Exception{

    public UserAlreadyExistsException(String name){
        super("Entity already exists");
    }
}

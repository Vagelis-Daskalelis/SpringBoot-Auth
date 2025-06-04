package com.vaggelis.UserManagementSystem.services;

public interface IPasswordResetService {

    void createPasswordResetToken(String email);
    void resetPassword(String token, String newPassword);
}

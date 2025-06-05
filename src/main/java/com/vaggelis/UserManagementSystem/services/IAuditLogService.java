package com.vaggelis.UserManagementSystem.services;

import com.vaggelis.UserManagementSystem.models.AuditLog;

import java.util.List;

public interface IAuditLogService {
    void log(String username, String action, String entityName);

    List<AuditLog> findAll();
}

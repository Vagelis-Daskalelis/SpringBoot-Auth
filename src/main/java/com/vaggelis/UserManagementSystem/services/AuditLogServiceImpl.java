package com.vaggelis.UserManagementSystem.services;

import com.vaggelis.UserManagementSystem.models.AuditLog;
import com.vaggelis.UserManagementSystem.repositories.IAuditLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements IAuditLogService{
    private final IAuditLogRepository auditLogRepository;


    // creates an log
    @Transactional
    @Override
    public void log(String username, String action, String entityName) {
        try {
            AuditLog log = new AuditLog();
            log.setUsername(username);
            log.setAction(action);
            log.setEntityName(entityName);
            log.setTimeStamp(LocalDateTime.now());
            auditLogRepository.save(log);
        } catch (Exception e) {
            // Log the error to see if something goes wrong
            e.printStackTrace();
        }
    }

    // finds all logs
    @Override
    public List<AuditLog> findAll() {
        return auditLogRepository.findAll();
    }
}

package com.vaggelis.UserManagementSystem.repositories;

import com.vaggelis.UserManagementSystem.models.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAuditLogRepository extends JpaRepository<AuditLog, Long> {
}

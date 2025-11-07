package com.example.SecuritySpring.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SecuritySpring.model.AuditLog;

import java.util.List;
 
public interface AuditRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findAllByOrderByTimestampDesc();
}
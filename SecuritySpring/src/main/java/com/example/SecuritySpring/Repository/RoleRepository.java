package com.example.SecuritySpring.Repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SecuritySpring.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}


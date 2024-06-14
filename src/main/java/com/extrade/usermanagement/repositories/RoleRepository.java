package com.extrade.usermanagement.repositories;

import com.extrade.usermanagement.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleCode(String roleCode);
}

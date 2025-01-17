package com.devteria.demo.repository;

import com.devteria.demo.entity.Permission;
import com.devteria.demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}

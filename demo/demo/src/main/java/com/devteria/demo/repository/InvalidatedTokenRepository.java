package com.devteria.demo.repository;

import com.devteria.demo.entity.InvalidatedToken;
import com.devteria.demo.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
}

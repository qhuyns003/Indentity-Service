package com.devteria.demo.repository;

import com.devteria.demo.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepositoryInterface extends JpaRepository<UserEntity, String> {
    List<UserEntity> findAll();
    boolean existsByUsername(String username);
    Optional<UserEntity> findByUsername(String username);
}

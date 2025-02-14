package com.devteria.demo.configuration;

import java.util.HashSet;
import java.util.Set;

import com.devteria.demo.entity.Permission;
import com.devteria.demo.entity.Role;
import com.devteria.demo.exception.AppException;
import com.devteria.demo.exception.ErrorCode;
import com.devteria.demo.repository.PermissionRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.devteria.demo.entity.UserEntity;
import com.devteria.demo.repository.RoleRepository;
import com.devteria.demo.repository.UserRepositoryInterface;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplicationConfig {

    final RoleRepository roleRepository;
    final PasswordEncoder passwordEncoder;
    final PermissionRepository permissionRepository;

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepositoryInterface userRepository) {
        log.info("Application started...");
        return args -> {
            if(!roleRepository.existsById(com.devteria.demo.enums.Role.ADMIN.name())){
                Permission permission = Permission.builder()
                        .name("CREATE_POST")
                        .description("create post")
                        .build();
                permissionRepository.save(permission);
                Set<Permission> permissions = new HashSet<>();
                permissions.add(permission);
                Role role = Role.builder()
                        .name(com.devteria.demo.enums.Role.ADMIN.name())
                        .description("Administrator")
                        .permissions(permissions)
                        .build();
                roleRepository.save(role);
            }

            if(!roleRepository.existsById(com.devteria.demo.enums.Role.USER.name())){
                Permission permission = Permission.builder()
                        .name("CREATE_POST")
                        .description("create post")
                        .build();
                permissionRepository.save(permission);
                Set<Permission> permissions = new HashSet<>();
                permissions.add(permission);
                Role role = Role.builder()
                        .name(com.devteria.demo.enums.Role.USER.name())
                        .description("User")
                        .permissions(permissions)
                        .build();
                roleRepository.save(role);
            }

            if (userRepository.findByUsername("admin").isEmpty()) {
                Set<com.devteria.demo.entity.Role> roles = new HashSet<>();
                                Role role =
                 roleRepository.findById(com.devteria.demo.enums.Role.ADMIN.name()).orElseThrow(()-> new
                         AppException(ErrorCode.ROLE_NOT_FOUND));
                                roles.add(role);
                UserEntity user = UserEntity.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                                                .roles(roles)
                        .build();
                userRepository.save(user);
            }
        };
    }
}

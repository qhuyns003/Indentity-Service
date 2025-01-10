package com.devteria.demo.configuration;

import com.devteria.demo.entity.UserEntity;
import com.devteria.demo.enums.Role;
import com.devteria.demo.repository.UserRepositoryInterface;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplicationConfig {

    final PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner applicationRunner(UserRepositoryInterface userRepository) {

        return args -> {
            if(userRepository.findByUsername("admin").isEmpty()){
                Set<String> roles = new HashSet<>();
                roles.add(Role.ADMIN.name());
                UserEntity user = UserEntity.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();
                userRepository.save(user);

            };

        };
    }
}

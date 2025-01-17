package com.devteria.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity {
     @Id
     @GeneratedValue(strategy = GenerationType.UUID)
      String id;
      String username;
      String password;
      String firstName;
      String lastName;
      LocalDate dob;

      @ManyToMany
      Set<Role> roles;

}

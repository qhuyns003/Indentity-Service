package com.devteria.demo.dto.request;

import com.devteria.demo.entity.Role;
import com.devteria.demo.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequest {

     @Size(min = 5, message = "PASSWORD_INVALID")
     String password;
     String firstName;
     String lastName;
     @DobConstraint(min = 18, message = "DOB_INVALID")
     LocalDate dob;

     Set<String> roles;
}

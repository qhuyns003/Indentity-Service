package com.devteria.demo.dto.request;

import java.time.LocalDate;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateRequest {

    @Size(min = 8, message = "USERNAME_INVALID")
     String username;

    @Size(min = 8, message = "PASSWORD_INVALID")
     String password;
     String firstName;
     String lastName;
     LocalDate dob;

}

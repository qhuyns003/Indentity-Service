package com.devteria.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequest {

     String password;
     String firstName;
     String lastName;
     LocalDate dob;

}

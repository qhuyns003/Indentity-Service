package com.devteria.demo.controller;

import com.devteria.demo.dto.request.AuthenticationRequest;
import com.devteria.demo.dto.request.IntrospectRequest;
import com.devteria.demo.dto.response.ApiResponse;
import com.devteria.demo.dto.response.AuthenticationResponse;
import com.devteria.demo.dto.response.IntrospectResponse;
import com.devteria.demo.service.impl.AuthenticationImpl;
import com.devteria.demo.service.impl.UserServiceImpl;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationImpl authentication;

    @PostMapping("/log-in")
    ApiResponse<AuthenticationResponse> authenticationUser(@RequestBody AuthenticationRequest authenticationRequest) {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(new AuthenticationResponse().builder()
                        .token(authentication.authenticateUser(authenticationRequest)).build()).build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspectUser(@RequestBody IntrospectRequest introspectRequest) throws ParseException, JOSEException {
        return ApiResponse.<IntrospectResponse>builder()
                .result(authentication.introspectUser(introspectRequest)).build();
    }

}

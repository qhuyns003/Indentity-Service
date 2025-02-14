package com.devteria.demo.controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.*;

import com.devteria.demo.dto.request.AuthenticationRequest;
import com.devteria.demo.dto.request.IntrospectRequest;
import com.devteria.demo.dto.request.LogoutRequest;
import com.devteria.demo.dto.request.RefreshTokenRequest;
import com.devteria.demo.dto.response.ApiResponse;
import com.devteria.demo.dto.response.AuthenticationResponse;
import com.devteria.demo.dto.response.IntrospectResponse;
import com.devteria.demo.dto.response.RefreshTokenResponse;
import com.devteria.demo.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authentication;

    @PostMapping("/log-in")
    ApiResponse<AuthenticationResponse> authenticationUser(@RequestBody AuthenticationRequest authenticationRequest) {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(AuthenticationResponse.builder()
                        .token(authentication.authenticateUser(authenticationRequest))
                        .build())
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<String> logout(@RequestBody LogoutRequest logoutRequest) throws ParseException, JOSEException {
        authentication.logout(logoutRequest.getToken());
        return ApiResponse.<String>builder().result("successful").build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspectUser(@RequestBody IntrospectRequest introspectRequest)
            throws ParseException, JOSEException {
        return ApiResponse.<IntrospectResponse>builder()
                .result(authentication.introspectUser(introspectRequest))
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest)
            throws ParseException, JOSEException {
        return ApiResponse.<RefreshTokenResponse>builder()
                .result(authentication.refreshToken(refreshTokenRequest))
                .build();
    }
}

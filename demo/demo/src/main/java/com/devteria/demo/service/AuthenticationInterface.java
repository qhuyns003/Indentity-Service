package com.devteria.demo.service;

import com.devteria.demo.dto.request.AuthenticationRequest;
import com.devteria.demo.dto.request.IntrospectRequest;
import com.devteria.demo.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationInterface {
    String authenticateUser(AuthenticationRequest authenticationRequest);
    IntrospectResponse introspectUser(IntrospectRequest introspectRequest) throws JOSEException, ParseException;
}

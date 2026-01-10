package com.khmermarket.khmermarket.authentication.service;

import com.khmermarket.khmermarket.authentication.request.AuthenticationRequest;
import com.khmermarket.khmermarket.authentication.request.RegisterRequest;
import com.khmermarket.khmermarket.authentication.response.AuthenticationResponse;
import com.khmermarket.khmermarket.domain.dto.UserDto;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    UserDto register(RegisterRequest request);
}

package com.upskilling.experiment.service;

import org.springframework.http.ResponseEntity;

import com.upskilling.experiment.dto.request.RegisterRequestDTO;

public interface AuthService {

    ResponseEntity<?> authenticate(String username, String password);

    ResponseEntity<String> registerUser(RegisterRequestDTO registerRequest);
}

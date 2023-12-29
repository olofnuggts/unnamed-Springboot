package com.yk.unnamed.controller;

import com.yk.unnamed.exceptions.AuthenticationException;
import com.yk.unnamed.repository.UserRepository;
import com.yk.unnamed.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
// import org.postgresql.plugin.AuthenticationRequestType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authService;
    private final UserRepository repository;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {

        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request,
            @RequestHeader("Authorization") String bearerToken) {
        AuthenticationResponse result = authService.authenticate(request, bearerToken);
        if (bearerToken == null) {
            return ResponseEntity.status(401).build();
        } else if (result == null) {
            return ResponseEntity.status(401).build();

        }
        return ResponseEntity.ok(result);
    }
}

package com.yk.unnamed.service;

import com.yk.unnamed.controller.AuthenticationRequest;
import com.yk.unnamed.controller.AuthenticationResponse;
import com.yk.unnamed.controller.RegisterRequest;
import com.yk.unnamed.exceptions.AuthenticationException;
import com.yk.unnamed.model.Role;
import com.yk.unnamed.model.Token;
import com.yk.unnamed.model.TokenType;
import com.yk.unnamed.model.User;
import com.yk.unnamed.repository.TokenRepository;
import com.yk.unnamed.repository.UserRepository;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.validator.routines.EmailValidator;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public boolean validEmail(RegisterRequest request) {
        EmailValidator emailValidator = EmailValidator.getInstance();
        if (!emailValidator.isValid(request.getEmail())) {
            return false;
        }
        try {
            InternetAddress emailAddr = new InternetAddress(request.getEmail());
            emailAddr.validate();
            return true;
        } catch (AddressException e) {
            return false;
        }
    }

    public AuthenticationResponse register(RegisterRequest request) {
        if (!validEmail(request)) {
            throw new AuthenticationException("Invalid email address.");
        }
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request, String bearerToken) {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        } catch (Exception e) {
            throw new AuthenticationException("Invalid email or password.");
        }
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthenticationException("User not found."));
        var storedToken = tokenRepository.findByToken(bearerToken.substring(7)).orElseThrow(
                () -> new AuthenticationException("Invalid token."));

        if (storedToken.getUser().getId() != user.getId()) {
            throw new AuthenticationException("Token does not match user.");
        }

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);

        return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

}

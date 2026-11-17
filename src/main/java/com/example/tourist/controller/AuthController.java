package com.example.tourist.controller;

import com.example.tourist.dto.TokenRequest;
import com.example.tourist.dto.TokenResponse;
import com.example.tourist.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtService jwtService;
    private final String demoUsername;
    private final String demoPassword;

    public AuthController(JwtService jwtService,
                          @Value("${app.demo.username}") String demoUsername,
                          @Value("${app.demo.password}") String demoPassword) {
        this.jwtService = jwtService;
        this.demoUsername = demoUsername;
        this.demoPassword = demoPassword;
    }

    @PostMapping("/token")
    @Operation(summary = "Generate a demo JWT", description = "Demo-only login endpoint. Replace with your real hotel identity provider in production.")
    public TokenResponse generateToken(@Valid @RequestBody TokenRequest request) {
        if (!demoUsername.equals(request.username()) || !demoPassword.equals(request.password())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
        return new TokenResponse("Bearer", jwtService.generateToken(request.username()),
                jwtService.getExpirationSeconds());
    }
}

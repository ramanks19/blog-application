package com.springboot.blog.springbootblogrestapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.blog.springbootblogrestapi.payload.JWTAuthResponse;
import com.springboot.blog.springbootblogrestapi.payload.LoginDTO;
import com.springboot.blog.springbootblogrestapi.payload.RegisterDTO;
import com.springboot.blog.springbootblogrestapi.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@Tag(
        name = "Authentication Controller",
        description = "APIs for user authentication and registration."
)
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //Build Login REST API for Database Authentication
    // @PostMapping(value = {"/login", "/signin"})
    // public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO){
    //     String response = authService.login(loginDTO);
    //     return ResponseEntity.ok(response);
    // }
    
    //Build Login REST API
    @PostMapping(value = {"/login", "/signin"})
    @Operation(
            summary = "User Login",
            description = "Authenticate and generate JWT token for user logins"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Login successfully."
    )
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDTO loginDTO){
        String token = authService.login(loginDTO);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }

    //Build Register REST API
    @PostMapping(value = {"/register", "/signup"})
    @Operation(
            summary = "User Registration.",
            description = "Register a new user."
    )
    @ApiResponse(
            responseCode = "201",
            description = "User registered successfully."
    )
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO){
        String response = authService.register(registerDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    } 
    
}

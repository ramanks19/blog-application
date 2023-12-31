package com.springboot.blog.springbootblogrestapi.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.blog.springbootblogrestapi.entity.Role;
import com.springboot.blog.springbootblogrestapi.entity.User;
import com.springboot.blog.springbootblogrestapi.exception.BlogAPIException;
import com.springboot.blog.springbootblogrestapi.payload.LoginDTO;
import com.springboot.blog.springbootblogrestapi.payload.RegisterDTO;
import com.springboot.blog.springbootblogrestapi.repository.RoleRepository;
import com.springboot.blog.springbootblogrestapi.repository.UserRepository;
import com.springboot.blog.springbootblogrestapi.security.JWTTokenProvider;
import com.springboot.blog.springbootblogrestapi.service.AuthService;

import java.util.Set;
import java.util.HashSet;

@Service
public class AuthServiceImpl implements AuthService{

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JWTTokenProvider jwtTokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JWTTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDTO.getUserNameOrEmail(), loginDTO.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

//        return "User logged in Successfully!!!";
        String token = jwtTokenProvider.generateToken(authentication);
        
        return token; 
    }

    @Override
    public String register(RegisterDTO registerDTO) {
        //check if the username exists in the database
        if (userRepository.existsByUserName(registerDTO.getUserName())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Username already exists in the Database");
        }

        //check if the email exists in the database
        if (userRepository.existsByEmail(registerDTO.getEmail())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Email already exists in the Database");
        }
        
        User user = new User();
        user.setName(registerDTO.getName());
        user.setUserName(registerDTO.getUserName());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        // Set<Role> roles = new HashSet<>();
        // Role userRole = roleRepository.findByName("ROLE_USER").get();
        // roles.add(userRole);
        // user.setRoles(roles);

        // userRepository.save(user);
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(registerDTO.getRole()).orElseThrow(() -> 
                        new BlogAPIException(HttpStatus.BAD_REQUEST, "Role not found: " + registerDTO.getRole()));

        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);

        return "User registered successfully!!!";
    }
    
}

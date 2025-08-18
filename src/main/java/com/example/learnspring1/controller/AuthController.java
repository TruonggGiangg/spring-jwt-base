package com.example.learnspring1.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.learnspring1.domain.APIResponse;
import com.example.learnspring1.domain.dto.LoginDTO;
import com.example.learnspring1.domain.dto.ResponseLoginDTO;
import com.example.learnspring1.utils.SecurityUtil;

import jakarta.validation.Valid;

@RestController
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
    }

    @PostMapping("/login")
    public ResponseLoginDTO login(@Valid @RequestBody LoginDTO loginDTO) {
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

        String accessToken = this.securityUtil.createToken(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseLoginDTO(accessToken);
    }

}

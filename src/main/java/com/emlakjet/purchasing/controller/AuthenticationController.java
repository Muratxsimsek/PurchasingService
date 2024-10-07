package com.emlakjet.purchasing.controller;

import com.emlakjet.purchasing.controller.Dto.LoginRequest;
import com.emlakjet.purchasing.exception.NotAuthorizedException;
import com.emlakjet.purchasing.security.TokenService;
import com.emlakjet.purchasing.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private UserService userService;

    private TokenService tokenService;


    @Operation(summary = "Security Authorization", description = "you have to provide first name, last name, email, password, pay attention camel case typing!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bearer Token to use for endpoint"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, NotAuthorizedException {

        boolean authenticated = userService.authenticate(loginRequest);
        if (!authenticated) {
            throw new NotAuthorizedException();
        }

        String token = tokenService.generateToken(loginRequest.getFirstName(), loginRequest.getLastName(), loginRequest.getEmail());
        return token;
    }
}

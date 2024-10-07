package com.emlakjet.purchasing.controller.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(description = "Login Data Transfer Object")
@AllArgsConstructor
@Data
public class LoginRequest {

    @Schema(description = "First Name", example = "Murat")
    private String firstName;
    @Schema(description = "Last Name", example = "Simsek")
    private String lastName;
    @Schema(description = "Email", example = "muratxsimsek@gmail.com")
    private String email;
    @Schema(description = "Password", example = "*******")
    private String password;
}
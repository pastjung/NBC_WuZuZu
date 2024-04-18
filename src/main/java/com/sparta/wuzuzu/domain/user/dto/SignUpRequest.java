package com.sparta.wuzuzu.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignUpRequest {

    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String confirmPassword;

    @NotBlank
    private String address;

    @NotBlank
    private String userName;

    private String petName;

    private String petType;
}

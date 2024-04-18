package com.sparta.wuzuzu.domain.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminLoginRequest {

    @NotBlank
    private String adminEmail;
    @NotBlank
    private String password;
    private final LocalDateTime currentTime = LocalDateTime.now();
}

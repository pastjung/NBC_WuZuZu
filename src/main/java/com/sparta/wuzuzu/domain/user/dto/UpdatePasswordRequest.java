package com.sparta.wuzuzu.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdatePasswordRequest {

    private String currentPassword;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String confirmPassword;
}

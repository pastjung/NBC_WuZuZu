package com.sparta.wuzuzu.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MyInformUpdateRequest {

    private String currentPassword;

    @NotBlank
    private String address;

    @NotBlank
    private String userName;

    private String petName;

    private String petType;
}

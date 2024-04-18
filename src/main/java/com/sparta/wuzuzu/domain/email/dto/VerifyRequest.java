package com.sparta.wuzuzu.domain.email.dto;

import lombok.Getter;

@Getter
public class VerifyRequest {

    private String mail;
    private String verifyCode;
}

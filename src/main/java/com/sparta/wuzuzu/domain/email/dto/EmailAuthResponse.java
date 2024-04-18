package com.sparta.wuzuzu.domain.email.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailAuthResponse {

    private String verifiedEmail;
    private boolean isVerified;

    public EmailAuthResponse(String verifiedEmail){
        this.verifiedEmail = verifiedEmail;
    }
}

package com.sparta.wuzuzu.domain.user.dto;

import lombok.Getter;

@Getter
public class ReportUserRequest {

    private Long reportUserId;
    private String  reason;
}

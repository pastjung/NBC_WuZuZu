package com.sparta.wuzuzu.domain.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExceptionResponse {

    String msg;
    int httpCode;
}

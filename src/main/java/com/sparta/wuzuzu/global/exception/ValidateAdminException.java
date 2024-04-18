package com.sparta.wuzuzu.global.exception;

public class ValidateAdminException extends RuntimeException {

    public ValidateAdminException() {
        super("권한이 없습니다.");
    }

}

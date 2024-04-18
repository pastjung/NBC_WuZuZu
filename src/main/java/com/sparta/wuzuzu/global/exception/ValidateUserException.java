package com.sparta.wuzuzu.global.exception;

public class ValidateUserException extends RuntimeException {

    public ValidateUserException() {
        super("권한이 없습니다.");
    }

    public ValidateUserException(String s) {
        super(s);
    }

}

package com.sparta.wuzuzu.global.exception;

public class NotFoundCommunityPostException extends RuntimeException {

    public NotFoundCommunityPostException() {
        super("게시글이 존재하지 않습니다");
    }

}

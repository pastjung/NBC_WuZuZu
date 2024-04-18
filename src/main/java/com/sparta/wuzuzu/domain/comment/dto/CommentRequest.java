package com.sparta.wuzuzu.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class CommentRequest {
    @NotBlank
    @Pattern(regexp = "^[a-z|A-Z|ㄱ-ㅎ|가-힣| ]*${2,200}", message = "2자 이상 200자 이하 한글과 영어만 가능합니다")
    private String contents;

}

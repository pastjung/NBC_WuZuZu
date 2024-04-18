package com.sparta.wuzuzu.domain.community_posts.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommunityPostRequest {

    @NotBlank(message = "제목을 입력하세요")
    private String title;

    @NotBlank(message = "내용을 입력하세요")
    private String content;

    @NotBlank(message = "카테고리를 입력하세요")
    private String categoryName;

}

package com.sparta.wuzuzu.domain.community_posts.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommunityCategoryRequest {

    @NotBlank(message = "카테고리명을 입력하세요")
    private String name;

}

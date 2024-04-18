package com.sparta.wuzuzu.domain.community_posts.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostLikeResponse {

    private Boolean isLiked;

    public PostLikeResponse(Boolean isLiked) {
        this.isLiked = isLiked;
    }
}



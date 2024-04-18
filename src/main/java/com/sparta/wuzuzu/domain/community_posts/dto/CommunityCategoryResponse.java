package com.sparta.wuzuzu.domain.community_posts.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.wuzuzu.domain.community_posts.entity.CommunityCategory;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommunityCategoryResponse {

    private String name;
    private boolean status;

    public CommunityCategoryResponse(CommunityCategory category) {

        this.name = category.getName();
        this.status = category.getStatus();
    }

}

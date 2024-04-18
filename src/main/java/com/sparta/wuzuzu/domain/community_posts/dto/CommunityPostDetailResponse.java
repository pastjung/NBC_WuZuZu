package com.sparta.wuzuzu.domain.community_posts.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommunityPostDetailResponse {

    private Long communityPostId;
    private String title;
    private String username;
    private String categoryName;
    private String contents;
    private Long likeCount;
    private Long views;
    private Integer comments;
    private String image;
    private Boolean isLiked;
}

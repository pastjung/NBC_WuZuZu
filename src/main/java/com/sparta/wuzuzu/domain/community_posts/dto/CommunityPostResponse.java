package com.sparta.wuzuzu.domain.community_posts.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.wuzuzu.domain.community_posts.entity.CommunityPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommunityPostResponse {

    private Long communityPostId;
    private String title;
    private String username;
    private String categoryName;
    private String contents;
    private Long likeCount;
    private Long views;
    private Integer comments;
    private String image;

    public CommunityPostResponse(CommunityPost communityPost, String userName, String image) {
        this.communityPostId = communityPost.getCommunityPostId();
        this.title = communityPost.getTitle();
        this.username = userName;
        this.categoryName = communityPost.getCategory().getName();
        this.contents = communityPost.getContent();
        this.likeCount = communityPost.getLikeCount();
        this.views = communityPost.getViews();
        this.comments = communityPost.getComments().intValue();
        this.image = image;
    }
}

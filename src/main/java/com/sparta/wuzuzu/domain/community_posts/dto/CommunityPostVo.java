package com.sparta.wuzuzu.domain.community_posts.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class CommunityPostVo {
    private String title;
    private String username;
    private String contents;
    private Long likeCount;
    private Long views;
    private List<String> imageUrls;

}

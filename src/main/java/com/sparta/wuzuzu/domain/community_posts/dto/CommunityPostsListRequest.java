package com.sparta.wuzuzu.domain.community_posts.dto;

import com.sparta.wuzuzu.global.dto.request.ListRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Setter
public class CommunityPostsListRequest extends ListRequest {

    private String title;
    private String contents;
    private String keyword;
    private String categoryName;

    @Builder
    public CommunityPostsListRequest(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
    public CommunityPostsListRequest() {
        this.title = "";
        this.contents = "";
        this.keyword = "";
        this.categoryName = "";
    }

}

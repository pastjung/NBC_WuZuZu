package com.sparta.wuzuzu.domain.community_posts.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.wuzuzu.global.dto.response.ListResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommunityPostListResponse extends ListResponse {

    private List<CommunityPostResponse> postList;

}



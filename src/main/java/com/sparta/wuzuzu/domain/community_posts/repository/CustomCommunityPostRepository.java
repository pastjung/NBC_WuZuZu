package com.sparta.wuzuzu.domain.community_posts.repository;

import com.sparta.wuzuzu.domain.community_posts.dto.CommunityPostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomCommunityPostRepository {

    Page<CommunityPostResponse> findByConditions(String keyword, String categoryName, Pageable pageable);

}

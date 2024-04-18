package com.sparta.wuzuzu.domain.common.image.repository;

import com.sparta.wuzuzu.domain.common.image.entity.Image;
import com.sparta.wuzuzu.domain.community_posts.entity.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Image findByCommunityPost(CommunityPost communityPost);
}


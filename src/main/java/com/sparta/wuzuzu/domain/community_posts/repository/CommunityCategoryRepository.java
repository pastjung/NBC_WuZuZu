package com.sparta.wuzuzu.domain.community_posts.repository;

import com.sparta.wuzuzu.domain.community_posts.entity.CommunityCategory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityCategoryRepository extends JpaRepository<CommunityCategory, Long> {

    Optional<CommunityCategory> findByNameEquals(String name);
}

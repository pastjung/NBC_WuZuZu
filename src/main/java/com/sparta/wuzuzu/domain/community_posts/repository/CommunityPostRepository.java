package com.sparta.wuzuzu.domain.community_posts.repository;

import com.sparta.wuzuzu.domain.community_posts.entity.CommunityPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {

    @Query("SELECT p FROM CommunityPost p WHERE p.title LIKE %:keyword%")
    Page<CommunityPost> findByTitleContaining(String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM community_posts p WHERE MATCH(p.title) AGAINST(:keyword IN BOOLEAN MODE)",
        countQuery = "SELECT count(*) FROM community_posts p WHERE MATCH(p.title) AGAINST(:keyword IN BOOLEAN MODE)",
        nativeQuery = true)
    Page<CommunityPost> findByTitleWithFullTextSearch(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM CommunityPost p WHERE p.category.name = :categoryName")
    Page<CommunityPost> findByCategoryName(@Param("categoryName") String categoryName, Pageable pageable);

}

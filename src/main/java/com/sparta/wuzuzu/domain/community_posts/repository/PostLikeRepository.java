package com.sparta.wuzuzu.domain.community_posts.repository;

import com.sparta.wuzuzu.domain.community_posts.entity.CommunityPost;
import com.sparta.wuzuzu.domain.community_posts.entity.PostLike;
import com.sparta.wuzuzu.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByUserAndCommunityPost(User user, CommunityPost communityPost);

}

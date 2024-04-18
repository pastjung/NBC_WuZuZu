package com.sparta.wuzuzu.domain.comment.repository;

import com.sparta.wuzuzu.domain.comment.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByCommunityPost_CommunityPostIdOrderByCreatedAtDesc(Long communityPostsId);
}

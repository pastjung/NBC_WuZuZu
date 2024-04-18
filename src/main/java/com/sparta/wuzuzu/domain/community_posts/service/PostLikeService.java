package com.sparta.wuzuzu.domain.community_posts.service;

import com.sparta.wuzuzu.domain.community_posts.dto.PostLikeResponse;
import com.sparta.wuzuzu.domain.community_posts.entity.CommunityPost;
import com.sparta.wuzuzu.domain.community_posts.entity.PostLike;
import com.sparta.wuzuzu.domain.community_posts.repository.CommunityPostRepository;
import com.sparta.wuzuzu.domain.community_posts.repository.PostLikeRepository;
import com.sparta.wuzuzu.domain.user.entity.User;
import com.sparta.wuzuzu.global.exception.NotFoundCommunityPostException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final CommunityPostRepository communityPostRepository;
    private final PostLikeRepository post_likeRepository;

    @Transactional
    public PostLikeResponse createLike(Long communityPostId, User user) {
        CommunityPost post = communityPostRepository.findById(communityPostId)
            .orElseThrow(() -> new NotFoundCommunityPostException());

        // 이미 좋아요가 존재하면 삭제
        if (post_likeRepository.findByUserAndCommunityPost(user, post).isPresent()) {
            PostLike like = post_likeRepository.findByUserAndCommunityPost(user, post).get();
            like.removeLike(post);//게시글 좋아요 리스트에서 제거.
            post.removeLike();
            communityPostRepository.save(post);
            return new PostLikeResponse(false);
        } else {
            PostLike like = new PostLike(user, post);
            like.addLike(post); // 게시글에 좋아요 추가(리스트 형태)
            post.addLike();
            communityPostRepository.save(post);

            return new PostLikeResponse(true);
        }
    }

}

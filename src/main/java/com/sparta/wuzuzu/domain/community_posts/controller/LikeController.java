package com.sparta.wuzuzu.domain.community_posts.controller;


import com.sparta.wuzuzu.domain.community_posts.dto.PostLikeResponse;
import com.sparta.wuzuzu.domain.community_posts.service.PostLikeService;
import com.sparta.wuzuzu.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/communityposts")
public class LikeController {

    private final PostLikeService likeService;

    @PostMapping("/{communityPostId}/likes")
    public ResponseEntity<PostLikeResponse> createLike(@PathVariable Long communityPostId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok()
            .body(likeService.createLike(communityPostId, userDetails.getUser()));
    }

}

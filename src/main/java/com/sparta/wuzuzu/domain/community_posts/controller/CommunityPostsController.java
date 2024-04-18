package com.sparta.wuzuzu.domain.community_posts.controller;

import com.sparta.wuzuzu.domain.community_posts.dto.CommunityPostDetailResponse;
import com.sparta.wuzuzu.domain.community_posts.dto.CommunityPostListRequest;
import com.sparta.wuzuzu.domain.community_posts.dto.CommunityPostListResponse;
import com.sparta.wuzuzu.domain.community_posts.dto.CommunityPostRequest;
import com.sparta.wuzuzu.domain.community_posts.dto.CommunityPostResponse;
import com.sparta.wuzuzu.domain.community_posts.service.CommunityPostsService;
import com.sparta.wuzuzu.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/v1/communityposts")
@RestController
@RequiredArgsConstructor
public class CommunityPostsController {

    private final CommunityPostsService communityPostsService;

    @PostMapping
    public ResponseEntity<CommunityPostResponse> saveCommunityPosts(
        @RequestPart(value = "communityPost") @Valid CommunityPostRequest communityPostRequest,
        @RequestPart(value = "image", required = false) MultipartFile image,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommunityPostResponse communityPostResponse = communityPostsService.saveCommunityPosts(
            communityPostRequest,
            image,
            userDetails.getUser());

        return new ResponseEntity<>(communityPostResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/{communityPostsId}")
    public ResponseEntity<CommunityPostResponse> updateCommunityPost(
        @Valid @RequestBody CommunityPostRequest communityPostRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long communityPostsId
    ) {
        CommunityPostResponse communityPostResponse = communityPostsService.updateCommunityPosts(
            communityPostRequest, userDetails.getUser().getUserId(), communityPostsId);

        return new ResponseEntity<>(communityPostResponse, HttpStatus.OK);
    }

    @PostMapping("/{communityPostsId}/multipart-files")
    public ResponseEntity<Void> uploadImage(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long communityPostsId,
        @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        communityPostsService.uploadImage(userDetails.getUser(), communityPostsId, image);
        return ResponseEntity.status(HttpStatus.OK.value()).build();
    }

    @DeleteMapping("/{communityPostId}/multipart-files/{key}")
    public ResponseEntity<Void> deleteImage(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long communityPostId,
        @PathVariable String key
    ) {
        communityPostsService.deleteImage(userDetails.getUser(), communityPostId, key);
        return ResponseEntity.status(HttpStatus.OK.value()).build();
    }

    @GetMapping("/search")
    public ResponseEntity<CommunityPostListResponse> searchCommunityPosts(
        @ModelAttribute CommunityPostListRequest request) {
        return ResponseEntity.ok(communityPostsService.searchCommunityPosts(request));
    }

    @GetMapping("/{communityPostsId}")
    public ResponseEntity<CommunityPostDetailResponse> readDetail(
        @PathVariable Long communityPostsId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok()
            .body(communityPostsService.getDetail(communityPostsId, userDetails.getUser()));
    }

    @DeleteMapping("/{communityPostsId}")
    public ResponseEntity<Void> deleteCommunityPosts(
        @PathVariable Long communityPostsId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        communityPostsService.deleteCommunityPost(communityPostsId, userDetails.getUser());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

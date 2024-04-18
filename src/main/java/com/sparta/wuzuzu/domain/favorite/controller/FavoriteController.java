package com.sparta.wuzuzu.domain.favorite.controller;

import com.sparta.wuzuzu.domain.common.dto.CommonResponse;
import com.sparta.wuzuzu.domain.favorite.dto.request.FavoriteRequest;
import com.sparta.wuzuzu.domain.favorite.dto.response.FavoriteResponse;
import com.sparta.wuzuzu.domain.favorite.service.FavoriteService;
import com.sparta.wuzuzu.global.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    // 즐겨찾기 추가
    @PostMapping
    public ResponseEntity<CommonResponse<String>> createFavorite(
        @RequestBody FavoriteRequest request,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        favoriteService.createFavorite(request, userDetails.getUser());
        return CommonResponse.ofDataWithHttpStatus("즐겨찾기 추가 성공", HttpStatus.CREATED);
    }

    // 즐겨찾기 삭제
    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<CommonResponse<String>> deleteFavorite(@PathVariable Long favoriteId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        favoriteService.deleteFavorite(favoriteId, userDetails.getUser());
        return CommonResponse.ofDataWithHttpStatus("즐겨찾기 삭제 성공", HttpStatus.NO_CONTENT);
    }

    // 즐겨찾기 전체 조회
    @GetMapping
    public ResponseEntity<CommonResponse<List<FavoriteResponse>>> getAllFavorite(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<FavoriteResponse> favoriteResponseList = favoriteService.getAllFavorite(
            userDetails.getUser());
        return CommonResponse.ofDataWithHttpStatus(favoriteResponseList, HttpStatus.OK);
    }

    // 즐겨찾기 카테고리별 조회
    @GetMapping("/{category}")
    public ResponseEntity<CommonResponse<List<FavoriteResponse>>> getFavoriteCategory(
        @PathVariable String category, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<FavoriteResponse> favoriteResponseList = favoriteService.getFavoriteCategory(category,
            userDetails.getUser());
        return CommonResponse.ofDataWithHttpStatus(favoriteResponseList, HttpStatus.OK);
    }
}

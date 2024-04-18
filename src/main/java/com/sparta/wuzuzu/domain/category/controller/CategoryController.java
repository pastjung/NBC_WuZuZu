package com.sparta.wuzuzu.domain.category.controller;

import com.sparta.wuzuzu.domain.category.dto.CategoryRequest;
import com.sparta.wuzuzu.domain.category.dto.CategoryResponse;
import com.sparta.wuzuzu.domain.category.service.CategoryService;
import com.sparta.wuzuzu.domain.common.dto.CommonResponse;
import com.sparta.wuzuzu.domain.user.entity.User;
import com.sparta.wuzuzu.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Void> createCategory(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody CategoryRequest requestDto
    ){
        categoryService.createCategory(userDetails.getUser(), requestDto);
        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<CategoryResponse>>> getCategory(
    ){
        List<CategoryResponse> response = categoryService.getCategory();
        return CommonResponse.ofDataWithHttpStatus(response, HttpStatus.OK);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Void> updateCategory(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long categoryId,
        @Valid @RequestBody CategoryRequest requestDto
    ){
        categoryService.updatePost(userDetails.getUser(), categoryId, requestDto);
        return ResponseEntity.status(HttpStatus.OK.value()).build();
    }

    @PostMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long categoryId
    ){
        categoryService.deletePost(userDetails.getUser(), categoryId);
        return ResponseEntity.status(HttpStatus.OK.value()).build();
    }
}

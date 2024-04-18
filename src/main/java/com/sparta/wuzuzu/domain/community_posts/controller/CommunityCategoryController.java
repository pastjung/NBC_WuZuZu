package com.sparta.wuzuzu.domain.community_posts.controller;


import com.sparta.wuzuzu.domain.community_posts.dto.CommunityCategoryRequest;
import com.sparta.wuzuzu.domain.community_posts.dto.CommunityCategoryResponse;
import com.sparta.wuzuzu.domain.community_posts.service.CommunityCategoryService;
import com.sparta.wuzuzu.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/communitycategories")
public class CommunityCategoryController {

    private final CommunityCategoryService community_CategoryService;

    @PostMapping("/admin")
    public ResponseEntity<CommunityCategoryResponse> createCategory(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody CommunityCategoryRequest communityCategoryRequest
    )
    {
        CommunityCategoryResponse response = community_CategoryService.createCategory(userDetails.getUser(), communityCategoryRequest);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CommunityCategoryResponse>> getCategory(
    ){
        List<CommunityCategoryResponse> response = community_CategoryService.getCategory();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/admin/{categoryId}")
    public ResponseEntity<Void> updateCategory(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long categoryId,
        @Valid @RequestBody CommunityCategoryRequest requestDto
    ){
        community_CategoryService.updateCategory(userDetails.getUser(), categoryId, requestDto);
        return ResponseEntity.status(HttpStatus.OK.value()).build();
    }

    @PostMapping("/admin/{categoryId}")
    public ResponseEntity<Void> deleteCategory (@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long categoryId){
        community_CategoryService.deleteCategory(userDetails.getUser(), categoryId);
        return ResponseEntity.status(HttpStatus.OK.value()).build();
    }

}

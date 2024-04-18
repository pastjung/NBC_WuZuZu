package com.sparta.wuzuzu.domain.dibs.controller;

import com.sparta.wuzuzu.domain.common.dto.CommonResponse;
import com.sparta.wuzuzu.domain.dibs.dto.DibsVo;
import com.sparta.wuzuzu.domain.dibs.service.DibsService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DibsController {
    private final DibsService dibsService;

    @PostMapping("/api/v1/salePosts/{salePostId}/dibs")
    public ResponseEntity<Void> createDibs(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long salePostId
    ){
        dibsService.createDibs(userDetails.getUser(), salePostId);
        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }

    @GetMapping("/api/v1/dibs")
    public ResponseEntity<CommonResponse<List<DibsVo>>> getOrders(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        List<DibsVo> dibsResponseList = dibsService.getDibs(userDetails.getUser());
        return CommonResponse.ofDataWithHttpStatus(dibsResponseList, HttpStatus.OK);
    }

    @DeleteMapping("/api/v1/salePosts/{salePostId}/dibs")
    public ResponseEntity<Void> deleteDibs(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long salePostId
    ){
        dibsService.deleteDibs(userDetails.getUser(), salePostId);
        return ResponseEntity.status(HttpStatus.OK.value()).build();
    }
}


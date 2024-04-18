package com.sparta.wuzuzu.domain.spot.controller;

import com.sparta.wuzuzu.domain.common.dto.CommonResponse;
import com.sparta.wuzuzu.domain.spot.dto.response.SpotDetailResponse;
import com.sparta.wuzuzu.domain.spot.dto.response.CategorySpotResponse;
import com.sparta.wuzuzu.domain.spot.service.SpotService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/spots")
public class SpotController {

    private final SpotService SpotService;

    // 카테고리별 조회
    @GetMapping("/{category}/{page}")
    public ResponseEntity<CommonResponse<List<CategorySpotResponse>>> getCatecorySpot(
        @PathVariable String category, @PathVariable Integer page) {
        List<CategorySpotResponse> spotAddressResponseList = SpotService.getCategorySpot(
            category, page);
        return CommonResponse.ofDataWithHttpStatus(spotAddressResponseList, HttpStatus.OK);
    }

    // 상세조회
    @GetMapping("/detail/{storeName}")
    public ResponseEntity<CommonResponse<SpotDetailResponse>> getSpotDetail(
        @PathVariable String storeName) {
        SpotDetailResponse spotDetailResponseList = SpotService.getSpotDetail(
            storeName);
        return CommonResponse.ofDataWithHttpStatus(spotDetailResponseList, HttpStatus.OK);
    }


}

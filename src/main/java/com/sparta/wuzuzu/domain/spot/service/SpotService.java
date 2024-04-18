package com.sparta.wuzuzu.domain.spot.service;

import com.sparta.wuzuzu.domain.spot.dto.response.SpotDetailResponse;
import com.sparta.wuzuzu.domain.spot.dto.response.CategorySpotResponse;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class SpotService {

    private final RestTemplate restTemplate;

    // RestTemplateBuilder의 build()를 사용하여 RestTemplate을 생성
    public SpotService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }


    /**
     * 카테고리별 전체 조회
     *
     * @param keyword 카테고리 (애견카페, 동물병원, 반려동물미용)
     * @param page    결과 페이지 번호
     * @return SpotAddressResponseKaKao 장소 정보
     */
    // 고정된 키워드 검색 -> 캐싱처리
    @Cacheable(cacheNames = "getCategorySpot", key = "#keyword +#page", cacheManager = "cacheManager")
    public List<CategorySpotResponse> getCategorySpot(String keyword, Integer page) {

        // api 요청
        JSONArray items = requsetSearchApi(keyword, page, 10);

        //dto 변환
        List<CategorySpotResponse> itemDtoList = new ArrayList<>();

        for (Object item : items) {
            CategorySpotResponse itemDto = new CategorySpotResponse((JSONObject) item);
            itemDtoList.add(itemDto);
        }
        return itemDtoList;
    }


    /**
     * 장소 상세 조회
     *
     * @param keyword 매장 이름
     * @return SpotAddressResponse 장소 정보
     */
    public SpotDetailResponse getSpotDetail(String keyword) {
        //api 요청
        JSONArray items = requsetSearchApi(keyword, 1, 1);

//        //dto변환
//        List<SpotDetailResponse> itemDtoList = new ArrayList<>();
//
//        for (Object item : items) {
//            SpotDetailResponse itemDto = new SpotDetailResponse((JSONObject) item);
//            itemDtoList.add(itemDto);
//        }
        return new SpotDetailResponse((JSONObject) items.get(0));
    }


    /**
     * 검색 api 요청
     *
     * @param keyword 카테고리 or 매장 이름
     * @param page    결과 페이지 번호 (상세조회의 경우 1로 고정)
     * @param size    한 페이지에 보여질 문서의 개수 (상세조회의 경우 1로 고정)
     * @return 장소 정보
     */
    public JSONArray requsetSearchApi(String keyword, Integer page, int size) {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
            .fromUriString("https://dapi.kakao.com")
            .path("/v2/local/search/keyword.json")
            .queryParam("query", keyword)
            .queryParam("page", page)
            .queryParam("size", size)
            .encode()
            .build()
            .toUri();

        // 카카오 API 호출 시 필요한 인증 정보
        RequestEntity<Void> requestEntity = RequestEntity
            .get(uri)
            .header("Authorization", "KakaoAK 736b1348fd2864407173f785ac47b0de")
            .build();

        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

        // 반환 데이터 JsonArray변환
        JSONObject jsonObject = new JSONObject(responseEntity.getBody());
        JSONArray items = jsonObject.getJSONArray("documents");
        return items;
    }


}

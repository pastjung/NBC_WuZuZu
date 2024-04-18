package com.sparta.wuzuzu.domain.favorite.service;

import com.sparta.wuzuzu.domain.favorite.dto.request.FavoriteRequest;
import com.sparta.wuzuzu.domain.favorite.dto.response.FavoriteResponse;
import com.sparta.wuzuzu.domain.favorite.entity.Favorite;
import com.sparta.wuzuzu.domain.favorite.repository.FavoriteQueryRepository;
import com.sparta.wuzuzu.domain.favorite.repository.FavoriteRepository;
import com.sparta.wuzuzu.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final FavoriteQueryRepository favoriteQueryRepository;


    /**
     * 즐겨찾기 추가
     *
     * @param request 즐겨찾기 추가 요청정보 (spotName,address,category)
     * @param user    추가 요청자
     */
    public void createFavorite(FavoriteRequest request, User user) {
        Boolean res = existFavorite(request);
        if (!res) {
            favoriteRepository.save(new Favorite(request, user));
        } else {
            throw new IllegalArgumentException("이미 추가한 곳입니다. ");
        }
    }

    /**
     * 즐겨찾기 삭제
     *
     * @param favoriteId 즐겨찾기 ID
     * @param user       추가 요청자
     */
    public void deleteFavorite(Long favoriteId, User user) {
        Favorite findFavorite = favoriteRepository.findById(favoriteId)
            .orElseThrow(() -> new IllegalArgumentException("선택한 favorite는 존재하지 않습니다."));

        if (!findFavorite.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("자신의 favorite가 아닙니다.");
        }
        favoriteRepository.delete(findFavorite);
    }

    /**
     * 즐겨찾기 중복확인
     *
     * @param request 즐겨찾기 추가 요청정보 (spotName,address,category)
     */
    private Boolean existFavorite(FavoriteRequest request) {
        return favoriteQueryRepository.existFavorite(request);
    }

    /**
     * 즐겨찾기 전체조회
     *
     * @param user 추가 요청자
     */
    public List<FavoriteResponse> getAllFavorite(User user) {
        return favoriteRepository.findAllByUser(user).stream().map(Favorite::createResponseFavorite)
            .toList();
    }

    /**
     * 즐겨찾기 카테고리별 조회
     *
     * @param category 카테고리
     * @param user     추가 요청자
     */
    public List<FavoriteResponse> getFavoriteCategory(String category, User user) {
        return favoriteQueryRepository.getFavoritecategory(category, user);
    }
}

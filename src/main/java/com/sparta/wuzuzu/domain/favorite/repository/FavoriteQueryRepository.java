package com.sparta.wuzuzu.domain.favorite.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.wuzuzu.domain.favorite.dto.request.FavoriteRequest;
import com.sparta.wuzuzu.domain.favorite.dto.response.FavoriteResponse;
import com.sparta.wuzuzu.domain.favorite.entity.Favorite;
import com.sparta.wuzuzu.domain.favorite.entity.QFavorite;
import com.sparta.wuzuzu.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class FavoriteQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private final QFavorite qFavorite = QFavorite.favorite;

    public Boolean existFavorite(FavoriteRequest request) {
        Integer fetchOne = jpaQueryFactory.selectOne().from(qFavorite).where(
            qFavorite.spotName.eq(request.getSpotName())
                .and(qFavorite.category.eq(request.getCategory()))
                .and(qFavorite.address.eq(request.getAddress()))).fetchFirst();
        return fetchOne != null;
    }

    public List<FavoriteResponse> getFavoritecategory(String category, User user) {
        return jpaQueryFactory.selectFrom(qFavorite)
            .where(qFavorite.user.userId.eq(user.getUserId()).and(qFavorite.category.eq(category)))
            .stream().map(Favorite::createResponseFavorite).toList();

    }
}

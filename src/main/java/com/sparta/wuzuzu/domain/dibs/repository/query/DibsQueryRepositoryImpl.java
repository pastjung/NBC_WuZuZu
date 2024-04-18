package com.sparta.wuzuzu.domain.dibs.repository.query;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.wuzuzu.domain.dibs.dto.DibsVo;
import com.sparta.wuzuzu.domain.dibs.entity.QDibs;
import com.sparta.wuzuzu.domain.sale_post.entity.QSalePost;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DibsQueryRepositoryImpl implements DibsQueryRepository{
    private final JPAQueryFactory jpaQueryFactory;
    private final QSalePost salePost = QSalePost.salePost;
    private final QDibs dibs = QDibs.dibs;

    @Override
    public List<DibsVo> findAllDibs(Long userId) {
        return jpaQueryFactory
            .select(Projections.constructor(DibsVo.class,
                salePost.salePostId,
                salePost.title,
                salePost.views,
                salePost.user.userName,
                salePost.category.name,
                salePost.status,
                salePost.goods,
                salePost.price,
                salePost.stock))
            .from(dibs)
            .join(salePost).on(dibs.salePostId.eq(salePost.salePostId))
            .where(dibs.user.userId.eq(userId))
            .orderBy(dibs.modifiedAt.desc())
            .fetch();
    }
}
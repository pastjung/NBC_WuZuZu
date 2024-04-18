package com.sparta.wuzuzu.domain.sale_post.repository.query;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.wuzuzu.domain.common.image.entity.QImage;
import com.sparta.wuzuzu.domain.sale_post.dto.SalePostVo;
import com.sparta.wuzuzu.domain.sale_post.entity.QSalePost;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SalePostQueryRepositoryImpl implements SalePostQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QSalePost salePost = QSalePost.salePost;
    private final QImage image = QImage.image;
    @Override
    public SalePostVo findPostByPostId(Long postId) {
        List<String> imageUrls = jpaQueryFactory
            .select(image.imageUrl)
            .from(image)
            .where(image.salePost.salePostId.eq(postId))
            .fetch();

        return jpaQueryFactory
            .select(Projections.constructor(SalePostVo.class,
                salePost.salePostId,
                salePost.title,
                salePost.description,
                salePost.views,
                salePost.user.userName,
                salePost.category.name,
                salePost.status,
                salePost.goods,
                salePost.price,
                salePost.stock,
                Expressions.asSimple(imageUrls)
            ))
            .from(salePost)
            .where(salePost.salePostId.eq(postId))
            .leftJoin(salePost.user)            // Fetch Join 으로 N+1 문제 해결
            .leftJoin(salePost.category)        // Fetch Join 으로 N+1 문제 해결
            .fetchOne();
    }
}
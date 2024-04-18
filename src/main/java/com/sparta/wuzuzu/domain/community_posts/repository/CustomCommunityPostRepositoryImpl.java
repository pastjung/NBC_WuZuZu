package com.sparta.wuzuzu.domain.community_posts.repository;

import static com.sparta.wuzuzu.domain.common.image.entity.QImage.*;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.wuzuzu.domain.common.image.entity.QImage;
import com.sparta.wuzuzu.domain.community_posts.dto.CommunityPostResponse;
import com.sparta.wuzuzu.domain.community_posts.entity.QCommunityPost;
import com.sparta.wuzuzu.domain.user.entity.QUser;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class CustomCommunityPostRepositoryImpl implements CustomCommunityPostRepository {

    public static final String TITLE = "title";
    public static final String VIEWS = "views";
    public static final String LIKE_COUNT = "likeCount";
    public static final String CREATED_AT = "createdAt";

    private final JPAQueryFactory queryFactory;
    QUser user = QUser.user;

    public CustomCommunityPostRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<CommunityPostResponse> findByConditions(String keyword, String categoryName,
        Pageable pageable) {
        QCommunityPost communityPost = QCommunityPost.communityPost;
        QUser user = QUser.user;

        JPAQuery<CommunityPostResponse> query = queryFactory
            .select(Projections.constructor(CommunityPostResponse.class,
                communityPost.communityPostId,
                communityPost.title,
                user.userName.as("userName"),
                communityPost.category.name.as("categoryName"),
                communityPost.content,
                communityPost.likeCount,
                communityPost.views,
                communityPost.commentList.size(),
                image.imageUrl.as("image")))
            .from(communityPost)
            .leftJoin(communityPost.user, user)
            .leftJoin(image).on(image.communityPost.communityPostId.eq(communityPost.communityPostId))
            .where(
                keywordIs(keyword),
                categoryIs(categoryName)
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());
        Sort.Order order = pageable.getSort().iterator().next();

        // 정렬 조건을 생성
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(order);

        // 쿼리에 정렬 조건 적용
        query.orderBy(orderSpecifier);

        QueryResults<CommunityPostResponse> results = query.fetchResults();

        List<CommunityPostResponse> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }


    private BooleanExpression keywordIs(String keyword) {
        if (StringUtils.hasText(keyword)) {
            return QCommunityPost.communityPost.title.containsIgnoreCase(keyword);
        }
        return null;
    }

    private BooleanExpression categoryIs(String categoryName) {
        if (StringUtils.hasText(categoryName)) {
            return QCommunityPost.communityPost.category.name.eq(categoryName);
        }
        return null;
    }

    private OrderSpecifier<?> getOrderSpecifier(Sort.Order order) {
        QCommunityPost communityPost = QCommunityPost.communityPost;

        switch (order.getProperty()) {
            case TITLE:
                return new OrderSpecifier<>(order.isAscending() ? Order.ASC : Order.DESC, communityPost.title);
            case VIEWS:
                return new OrderSpecifier<>(order.isAscending() ? Order.ASC : Order.DESC, communityPost.views);
            case LIKE_COUNT:
                return new OrderSpecifier<>(order.isAscending() ? Order.ASC : Order.DESC, communityPost.likeCount);
            case CREATED_AT:
                // createdAt 컬럼에 인덱스가 적용되어 있으므로, 이 컬럼을 기준으로 정렬할 때 쿼리 성능이 향상됩니다.
                return new OrderSpecifier<>(order.isAscending() ? Order.ASC : Order.DESC, communityPost.createdAt);
            default:
                throw new IllegalArgumentException("정렬 기준이 유효하지 않습니다: " + order.getProperty());
        }
    }

}


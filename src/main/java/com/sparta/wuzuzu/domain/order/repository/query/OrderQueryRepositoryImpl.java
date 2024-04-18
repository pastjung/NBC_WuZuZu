package com.sparta.wuzuzu.domain.order.repository.query;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.wuzuzu.domain.order.dto.OrdersVo;
import com.sparta.wuzuzu.domain.order.entity.QOrder;
import com.sparta.wuzuzu.domain.sale_post.entity.QSalePost;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepositoryImpl implements OrderQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QSalePost salePost = QSalePost.salePost;
    private final QOrder order = QOrder.order;

    @Override
    public List<OrdersVo> findAllOrders(Long userId) {
        return jpaQueryFactory
            .select(Projections.constructor(OrdersVo.class,
                salePost.salePostId,
                salePost.title,
                salePost.views,
                salePost.user.userName,
                salePost.category.name,
                salePost.status,
                salePost.goods,
                salePost.price,
                order.count))
            .from(order)
            .join(salePost).on(order.salePostId.eq(salePost.salePostId))
            .where(order.user.userId.eq(userId))
            .orderBy(order.modifiedAt.desc())
            .fetch();
    }
}
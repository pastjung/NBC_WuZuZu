package com.sparta.wuzuzu.domain.message.repository;

import static com.sparta.wuzuzu.domain.message.entity.QMessage.*;
import static com.sparta.wuzuzu.domain.user.entity.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.wuzuzu.domain.message.dto.GetMessageResponse;
import com.sparta.wuzuzu.domain.message.entity.QMessage;
import com.sparta.wuzuzu.domain.user.entity.QUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GetMessageResponse> findAllByRoomId(Long roomId) {
        return queryFactory.select(
                Projections.fields(GetMessageResponse.class, user.userId, user.userName, message.messageId,
                    message.content, message.createdAt))
            .from(message)
            .where(message.roomId.eq(roomId))
            .leftJoin(user).on(message.userId.eq(user.userId))
            .fetch();
    }
}

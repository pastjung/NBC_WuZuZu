package com.sparta.wuzuzu.domain.chat_room.entity;

import com.sparta.wuzuzu.domain.common.entity.Timestamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "members", indexes = {
    @Index(name = "idx_member_user_id", columnList = "user_id"),
    @Index(name = "idx_member_chat_room_id", columnList = "chat_room_id"),
    @Index(name = "idx_member_user_id_chat_room_id", columnList = "user_id, chat_room_id")})
public class Member extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column
    private Long userId;

    @Column
    private Long chatRoomId;
}

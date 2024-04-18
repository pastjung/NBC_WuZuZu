package com.sparta.wuzuzu.domain.message.entity;

import com.sparta.wuzuzu.domain.common.entity.Timestamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
public class Message extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long roomId;

    public Message(String content, Long userId, Long roomId) {
        this.content = content;
        this.userId = userId;
        this.roomId = roomId;
    }
}
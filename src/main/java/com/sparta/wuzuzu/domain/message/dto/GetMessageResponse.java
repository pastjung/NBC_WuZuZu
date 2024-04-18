package com.sparta.wuzuzu.domain.message.dto;

import com.sparta.wuzuzu.domain.message.entity.Message;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMessageResponse {

    private Long userId;
    private String userName;
    private Long messageId;
    private String content;
    private LocalDateTime createdAt;

    public GetMessageResponse(String userName, Message message) {
        this.userId = message.getUserId();
        this.userName = userName;
        this.messageId = message.getMessageId();
        this.content = message.getContent();
        this.createdAt = message.getCreatedAt();
    }
}

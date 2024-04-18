package com.sparta.wuzuzu.domain.chat_room.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateChatRoomRequest {

    private String chatRoomName;
    private String description;
    private String coverImage;
    private List<String> chatRoomTags;
}

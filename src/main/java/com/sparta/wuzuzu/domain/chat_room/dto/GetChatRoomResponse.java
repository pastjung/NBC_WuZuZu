package com.sparta.wuzuzu.domain.chat_room.dto;

import com.sparta.wuzuzu.domain.chat_room.entity.ChatRoom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetChatRoomResponse {

    private String userName;
    private Long chatRoomId;
    private String chatRoomName;
    private String description;
    private String coverImage;
    private List<String> chatRoomTags = new ArrayList<>();

    public GetChatRoomResponse(String userName, ChatRoom chatRoom) {
        this.userName = userName;
        chatRoomId = chatRoom.getChatRoomId();
        chatRoomName = chatRoom.getChatRoomName();
        description = chatRoom.getDescription();
        coverImage = chatRoom.getDescription();
        chatRoomTags = Arrays.stream(chatRoom.getChatRoomTag().split(",")).toList();
    }
}

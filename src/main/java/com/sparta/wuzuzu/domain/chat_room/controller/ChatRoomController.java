package com.sparta.wuzuzu.domain.chat_room.controller;

import com.sparta.wuzuzu.domain.chat_room.dto.CreateChatRoomRequest;
import com.sparta.wuzuzu.domain.chat_room.dto.GetChatRoomResponse;
import com.sparta.wuzuzu.domain.chat_room.serivce.ChatRoomService;
import com.sparta.wuzuzu.global.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/chat-rooms")
    public List<GetChatRoomResponse> getChatRooms(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.getChatRooms(userDetails.getUser());
    }

    @PostMapping("/chat-rooms")
    public GetChatRoomResponse createChatRoom(
        @RequestBody CreateChatRoomRequest request,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.createChatRoom(request, userDetails.getUser());
    }

    @PostMapping("/chat-rooms/{chatRoomId}")
    public Long enterChatRoom(
        @PathVariable Long chatRoomId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.enterChatRoom(chatRoomId, userDetails.getUser());
    }

    @GetMapping("/chat-rooms/my-rooms")
    public List<GetChatRoomResponse> getMyChatRooms(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.getMyChatRooms(userDetails.getUser());
    }

    @GetMapping("/chat-rooms/search-keyword")
    public List<GetChatRoomResponse> searchChatRoomsByKeyword(
        @RequestParam(name = "keyword") String keyword
    ) {
        return chatRoomService.searchChatRoomsByKeyword(keyword);
    }

    @GetMapping("/chat-rooms/search-tag")
    public List<GetChatRoomResponse> searchChatRoomsByTag(
        @RequestParam(name = "tag") String tag
    ) {
        return chatRoomService.searchChatRoomsByTag(tag);
    }
}

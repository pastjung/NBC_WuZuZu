package com.sparta.wuzuzu.domain.message.controller;

import com.sparta.wuzuzu.domain.common.dto.CommonResponse;
import com.sparta.wuzuzu.domain.message.dto.CreateMessageRequest;
import com.sparta.wuzuzu.domain.message.dto.GetMessageResponse;
import com.sparta.wuzuzu.domain.message.serivce.MessageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat-rooms")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @MessageMapping("/chat-rooms/{roomId}/messages")
    @SendTo("/topic/chat-rooms/{roomId}")
    public GetMessageResponse sendMessage(
        @DestinationVariable Long roomId,
        CreateMessageRequest request,
        @Header("Authorization") String token
    ) throws Exception {
        Thread.sleep(500); // 지연 시뮬레이션

        return messageService.createMessage(roomId, request, token);
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<CommonResponse<List<GetMessageResponse>>> getRoomMessages(
        @PathVariable Long roomId) {
        return CommonResponse.ofDataWithHttpStatus(
            messageService.getRoomMessages(roomId), HttpStatus.OK);
    }
}
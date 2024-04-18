package com.sparta.wuzuzu.domain.message.serivce;

import com.sparta.wuzuzu.domain.message.dto.CreateMessageRequest;
import com.sparta.wuzuzu.domain.message.dto.GetMessageResponse;
import com.sparta.wuzuzu.domain.message.entity.Message;
import com.sparta.wuzuzu.domain.message.repository.MessageRepository;
import com.sparta.wuzuzu.domain.user.entity.User;
import com.sparta.wuzuzu.global.jwt.JwtUtil;
import com.sparta.wuzuzu.global.security.UserDetailsImpl;
import com.sparta.wuzuzu.global.security.UserDetailsServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;

    public GetMessageResponse createMessage(Long roomId, CreateMessageRequest request,
        String token) {
        if (roomId < 1) {
            throw new IllegalArgumentException("방 번호 오류 : " + roomId);
        }

        if (request.getContent().isEmpty()) {
            throw new IllegalArgumentException("메시지 입력값 Empty");
        }

        token = token.substring(7);

        User user = ((UserDetailsImpl) userDetailsService.loadUserByUsername(
            jwtUtil.getUserInfoFromToken(token).getSubject())).getUser();

        Message message = messageRepository.save(
            new Message(request.getContent(), user.getUserId(), roomId));

        return new GetMessageResponse(user.getUserName(), message);
    }

    public List<GetMessageResponse> getRoomMessages(Long roomId) {
        List<GetMessageResponse> responseList = messageRepository.findAllByRoomId(roomId);

        return responseList;
    }
}

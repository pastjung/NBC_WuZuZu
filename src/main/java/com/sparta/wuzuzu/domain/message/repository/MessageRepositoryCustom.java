package com.sparta.wuzuzu.domain.message.repository;

import com.sparta.wuzuzu.domain.message.dto.GetMessageResponse;
import java.util.List;

public interface MessageRepositoryCustom {

    List<GetMessageResponse> findAllByRoomId(Long roomId);
}

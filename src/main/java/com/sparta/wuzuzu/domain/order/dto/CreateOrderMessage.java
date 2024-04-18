package com.sparta.wuzuzu.domain.order.dto;

import com.sparta.wuzuzu.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateOrderMessage {

    private User user;

    private OrderRequest request;

    private String imp_uid;

    public CreateOrderMessage(User user, OrderRequest orderRequest, String imp_uid) {
        this.user = user;
        this.request = orderRequest;
        this.imp_uid = imp_uid;
    }
}

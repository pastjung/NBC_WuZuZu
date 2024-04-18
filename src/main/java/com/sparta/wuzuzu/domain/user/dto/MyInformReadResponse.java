package com.sparta.wuzuzu.domain.user.dto;

import com.sparta.wuzuzu.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MyInformReadResponse {

    private String email;

    private String address;

    private String userName;

    private String petName;

    private String petType;

    @Builder
    public MyInformReadResponse(User user) {
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.userName = user.getUserName();
        this.petName = user.getPetName();
        this.petType = user.getPetType();
    }
}

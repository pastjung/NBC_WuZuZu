package com.sparta.wuzuzu.domain.admin.dto;

import com.sparta.wuzuzu.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserInformReadResponse {

    private Long userId;

    private String email;

    private String address;

    private String userName;

    private String petName;

    private String petType;

    private LocalDateTime createdAt;

    @Builder
    public UserInformReadResponse(User user){
        userId = user.getUserId();
        email = user.getEmail();
        address = user.getAddress();
        userName = user.getUserName();
        petName = user.getPetName();
        petType = user.getPetType();
        createdAt = user.getCreatedAt();
    }
}

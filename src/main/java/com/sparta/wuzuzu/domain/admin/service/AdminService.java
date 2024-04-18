package com.sparta.wuzuzu.domain.admin.service;

import com.sparta.wuzuzu.domain.admin.dto.UserInformReadResponse;
import com.sparta.wuzuzu.domain.user.entity.User;
import com.sparta.wuzuzu.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public List<UserInformReadResponse> readAllUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(UserInformReadResponse::new).collect(Collectors.toList());
    }

    public UserInformReadResponse readUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->new IllegalArgumentException("해당 회원은 존재하지 않습니다."));
        return new UserInformReadResponse(user);
    }

    public void blockedUser(Long userId) {
        User user= userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 회원은 존재하지 않습니다."));

        if(user.getBlocked())
            throw new IllegalArgumentException("이미 차단된 회원입니다.");

        user.beBlocked(user, true);
    }
}

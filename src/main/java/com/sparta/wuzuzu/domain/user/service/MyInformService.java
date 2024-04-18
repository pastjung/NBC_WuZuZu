package com.sparta.wuzuzu.domain.user.service;

import com.sparta.wuzuzu.domain.user.dto.MyInformReadResponse;
import com.sparta.wuzuzu.domain.user.dto.MyInformUpdateRequest;
import com.sparta.wuzuzu.domain.user.dto.UpdatePasswordRequest;
import com.sparta.wuzuzu.domain.user.entity.User;
import com.sparta.wuzuzu.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class MyInformService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public MyInformReadResponse readMyInform(User user) {
        user = userRepository.findById(user.getUserId()).orElseThrow();

        return MyInformReadResponse.builder()
            .user(user)
            .build();
    }

    public MyInformReadResponse updateMyInform(User user, MyInformUpdateRequest myInformUpdateRequest) {
        user = userRepository.findById(user.getUserId()).orElseThrow();

        if (!isPasswordMatches(myInformUpdateRequest.getCurrentPassword(), user.getPassword()))
            throw new IllegalArgumentException();

        user.update(user, myInformUpdateRequest);

        return MyInformReadResponse.builder()
            .user(user)
            .build();
    }

    public void updatePassword(User user, UpdatePasswordRequest updatePasswordRequest) {
        String encryptNewPassword = passwordEncoder.encode(
            updatePasswordRequest.getNewPassword());
        user = userRepository.findById(user.getUserId()).orElseThrow();

        if (!isPasswordMatches(updatePasswordRequest.getCurrentPassword(), user.getPassword()))
            throw new IllegalArgumentException();

        if (!Objects.equals(updatePasswordRequest.getNewPassword(), updatePasswordRequest.getConfirmPassword()))
            throw new IllegalArgumentException();

        user.updatePassword(user, encryptNewPassword);
    }

    private boolean isPasswordMatches(String inputPassword, String passwordInDb) {
        return passwordEncoder.matches(inputPassword, passwordInDb);
    }
}

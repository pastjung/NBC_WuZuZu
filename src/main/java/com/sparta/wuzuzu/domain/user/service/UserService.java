package com.sparta.wuzuzu.domain.user.service;

import com.sparta.wuzuzu.domain.user.dto.ReportUserRequest;
import com.sparta.wuzuzu.domain.user.dto.SignUpRequest;
import com.sparta.wuzuzu.domain.user.dto.SignUpResponse;
import com.sparta.wuzuzu.domain.user.entity.User;
import com.sparta.wuzuzu.domain.user.entity.UserRole;
import com.sparta.wuzuzu.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        String confirmEmail = signUpRequest.getEmail();
        if (userRepository.findByEmail(confirmEmail) != null) {
            throw new IllegalArgumentException("중복된 이메일 입니다.");
        }

        if (!Objects.equals(signUpRequest.getPassword(), signUpRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        User user = User.builder().
                email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .userName(signUpRequest.getUserName())
                .address(signUpRequest.getAddress())
                .petName(signUpRequest.getPetName())
                .petType(signUpRequest.getPetType())
                .role(UserRole.USER)
                .blocked(false)
                .numberOfCount(0)
                .build();

        userRepository.save(user);

        return new SignUpResponse(user.getUserId(), user.getEmail());
    }

    public void reportUser(ReportUserRequest reportUserRequest) {
        User user = userRepository.findById(reportUserRequest.getReportUserId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        if (user.getBlocked())
            throw new IllegalArgumentException("이미 차단된 사용자입니다.");

        int limitedCount = 10;
        if (user.getNumberOfCount() == limitedCount) {
            user.beBlocked(user, true);
            return;
        }

        if (user.getNumberOfCount() < limitedCount) {
            user.plusCount(user);
        }

        if (user.getNumberOfCount() >= limitedCount) {
            user.beBlocked(user, true);
        }
    }
}

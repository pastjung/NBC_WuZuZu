package com.sparta.wuzuzu.domain.user.controller;

import com.sparta.wuzuzu.domain.common.dto.CommonResponse;
import com.sparta.wuzuzu.domain.user.dto.ReportUserRequest;
import com.sparta.wuzuzu.domain.user.dto.SignUpRequest;
import com.sparta.wuzuzu.domain.user.dto.SignUpResponse;
import com.sparta.wuzuzu.domain.user.service.UserService;
import com.sparta.wuzuzu.global.jwt.JwtTokenBlacklist;
import com.sparta.wuzuzu.global.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final JwtTokenBlacklist jwtTokenBlacklist;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<SignUpResponse>> signUp(@RequestBody @Validated SignUpRequest signUpRequest) {
        SignUpResponse signUpResponse = userService.signUp(signUpRequest);
        return CommonResponse.ofDataWithHttpStatus(signUpResponse, HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity<CommonResponse<String>> reportUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ReportUserRequest reportUserRequest){
        userService.reportUser(reportUserRequest);
        return CommonResponse.ofDataWithHttpStatus("신고 완료", HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<CommonResponse<String>> logout(HttpServletRequest request) {
        String token = extractTokenFromHeader(request);
        if (token != null) {
            jwtTokenBlacklist.addToBlacklist(token);
            return CommonResponse.ofDataWithHttpStatus("로그아웃 되었습니다.", HttpStatus.NO_CONTENT);
        } else {
            return CommonResponse.ofDataWithHttpStatus("토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
    }


    private String extractTokenFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}

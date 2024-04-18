package com.sparta.wuzuzu.domain.admin.controller;

import com.sparta.wuzuzu.domain.admin.dto.UserInformReadResponse;
import com.sparta.wuzuzu.domain.admin.service.AdminService;
import com.sparta.wuzuzu.domain.common.dto.CommonResponse;
import com.sparta.wuzuzu.global.jwt.JwtTokenBlacklist;
import com.sparta.wuzuzu.global.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Secured("ROLE_ADMIN")
@RequestMapping("/api/v1/admins")
public class AdminController {

    private final AdminService adminService;
    private final JwtTokenBlacklist jwtTokenBlacklist;

    @GetMapping("/users")
    public ResponseEntity<CommonResponse<List<UserInformReadResponse>>> readAllUsers(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<UserInformReadResponse> userList = adminService.readAllUsers();
        return CommonResponse.ofDataWithHttpStatus(userList, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<CommonResponse<UserInformReadResponse>> readAllUsers(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long userId) {
        UserInformReadResponse user = adminService.readUser(userId);
        return CommonResponse.ofDataWithHttpStatus(user, HttpStatus.OK);
    }

    @PatchMapping("/users/{userId}")
    public ResponseEntity<CommonResponse<String>> blockedUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long userId){
        adminService.blockedUser(userId);
        return CommonResponse.ofDataWithHttpStatus(userId + "차단 성공", HttpStatus.OK);
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

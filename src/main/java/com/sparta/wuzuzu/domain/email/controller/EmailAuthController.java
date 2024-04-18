package com.sparta.wuzuzu.domain.email.controller;

import com.sparta.wuzuzu.domain.admin.dto.AdminVerifyRequest;
import com.sparta.wuzuzu.domain.common.dto.CommonResponse;
import com.sparta.wuzuzu.domain.email.dto.EmailAuthResponse;
import com.sparta.wuzuzu.domain.email.dto.EmailRequest;
import com.sparta.wuzuzu.domain.email.dto.VerifyRequest;
import com.sparta.wuzuzu.domain.email.service.EmailAuthService;
import com.sparta.wuzuzu.domain.user.dto.SignUpRequest;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/email")
public class EmailAuthController {

    private final EmailAuthService emailAuthService;

    @PostMapping("/users/auth")
    public ResponseEntity<CommonResponse<String>> sendEmail(@RequestBody @Validated EmailRequest emailRequest) throws MessagingException {
        emailAuthService.sendEmail(emailRequest.getMail());
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.ofData("인증 메일 발송 성공"));
    }

    // 발송된 코드 인증
    @PostMapping("/users/verify")
    public ResponseEntity<CommonResponse<String>> verify(@RequestBody VerifyRequest verifyRequest) {
         EmailAuthResponse emailAuthResponse = emailAuthService.verifyEmailCode(verifyRequest);
         if(emailAuthResponse.getVerifiedEmail() == null)
             throw new IllegalStateException("인증에 실패했습니다");
         return CommonResponse.ofDataWithHttpStatus(emailAuthResponse.getVerifiedEmail(),HttpStatus.OK);
    }

    // 관리자 권한 인증
    @PostMapping("/admins/{adminId}/auth")
    public ResponseEntity<CommonResponse<String>> requestAdminAuth(@PathVariable Long adminId, @RequestBody EmailRequest emailRequest) throws MessagingException {
        emailAuthService.sendAdminEmail(adminId, emailRequest.getMail());
        return CommonResponse.ofDataWithHttpStatus("관리자 인증 요청 이메일 인증 코드 발송", HttpStatus.OK);
    }

    @PatchMapping("/admins/{adminId}/verify")
    public ResponseEntity<CommonResponse<String>> responseAdminVerify(@PathVariable Long adminId, @RequestBody AdminVerifyRequest adminVerifyRequest) {
        String wuzuzuPassword = emailAuthService.verifyAdminEmailCode(adminId, adminVerifyRequest);
        return CommonResponse.ofDataWithHttpStatus(wuzuzuPassword, HttpStatus.OK);
    }
}

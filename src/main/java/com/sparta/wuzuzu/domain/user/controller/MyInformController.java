package com.sparta.wuzuzu.domain.user.controller;

import com.sparta.wuzuzu.domain.common.dto.CommonResponse;
import com.sparta.wuzuzu.domain.user.dto.MyInformReadResponse;
import com.sparta.wuzuzu.domain.user.dto.MyInformUpdateRequest;
import com.sparta.wuzuzu.domain.user.dto.UpdatePasswordRequest;
import com.sparta.wuzuzu.domain.user.service.MyInformService;
import com.sparta.wuzuzu.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class MyInformController {

    private final MyInformService myInformService;

    @GetMapping
    public ResponseEntity<CommonResponse<MyInformReadResponse>> readMyInform(@AuthenticationPrincipal UserDetailsImpl userDetails
                                                                             ) {
        MyInformReadResponse myInformReadResponse = myInformService.readMyInform(userDetails.getUser());
        return CommonResponse.ofDataWithHttpStatus(myInformReadResponse, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<CommonResponse<MyInformReadResponse>> updateMyInform(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                                 @RequestBody @Validated MyInformUpdateRequest myInformUpdateRequest) {
        MyInformReadResponse myInformReadResponse = myInformService.updateMyInform(userDetails.getUser(), myInformUpdateRequest);
        return CommonResponse.ofDataWithHttpStatus(myInformReadResponse, HttpStatus.OK);
    }

    @PatchMapping("/password")
    public ResponseEntity<CommonResponse<Void>> updatePassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                               @RequestBody @Validated UpdatePasswordRequest updatePasswordRequest){
        myInformService.updatePassword(userDetails.getUser(), updatePasswordRequest);
        return ResponseEntity.status(HttpStatus.OK.value()).body(CommonResponse.<Void>builder().build());
    }
}

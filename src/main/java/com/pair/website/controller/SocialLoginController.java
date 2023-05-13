package com.pair.website.controller;

import com.pair.website.dto.BaseResponseDto;
import com.pair.website.dto.kakao.KakaoAccountDto;
import com.pair.website.service.SocialLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
public class SocialLoginController {

    @Autowired
    private final SocialLoginService socialLoginService;

    private final HttpSession httpSession;

    // 프론트에서 인가코드 돌려 받는 주소
    // 인가 코드로 엑세스 토큰 발급 -> 사용자 정보 조회 -> DB 저장 -> jwt 토큰 발급 -> 프론트에 토큰 전달
    @GetMapping("/api/oauth/token")
    public BaseResponseDto<?> getLogin(@RequestParam("code") String code, HttpServletResponse response) {
        // 헤더에 넣어서 주기?

        return socialLoginService.kakaoLogin(code, response);
    }
}

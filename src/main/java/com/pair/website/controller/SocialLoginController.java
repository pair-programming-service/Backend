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

    private final SocialLoginService socialLoginService;
    private final HttpSession httpSession;

    /**
     * 카카오 소셜 로그인 API
     * flow: 인가 코드로 엑세스 토큰 발급 -> 사용자 정보 조회 -> DB 저장 -> jwt 토큰 발급 -> 프론트에 토큰 전달
     *
     * @param code 카카오 인가코드(프론트에서 전달)
     * @return BaseResponseDto<LoginResponseDto> 200 OK, 로그인한 회원 정보
     */
    // 프론트에서 인가코드 돌려 받는 주소
    @GetMapping("/api/oauth/token")
    public BaseResponseDto<?> getLogin(@RequestParam("code") String code, HttpServletResponse response) {

        return socialLoginService.kakaoLogin(code, response);
    }
}

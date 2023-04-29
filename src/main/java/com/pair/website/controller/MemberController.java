package com.pair.website.controller;

import com.pair.website.dto.BaseResponseDto;
import com.pair.website.dto.LoginRequestDto;
import com.pair.website.dto.MemberRequestDto;
import com.pair.website.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;


    @PostMapping("/api/member/signup")
    public BaseResponseDto<?> signup(@RequestBody MemberRequestDto memberRequestDto){
        return memberService.signup(memberRequestDto);
    }

    @PostMapping("/api/member/login")
    public BaseResponseDto<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response){
        return memberService.login(loginRequestDto,response);
    }
}

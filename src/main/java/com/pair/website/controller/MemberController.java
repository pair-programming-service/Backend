package com.pair.website.controller;

import com.pair.website.domain.Member;
import com.pair.website.dto.BaseResponseDto;
import com.pair.website.dto.LoginRequestDto;
import com.pair.website.dto.MemberRequestDto;
import com.pair.website.dto.ProfileRequestDto;
import com.pair.website.service.MemberService;
import com.pair.website.util.PublicMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PublicMethod publicMethod;


    @PostMapping("/api/member/signup")
    public BaseResponseDto<?> signup(@RequestBody MemberRequestDto memberRequestDto){
        return memberService.signup(memberRequestDto);
    }

    @PostMapping("/api/member/login")
    public BaseResponseDto<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response){
        return memberService.login(loginRequestDto,response);
    }

    @PostMapping("/api/member/check")
    public BaseResponseDto<?> nicknameCheck(@RequestBody String nickname){
        return memberService.nicknameCheck(nickname);
    }
    @PutMapping("/api/member/update")
    public BaseResponseDto<?> profileUpdate(@RequestBody ProfileRequestDto requestDto, HttpServletRequest request){
        BaseResponseDto<?> result = publicMethod.checkLogin(request);
        if(!result.isSuccess()) return result;
        Member member = (Member)result.getData();

        return memberService.profileUpdate(requestDto,member.getId());

    }
}

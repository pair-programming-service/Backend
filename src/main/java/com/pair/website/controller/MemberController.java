package com.pair.website.controller;

import com.pair.website.domain.Member;
import com.pair.website.dto.*;
import com.pair.website.dto.response.BaseResponseDto;
import com.pair.website.service.MemberService;
import com.pair.website.util.PublicMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PublicMethod publicMethod;


    @PostMapping("/api/member/signup")
    public ResponseEntity<?> signup(@RequestBody MemberRequestDto memberRequestDto) {
        return memberService.signup(memberRequestDto);
    }

    @PostMapping("/api/member/login")
    public BaseResponseDto<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return memberService.login(loginRequestDto, response);
    }

    @PutMapping("/api/member/update")
    public BaseResponseDto<?> profileUpdate(@RequestPart ProfileRequestDto requestDto, HttpServletRequest request , @RequestPart(value = "image") MultipartFile image) throws IOException {
        BaseResponseDto<?> result = publicMethod.checkLogin(request);
        if (!result.isSuccess()) return result;
        Member member = (Member) result.getData();

        return memberService.profileUpdate(image, requestDto, member.getId());
    }

    @GetMapping("/api/member/detail")
    public BaseResponseDto<?> getProfile(HttpServletRequest request) {
        BaseResponseDto<?> result = publicMethod.checkLogin(request);
        if (!result.isSuccess()) return result;
        Member member = (Member) result.getData();

        return memberService.getProfile(member.getId());
    }

    @PostMapping("/api/room/produce/{customer_id}")
    public BaseResponseDto<?> roomProduce(HttpServletRequest request, @PathVariable Long customer_id){
        BaseResponseDto<?> result = publicMethod.checkLogin(request);
        if (!result.isSuccess()) return result;
        Member member = (Member) result.getData();
        return memberService.roomProduce(member,customer_id);
    }
}

package com.pair.website.controller;

import com.pair.website.domain.Member;
import com.pair.website.dto.BaseResponseDto;
import com.pair.website.dto.LoginRequestDto;
import com.pair.website.dto.MemberRequestDto;
import com.pair.website.dto.ProfileRequestDto;
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


    /**
     * 회원가입 API
     *
     * @param memberRequestDto 회원가입 정보
     * @return BaseResponseDto<MemberResponseDto> 200 OK, 가입한 회원 정보, JWT 토큰
     */
    @PostMapping("/api/member/signup")
    public ResponseEntity<?> signup(@RequestBody MemberRequestDto memberRequestDto) {
        return memberService.signup(memberRequestDto);
    }

    /**
     * 로그인 API
     *
     * @param loginRequestDto 아이디, 비밀번호
     * @return BaseResponseDto<MemberResponseDto> 200 OK, 가입한 회원 정보, JWT 토큰
     */
    @PostMapping("/api/member/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return memberService.login(loginRequestDto, response);
    }

    /**
     * 회원정보 수정 API
     *
     * @return BaseResponseDto<MemberResponseDto> 200 OK, 수정된 회원정보 및 작성한 게시물
     */
    @PutMapping("/api/member/update")
    public BaseResponseDto<?> updateProfile(@RequestPart ProfileRequestDto requestDto, HttpServletRequest request, @RequestPart(value = "image",required = false) MultipartFile image) throws IOException {
        BaseResponseDto<?> result = publicMethod.checkLogin(request);
        if (!result.isSuccess()) return result;
        Member member = (Member) result.getData();

        return memberService.profileUpdate(image, requestDto, member.getId());
    }

    /**
     * 마이페이지 조회 API
     *
     * @return BaseResponseDto<MemberResponseDto> 200 OK, 회원정보 및 작성한 게시물
     */
    @GetMapping("/api/member/detail")
    public BaseResponseDto<?> getProfile(HttpServletRequest request) {

        BaseResponseDto<?> result = publicMethod.checkLogin(request);
        if (!result.isSuccess()) return result;
        Member member = (Member) result.getData();

        return memberService.getProfile(member.getId());
    }

    /**
     * nickname을 통한 마이페이지 조회 API
     *
     * @param nickname 닉네임
     * @return BaseResponseDto<MemberResponseDto> 200 OK, 회원정보 및 작성한 게시물
     */
    @GetMapping("/api/member/detail/{nickname}")
    public ResponseEntity<?> getOtherProfile(@PathVariable String nickname) {
        return memberService.getOtherProfile(nickname);
    }
}

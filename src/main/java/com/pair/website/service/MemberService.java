package com.pair.website.service;

import com.pair.website.domain.Member;
import com.pair.website.dto.BaseResponseDto;
import com.pair.website.dto.LoginRequestDto;
import com.pair.website.dto.MemberRequestDto;
import com.pair.website.dto.TokenDto;
import com.pair.website.dto.response.MemberResponseDto;
import com.pair.website.jwt.TokenProvider;
import com.pair.website.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public BaseResponseDto<?> signup(@Valid MemberRequestDto requestDto) {
        if (isPresentMember(requestDto.getEmail()) != null) {
            return BaseResponseDto.fail("DUPLICATED_NICKNAME",
                    "중복된 아이디입니다.");
        }
        Member member = Member.builder()
                .email(requestDto.getEmail())
                .nickname(requestDto.getNickname())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .profileImage(requestDto.getProfileImage())
                .githubLink(requestDto.getGithubLink())
                .build();
        memberRepository.save(member);

        return BaseResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .email(member.getEmail())
                        .nickname(member.getNickname())
                        .profileImage(member.getProfileImage())
                        .githubLink(member.getGithubLink())
                        .createdAt(member.getCreatedAt())
                        .build());
    }


    @Transactional
    public BaseResponseDto<?> login(@Valid LoginRequestDto loginRequestDto, HttpServletResponse response) {
        Member member = isPresentMember(loginRequestDto.getEmail());
        if (null == member) {
            return BaseResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }

        if (!member.validatePassword(passwordEncoder, loginRequestDto.getPassword())) {
            return BaseResponseDto.fail("INVALID_MEMBER", "사용자를 찾을 수 없습니다.");
        }


        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response);

        return BaseResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .email(member.getEmail())
                        .nickname(member.getNickname())
                        .profileImage(member.getProfileImage())
                        .githubLink(member.getGithubLink())
                        .createdAt(member.getCreatedAt())
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public Member isPresentMember(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        return optionalMember.orElse(null);
    }

    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }
}

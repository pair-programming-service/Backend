package com.pair.website.util;

import com.pair.website.domain.Member;
import com.pair.website.dto.BaseResponseDto;
import com.pair.website.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@Component
@RequiredArgsConstructor
public class PublicMethod {
    private final TokenProvider tokenProvider;

    @Transactional
    public BaseResponseDto<?> checkLogin(HttpServletRequest request){
        if (null == request.getHeader("refreshToken")) {
            return BaseResponseDto.fail("MEMBER_NOT_FOUND","로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return BaseResponseDto.fail("MEMBER_NOT_FOUND","로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return BaseResponseDto.fail("INVALID_TOKEN","토큰이 유효하지 않습니다.");
        }

        return BaseResponseDto.success(member);
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("refreshToken"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}

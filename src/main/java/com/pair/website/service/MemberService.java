package com.pair.website.service;

import com.pair.website.domain.Member;
import com.pair.website.domain.PairBoard;
import com.pair.website.dto.*;
import com.pair.website.dto.response.BoardAllResponseDto;
import com.pair.website.dto.response.MemberResponseDto;
import com.pair.website.dto.response.PairBoardSaveResponseDto;
import com.pair.website.jwt.TokenProvider;
import com.pair.website.repository.MemberRepository;
import com.pair.website.repository.PairBoardRepository;
import com.pair.website.util.PublicMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final PairBoardRepository pairBoardRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> signup(@Valid MemberRequestDto requestDto) {
        if (isPresentMember(requestDto.getEmail()) != null) {
            return new ResponseEntity(BaseResponseDto.fail("DUPLICATED_EMAIL",
                    "중복된 이메일입니다."), HttpStatus.BAD_REQUEST);
        }

        if(checkNickname(requestDto.getNickname()) != null) {
            return new ResponseEntity(BaseResponseDto.fail("DUPLICATED_NICKNAME",
                    "중복된 닉네임입니다."), HttpStatus.BAD_REQUEST);
        }
        Member member = Member.builder()
                .email(requestDto.getEmail())
                .nickname(requestDto.getNickname())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();
        memberRepository.save(member);

        return new ResponseEntity(BaseResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .email(member.getEmail())
                        .nickname(member.getNickname())
                        .profileImage(member.getProfileImage())
                        .githubLink(member.getGithubLink())
                        .createdAt(member.getCreatedAt())
                        .build()),HttpStatus.OK);
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

    // 마이페이지 조회
    public BaseResponseDto<?> getProfile(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new NullPointerException("NOT_FOUND_MEMBER")
        );

        List<PairBoard> boards = pairBoardRepository.findAllByMemberId(member.getId());
        // List<BoardAllResponseDto> responseDtos = new ArrayList<>();
        List<BoardListResponseDto> boardListResponseDtos = new ArrayList<>();

        for (PairBoard board : boards) {
            boardListResponseDtos.add(BoardListResponseDto.builder().title(board.getTitle()).content(board.getContent()).category(board.getCategory()).build());
        }

        return BaseResponseDto.success(MemberResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .githubLink(member.getGithubLink())
                .createdAt(member.getCreatedAt())
                .boardList(boardListResponseDtos)
                .build());
    }

    // 프로필 수정
    @Transactional
    public BaseResponseDto<?> profileUpdate(ProfileRequestDto requestDto, Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new NullPointerException("NOT_FOUND_MEMBER")
        );
        member.update(ProfileRequestDto.builder()
                .nickname(requestDto.getNickname())
                .profileImage(requestDto.getProfileImage())
                .githubLink(requestDto.getGithubLink())
                .build());

        return BaseResponseDto.success(MemberResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .githubLink(member.getGithubLink())
                .createdAt(member.getCreatedAt())
                .build());
    }

    @Transactional(readOnly = true)
    public Member isPresentMember(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        return optionalMember.orElse(null);
    }

    @Transactional(readOnly = true)
    public Member checkNickname(String nickname) {
        Optional<Member> optionalMember = memberRepository.findByNickname(nickname);
        return optionalMember.orElse(null);
    }

    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("refreshToken", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }
}

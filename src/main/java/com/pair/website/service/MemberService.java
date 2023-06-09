package com.pair.website.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.pair.website.configuration.s3.AwsS3Uploader;
import com.pair.website.domain.Member;
import com.pair.website.domain.PairBoard;
import com.pair.website.dto.*;
import com.pair.website.dto.response.BoardAllResponseDto;
import com.pair.website.dto.response.MemberResponseDto;
import com.pair.website.jwt.TokenProvider;
import com.pair.website.repository.MemberRepository;
import com.pair.website.repository.PairBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final PairBoardRepository pairBoardRepository;
    private final PairBoardService pairBoardService;
    private final PasswordEncoder passwordEncoder;
    private final AwsS3Uploader awsS3Uploader;


    public ResponseEntity<?> signup(@Valid MemberRequestDto requestDto) {
        if (isPresentMember(requestDto.getEmail()) != null) {
            return new ResponseEntity<>(BaseResponseDto.fail("DUPLICATED_EMAIL",
                    "중복된 아이디입니다."), HttpStatus.BAD_REQUEST);
        }

        if (checkNickname(requestDto.getNickname()) != null) {
            return new ResponseEntity<>(BaseResponseDto.fail("DUPLICATED_NICKNAME",
                    "중복된 닉네임입니다."), HttpStatus.BAD_REQUEST);
        }
        Member member = Member.builder()
                .email(requestDto.getEmail())
                .nickname(requestDto.getNickname())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();
        memberRepository.save(member);

        return new ResponseEntity<>(BaseResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .email(member.getEmail())
                        .nickname(member.getNickname())
                        .profileImage(member.getProfileImage())
                        .githubLink(member.getGithubLink())
                        .createdAt(member.getCreatedAt())
                        .build()), HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<?> login(@Valid LoginRequestDto loginRequestDto, HttpServletResponse response) {
        Member member = isPresentMember(loginRequestDto.getEmail());
        if (null == member) {
            return new ResponseEntity<>(BaseResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다."),HttpStatus.NOT_FOUND);
        }

        if (!member.validatePassword(passwordEncoder, loginRequestDto.getPassword())) {
            return new ResponseEntity<>(BaseResponseDto.fail("INVALID_MEMBER", "사용자를 찾을 수 없습니다."),HttpStatus.NOT_FOUND);
        }


        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response);

        return new ResponseEntity<>(BaseResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .email(member.getEmail())
                        .nickname(member.getNickname())
                        .profileImage(member.getProfileImage())
                        .githubLink(member.getGithubLink())
                        .createdAt(member.getCreatedAt())
                        .build()),HttpStatus.OK);
    }

    // 마이페이지 조회
    public BaseResponseDto<?> getProfile(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new NotFoundException("NOT_FOUND_MEMBER")
        );
        List<PairBoard> boards = pairBoardRepository.findAllByMemberId(member.getId());
        List<BoardAllResponseDto> boardAllResponseDtos = new ArrayList<>();

        for (PairBoard board : boards) {
            pairBoardService.boardList(board,boardAllResponseDtos);
        }

        return BaseResponseDto.success(MemberResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .githubLink(member.getGithubLink())
                .createdAt(member.getCreatedAt())
                .boardList(boardAllResponseDtos)
                .build());
    }

    // 다른 멤버 마이페이지 조회
    public ResponseEntity<?> getOtherProfile(String nickname) {
        Member member = memberRepository.findByNickname(nickname).orElseThrow(
                () -> new NotFoundException("NOT_FOUND_MEMBER")
        );

        List<PairBoard> boards = pairBoardRepository.findAllByMemberId(member.getId());
        List<BoardAllResponseDto> boardAllResponseDtos = new ArrayList<>();

        for (PairBoard board : boards) {
            pairBoardService.boardList(board,boardAllResponseDtos);
        }

        return new ResponseEntity<>(BaseResponseDto.success(MemberResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .githubLink(member.getGithubLink())
                .createdAt(member.getCreatedAt())
                .boardList(boardAllResponseDtos)
                .build()), HttpStatus.OK);
    }

    // 프로필 수정
    @Transactional
    public BaseResponseDto<?> profileUpdate(MultipartFile image, ProfileRequestDto requestDto, Long id) throws IOException {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new NullPointerException("NOT_FOUND_MEMBER")
        );


        if (!member.getNickname().equals(requestDto.getNickname())  && checkNickname(requestDto.getNickname()) != null)
            return BaseResponseDto.fail("DUPLICATED_NICKNAME", "중복된 닉네임 입니다.");
        String storedFileName = null;
        if(image == null)
            storedFileName = member.getProfileImage();
        else if (!image.isEmpty()) {
            storedFileName = awsS3Uploader.upload(image, "images");
            // member.setProfileImage(storedFileName);
        }

        member.update(ProfileRequestDto.builder()
                .nickname(requestDto.getNickname())
                .githubLink(requestDto.getGithubLink())
                .build(), storedFileName);

        List<PairBoard> boards = pairBoardRepository.findAllByMemberId(member.getId());
        List<BoardAllResponseDto> boardAllResponseDtos = new ArrayList<>();

        for (PairBoard board : boards) {
            pairBoardService.boardList(board,boardAllResponseDtos);
        }

        return BaseResponseDto.success(MemberResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .githubLink(member.getGithubLink())
                .boardList(boardAllResponseDtos)
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

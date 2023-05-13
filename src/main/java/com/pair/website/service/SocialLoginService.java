package com.pair.website.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pair.website.dto.TokenDto;
import com.pair.website.jwt.JwtProperties;
import com.pair.website.domain.Member;
import com.pair.website.dto.BaseResponseDto;
import com.pair.website.dto.LoginResponseDto;
import com.pair.website.dto.kakao.KakaoTokenDto;
import com.pair.website.dto.kakao.KakaoAccountDto;
import com.pair.website.jwt.TokenProvider;
import com.pair.website.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialLoginService {

    @Autowired
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    // 환경변수로 바꾸기
    @Value("${kakao.clientId}")
    String KAKAO_CLIENT_ID;
    @Value("${kakao.redirectUrl}")
    String KAKAO_REDIRECT_URI;

    // 닉네임 중복처리를 위해 int max부터 내림차순으로 #id추가 ex)제리 #9999999
    int maxID = 10000000;

    public BaseResponseDto<?> kakaoLogin(String code, HttpServletResponse response) {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code).getAccess_token();

        // 2. 토큰으로 카카오 API 호출
        KakaoAccountDto kakaoAccountDto = getKakaoUserInfo(accessToken);

        // 3. 카카오ID로 회원가입 처리
        Member member = registerKakaoUserIfNeed(kakaoAccountDto);

        // 4. member로 jwt 토큰 만들기
        // createToken(member);
        //tokenToHeaders(jwtToken, response);

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response);

        LoginResponseDto loginResponseDto = LoginResponseDto.builder().loginSuccess(true).id(member.getId()).nickname(member.getNickname()).profileImage(member.getProfileImage()).createdAt(member.getCreatedAt()).build();
        return BaseResponseDto.success(loginResponseDto);
    }

    // 1. "인가 코드"로 "액세스 토큰" 요청
    private KakaoTokenDto getAccessToken(String code) {
        // HTTP Header 생성
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", KAKAO_CLIENT_ID);
        body.add("redirect_uri", KAKAO_REDIRECT_URI);
        body.add("code", code); // 프론트에서 받아오는 인가코드

        // 헤더와 바디 합치기 위해 HttpEntity 객체 생성
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);

        // 카카오로부터 Access token 수신
        ResponseEntity<String> accessTokenResponse = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // JSON Parsing (-> KakaoTokenDto)
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoTokenDto kakaoTokenDto = null;
        try {
            kakaoTokenDto = objectMapper.readValue(accessTokenResponse.getBody(), KakaoTokenDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoTokenDto;
    }

    /* kakaoAccessToken 으로 카카오 서버에 정보 요청 */
    private KakaoAccountDto getKakaoUserInfo(String kakaoAccessToken) {

        // HTTP Header 생성
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoAccessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> accountInfoRequest = new HttpEntity<>(headers);
        ResponseEntity<String> accountInfoResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                accountInfoRequest,
                String.class
        );

        // JSON Parsing (-> kakaoAccountDto)
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoAccountDto kakaoAccountDto = null;
        try {
            kakaoAccountDto = objectMapper.readValue(accountInfoResponse.getBody(), KakaoAccountDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoAccountDto;
    }

    // 3. 카카오ID로 회원가입 처리
    private Member registerKakaoUserIfNeed(KakaoAccountDto kakaoAccountDto) {

        // DB 에 중복된 email이 있는지 확인
        String kakaoEmail = kakaoAccountDto.getKakao_account().getEmail();
        Member member = memberRepository.findByEmail(kakaoEmail)
                .orElse(null);

        maxID = maxID - 1;

        if (member == null) {
            // 회원가입
            String kakaoName = kakaoAccountDto.getKakao_account().getProfile().getNickname();
            String kakaoImage = kakaoAccountDto.getKakao_account().getProfile().getProfile_image_url();
            member = Member.builder().email(kakaoEmail).nickname(kakaoName + " #" + maxID).profileImage(kakaoImage).password("password").build();
            //비밀번호 암호화 로직 추가하기

            memberRepository.save(member);
        }

        return member;
    }

    public String createToken(Member member) {
        // Jwt 생성 후 헤더에 추가해서 보내줌
        String jwtToken = JWT.create()
                .withSubject(member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("id", member.getId())
                .withClaim("nickname", member.getNickname())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        return jwtToken;
    }

    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("refreshToken", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }
}

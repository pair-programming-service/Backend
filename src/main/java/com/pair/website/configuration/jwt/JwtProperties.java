package com.pair.website.configuration.jwt;

public interface JwtProperties {
    String SECRET = "Codingmate"; // .gitIgnore로 수정하기
    int EXPIRATION_TIME = 864000000; //60000 1분 //864000000 10일
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
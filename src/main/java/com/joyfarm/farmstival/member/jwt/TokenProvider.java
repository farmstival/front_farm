package com.joyfarm.farmstival.member.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class TokenProvider {

    // 토큰 생성하기
    public String createToken(){
        return null;
    }

    // 토큰 정보로 인증한 회원 정보 가져오기
    public Authentication getAuthentication(String token){
        return null;
    }

    // 토큰 검증 (유효한지, 만료되지 않았는지)
    public void validateToken(String token){

    }
}

package com.joyfarm.farmstival.member.jwt;

import com.joyfarm.farmstival.member.MemberInfo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class TokenProvider {

    private final JwtProperties properties;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    /**
     * JTW 토큰 생성
     * @param email
     * @param password
     * @return
     */
    public String createToken(String email, String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
        if(authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails){
            //로그인 성공시(유효한 사용자) -> JWT 토큰 "발급"
            long now = (new Date()).getTime(); // 현재 시간
            Date validity = new Date(now + properties.getValidSeconds() * 1000);
            //현재 발급받은 시간에서 한 시간동안 유효

            return Jwts.builder()
                    .setSubject(authentication.getName()) // 사용자 email(인증 완료)
                    .signWith(getKey(), SignatureAlgorithm.HS512) // 데이터 위변조 방지
                    .expiration(validity)
                    .compact();
        }
        return null;
    }

    // 토큰 정보로 인증한 회원 정보 가져오기
    public Authentication getAuthentication(String token){
        return null;
    }

    // 토큰 검증 (유효한지, 만료되지 않았는지)
    public void validateToken(String token){

    }

    private Key getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(properties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

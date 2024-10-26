package com.example.researchlab.config.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil{

    private final Key key;

    // 프로퍼티에서 비밀키를 주입
    public JwtUtil(@Value("jwt.secret") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // JWT 토큰 생성
    public String generateToken(String username) {
        return Jwts.builder()
                   .setSubject(username)
                   .setIssuedAt(new Date())
                   .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 만료시간 1시간 세팅
                   .signWith(key, SignatureAlgorithm.HS256)
                   .compact();
    }

    // JWT에서 사용자 이름 추출
    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }

    // JWT 유효성 검증
    public boolean validateToken(String token, String username){
        return (extractUsername(token).equals(username) && !isTokenExpired(token));
    }

    // 토큰 만료 여부 확인
    public boolean isTokenExpired(String token){
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // Claims 정보 추출
    public Claims extractAllClaims(String token){
        try {
            return Jwts.parserBuilder()
                       .setSigningKey(key)
                       .build()
                       .parseClaimsJws(token)
                       .getBody();
        } catch (io.jsonwebtoken.io.IOException e){
            throw new RuntimeException("JWT parsing error", e);
        }
    }


}

package com.example.researchlab.config.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

    private final Key key;

    /**
     * 생성자: jwt.secret 프로퍼티에서 비밀키를 주입받아 초기화합니다.
     * 길이가 짧을 경우에는 자동으로 안전한 키를 생성하여 사용합니다.
     *
     * @param secretKey 외부에서 주입받은 시크릿 키
     */
    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        // 비밀 키 설정 및 자동 생성 조건
        if (secretKey.length() < 32) {
            this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);  // 32바이트 이상 자동 생성
            log.info("JWT 시크릿 키가 짧아서 자동 생성된 키를 사용합니다.");
        } else {
            this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));  // 설정된 키 사용
            log.info("외부 시크릿 키 사용");
        }
    }

    /**
     * JWT 토큰 생성 메서드: 사용자 이름을 기반으로 JWT 토큰을 생성합니다.
     *
     * @param username 사용자 이름
     * @return 생성된 JWT 토큰
     */
    public String generateToken(String username) {
        log.info("JWT 토큰 생성: 사용자 - {}", username);
        return Jwts.builder()
                   .setSubject(username)
                   .setIssuedAt(new Date())
                   .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 만료시간 1시간 설정
                   .signWith(key, SignatureAlgorithm.HS256)
                   .compact();
    }

    /**
     * JWT 토큰에서 사용자 이름을 추출하는 메서드
     *
     * @param token JWT 토큰
     * @return 추출된 사용자 이름
     */
    public String extractUsername(String token) {
        log.debug("JWT에서 사용자 이름 추출 시도");
        return extractAllClaims(token).getSubject();
    }

    /**
     * JWT 토큰 유효성 검증 메서드: 사용자 이름 일치 여부와 만료 여부를 확인합니다.
     *
     * @param token JWT 토큰
     * @return 토큰이 유효한 경우 true, 그렇지 않은 경우 false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("JWT 유효성 검사 실패: {}", e.getMessage());
            return false;
        }
    }
    /**
     * 토큰 만료 여부를 확인하는 메서드
     *
     * @param token JWT 토큰
     * @return 만료된 경우 true, 그렇지 않은 경우 false
     */
    public boolean isTokenExpired(String token) {
        boolean isExpired = extractAllClaims(token).getExpiration().before(new Date());
        log.debug("JWT 만료 상태: {}", isExpired ? "만료됨" : "유효함");
        return isExpired;
    }

    /**
     * JWT에서 모든 Claims 정보를 추출하는 메서드
     *
     * @param token JWT 토큰
     * @return Claims 객체
     */
    public Claims extractAllClaims(String token) {
        try {
            log.debug("JWT Claims 추출 시도");
            return Jwts.parserBuilder()
                       .setSigningKey(key)
                       .build()
                       .parseClaimsJws(token)
                       .getBody();
        } catch (io.jsonwebtoken.io.IOException e) {
            log.error("JWT Claims 추출 오류", e);
            throw new RuntimeException("JWT parsing error", e);
        }
    }

    /**
     * 리프레시토큰 생성
     * @param username
     * @return
     */
    public String generateRefreshToken(String username) {
        log.info("리프레시 토큰 생성: 사용자 - {}", username);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 7일 만료
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 리프레시토큰 검증
     * @param token
     * @return
     */
    public boolean validateRefreshToken(String token) {
        return validateToken(token);
    }


}

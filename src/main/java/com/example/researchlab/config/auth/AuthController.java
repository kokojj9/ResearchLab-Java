package com.example.researchlab.config.auth;

import com.example.researchlab.member.model.service.MemberService;
import com.example.researchlab.member.model.vo.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    // 로그인 메서드
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid Member member, HttpServletResponse response) {
        try {
            // Spring Security를 통한 사용자 인증
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(member.getMemberId(), member.getMemberPwd())
            );

            Member loginMember = memberService.login(member);
            if (loginMember == null) {
                return ResponseEntity.status(401).body("존재하지 않는 회원 또는 잘못된 비밀번호");
            }

            // JWT 토큰 생성 및 쿠키에 저장
            String token = jwtUtil.generateToken(loginMember.getMemberId());
            setJwtCookie(response, token);
            return ResponseEntity.ok("로그인 성공");

        } catch (AuthenticationException e) {
            log.error("인증 실패: {}", e.getMessage());
            return ResponseEntity.status(401).body("인증 실패");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        // 쿠키에서 토큰 제거
        removeJwtCookie(response);
        return ResponseEntity.ok("로그아웃 성공");
    }
    // JWT 토큰을 HttpOnly 쿠키에 저장
    private static void setJwtCookie(HttpServletResponse response, String token) {
        log.info("토큰 저장: {}", token);
        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true);       // 자바스크립트로 접근 불가
        jwtCookie.setSecure(false);        // HTTPS 환경에서만 전송 (개발 환경에서는 false)
        jwtCookie.setPath("/");            // 모든 경로에서 접근 가능
        jwtCookie.setMaxAge(24 * 60 * 60); // 쿠키 유효 기간: 1일
        response.addCookie(jwtCookie);     // 응답에 쿠키 추가
    }

    // 쿠키에서 JWT 토큰 제거 (로그아웃)
    private static void removeJwtCookie(HttpServletResponse response) {
        log.info("토큰 삭제");
        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0); // 쿠키 제거
        response.addCookie(jwtCookie);
    }
}


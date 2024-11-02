package com.example.researchlab.config.auth;

import com.example.researchlab.common.model.vo.ResponseData;
import com.example.researchlab.member.model.service.MemberService;
import com.example.researchlab.member.model.vo.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

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
    public ResponseData<Object> login(@RequestBody @Valid Member member, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(member.getMemberId(), member.getMemberPwd())
            );

            Member loginMember = memberService.findMemberById(member.getMemberId());
            if (loginMember == null) {
                return ResponseData.builder().resultMessage("사용자가 존재하지 않습니다.").build();
            }

            // JWT 액세스 토큰과 리프레시 토큰 생성
            String accessToken = jwtUtil.generateToken(loginMember.getMemberId());
            String refreshToken = jwtUtil.generateRefreshToken(loginMember.getMemberId());

            // HttpOnly 쿠키에 액세스 토큰과 리프레시 토큰 저장
            setJwtCookie(response, accessToken, "jwt");
            setJwtCookie(response, refreshToken, "refreshToken");

            return ResponseData.builder().data(loginMember).resultMessage("로그인 성공").build();

        } catch (AuthenticationException e) {
            log.error("인증 실패: {}", e.getMessage());
            return ResponseData.builder().resultMessage("인증 실패").build();
        }
    }

    @PostMapping("/refresh")
    public ResponseData<Object> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie refreshTokenCookie = WebUtils.getCookie(request, "refreshToken");
        String refreshToken = (refreshTokenCookie != null) ? refreshTokenCookie.getValue() : null;

        if (refreshToken != null && jwtUtil.validateRefreshToken(refreshToken)) {
            String username = jwtUtil.extractUsername(refreshToken);
            String newAccessToken = jwtUtil.generateToken(username);
            setJwtCookie(response, newAccessToken, "jwt"); // 새 액세스 토큰 쿠키에 저장
            return ResponseData.builder().resultMessage("새 액세스 토큰 발급").build();
        } else {
            return ResponseData.builder().resultMessage("리프레시 토큰이 유효하지 않음").build();
        }
    }

    @PostMapping("/logout")
    public ResponseData<Object> logout(HttpServletResponse response) {
        // 쿠키에서 토큰 제거
        removeJwtCookie(response);
        return ResponseData.builder().resultMessage("사용자 로그아웃").build();
    }

    @GetMapping("/checkJwt")
    public ResponseData<Object> checkJwt(HttpServletRequest request) {
        log.info("jwt 체크 시도");
        Cookie jwtCookie = WebUtils.getCookie(request, "jwt");
        String token = (jwtCookie != null) ? jwtCookie.getValue() : null;

        if(token == null) return ResponseData.builder().resultMessage("jwt토큰 없음").build();

        try {
            // JWT 토큰 유효성 검증
            if (jwtUtil.validateToken(token, "")){
                String memberId = jwtUtil.extractUsername(token);
                Member member = new Member();
                member.setMemberId(memberId);
                member = memberService.login(member);

                if(member != null){
                    return ResponseData.builder().data(member).resultMessage("토큰 인증 성공").build();
                } else {
                    return ResponseData.builder().data(member).resultMessage("회원정보를 찾을 수 없습니다.").build();
                }
            } else {
                return ResponseData.builder().resultMessage("유효하지않은 토큰입니다.").build();
            }
        } catch (Exception e) {
            log.error("jwt 검증 오류");
            return ResponseData.builder().resultMessage("jwt토큰 검증 실패").build();
        }
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


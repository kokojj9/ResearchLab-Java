package com.example.researchlab.config.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * JWT 토큰을 통해 요청의 인증 상태를 확인하고, 유효한 토큰이 있을 경우 SecurityContext에 인증 정보를 설정합니다.
     *
     * @param request  HTTP 요청 객체로, 헤더에서 JWT 토큰을 추출하기 위해 사용됩니다.
     * @param response HTTP 응답 객체
     * @param filterChain 요청-응답 필터 체인으로, 이 필터에서 인증 절차를 마친 후 다른 필터로 요청을 전달합니다.
     * @throws ServletException 요청 처리 중에 발생할 수 있는 예외
     * @throws IOException 입출력 과정에서 발생할 수 있는 예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("JWT Filter 실행 - 요청 URI: {}", request.getRequestURI());

        final String authorizationHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            log.info("Authorization 헤더에서 JWT 추출: {}", jwt);
            username = jwtUtil.extractUsername(jwt); // 한 번만 호출
        } else {
            log.info("Authorization 헤더가 없거나 Bearer 토큰이 아님");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("인증되지 않은 요청 - JWT 검증 중");
            if (jwtUtil.validateToken(jwt, username)) {  // 유효성 검사
                log.info("JWT 유효성 검사 성공 - 사용자: {}", username);
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("SecurityContext에 인증 객체 설정 완료");
            } else {
                log.warn("JWT 유효성 검사 실패");
            }
        }
        filterChain.doFilter(request, response);
        log.info("JWT Filter 완료 - 다음 필터로 진행");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.equals("/auth/login");
    }



}

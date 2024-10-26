package com.example.researchlab.member.controller;

import com.example.researchlab.common.model.vo.ResponseData;
import com.example.researchlab.config.auth.JwtUtil;
import com.example.researchlab.member.model.service.MemberService;
import com.example.researchlab.member.model.vo.Member;
import com.example.researchlab.template.ResponseTemplate;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final ResponseTemplate responseTemplate;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseData<Object> login(@RequestBody @Valid Member member, HttpServletResponse response, BindingResult br) {
        log.info("회원 로그인 시도: {}", member.getMemberId());

        ResponseData.ResponseDataBuilder<Object> responseDataBuilder = ResponseData.builder();

        if (br.hasErrors()) {
            log.warn("유효성 검사 오류: {}", br.getAllErrors());
            return responseDataBuilder.responseCode("NN")
                                      .resultMessage("잘못된 로그인 정보")
                                      .data(null)
                                      .build();
        }

        Member loginMember = memberService.login(member);
        if (loginMember == null) {
            log.warn("존재하지 않는 회원: {}", member.getMemberId());
            return responseDataBuilder.responseCode("NN")
                                      .resultMessage("존재하지 않는 회원")
                                      .data(null)
                                      .build();
        }

        // 로그인 성공 시 JWT 토큰 생성
        log.info("로그인 성공: {}", member.getMemberId());
        String token = jwtUtil.generateToken(member.getMemberId());

        // HTTP-only 쿠키에 JWT 설정
        setJwtCookie(response, token);

        return responseDataBuilder.responseCode("YY")
                                  .resultMessage("로그인 성공")
                                  .data(loginMember)
                                  .build();
    }

    private static void setJwtCookie(HttpServletResponse response, String token) {
        log.info("토큰 저장: {}", token);
        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true);       // 자바스크립트로 접근 불가
        jwtCookie.setSecure(false);         // HTTPS 환경에서만 전송
        jwtCookie.setPath("/");            // 모든 경로에서 접근 가능
        jwtCookie.setMaxAge(24 * 60 * 60); // 쿠키의 유효 기간 설정 1일
        response.addCookie(jwtCookie);     // 응답에 쿠키 추가
    }

    @PostMapping("/enroll")
    public ResponseData<Object> enrollMember(@RequestBody @Valid Member member, BindingResult br) {
        log.info("회원가입 시도: {}", member.getMemberId());
        ResponseData.ResponseDataBuilder<Object> responseDataBuilder = ResponseData.builder();

        if (br.hasErrors()) {
            log.warn("유효성 검사 오류: {}", br.getAllErrors());
            return responseDataBuilder.responseCode("NN")
                                      .resultMessage("잘못된 회원가입 정보")
                                      .data(null)
                                      .build();
        }

        int result = memberService.enrollMember(member);
        if (result > 0) {
            log.info("회원가입 성공: {}", member.getMemberId());
            return responseDataBuilder.responseCode("YY")
                                      .resultMessage("회원가입 성공")
                                      .data(null)
                                      .build();
        } else {
            log.warn("회원가입 실패: {}", member.getMemberId());
            return responseDataBuilder.responseCode("NN")
                                      .resultMessage("회원가입 실패")
                                      .data(null)
                                      .build();
        }
    }
}

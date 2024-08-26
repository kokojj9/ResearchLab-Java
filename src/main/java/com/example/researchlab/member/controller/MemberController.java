package com.example.researchlab.member.controller;

import com.example.researchlab.common.model.vo.ResponseData;
import com.example.researchlab.member.model.service.MemberService;
import com.example.researchlab.member.model.vo.Member;
import com.example.researchlab.template.ResponseTemplate;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final ResponseTemplate responseTemplate;
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @PostMapping("/login")
    public ResponseEntity<ResponseData<Object>> login(@RequestBody @Valid Member member, HttpSession session, BindingResult br) {
        logger.info("회원 로그인 시도: {}", member.getMemberId());

<<<<<<< Updated upstream
        if (br.hasErrors()) {
            logger.warn("유효성 검사 오류: {}", br.getAllErrors());
            return responseTemplate.fail("잘못된 정보", HttpStatus.BAD_REQUEST);
        } else {
            Member loginMember = memberService.login(member);

            if (loginMember != null) {
                loginMember.setMemberPwd(null);
                session.setAttribute("loginMember", loginMember);
                logger.info("로그인 성공: {}", member.getMemberId());
                return responseTemplate.success("로그인 성공", null, HttpStatus.OK);
            }

            logger.warn("로그인 실패: {}", member.getMemberId());
            return responseTemplate.success("존재하지 않는 회원", null, HttpStatus.OK);
        }
    }

    @GetMapping("/getSession")
    public ResponseEntity<ResponseData<Object>> getSessionInfo(HttpSession session) {
        Member member = (Member) session.getAttribute("loginMember");

        if (member != null) {
            logger.info("유효한 세션: {}", member.getMemberId());
            return responseTemplate.success("유효한 세션", member, HttpStatus.OK);
        }
        logger.warn("세션 없음");
        return responseTemplate.success("세션 없음", null, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseData<Object>> logout(HttpSession session) {
        logger.info("로그아웃");
        session.invalidate();
        return responseTemplate.success("로그아웃 성공", null, HttpStatus.OK);
    }

=======
        ResponseData.ResponseDataBuilder<Object> responseDataBuilder = ResponseData.builder();

        if (br.hasErrors()) {
            logger.warn("유효성 검사 오류: {}", br.getAllErrors());
            responseDataBuilder.responseCode("NN")
                               .resultMessage("잘못된 로그인 정보")
                                .data(null);
        } else {
            Member loginMember = memberService.login(member);
            if (loginMember == null) {
                logger.warn("존재하지 않는 회원: {}", member.getMemberId());
                responseDataBuilder.responseCode("NN")
                                   .resultMessage("존재하지 않는 회원")
                                   .data(null);
            } else {
                logger.info("로그인 성공: {}", member.getMemberId());
                loginMember.setMemberPwd(null);
                responseDataBuilder.responseCode("YY")
                                   .resultMessage("로그인 성공")
                                   .data(loginMember);
            }
        }

        return responseDataBuilder.build();
    }

>>>>>>> Stashed changes
    @PostMapping("/enroll")
    public ResponseEntity<ResponseData<Object>> enrollMember(@RequestBody @Valid Member member, BindingResult br) {
        logger.info("회원가입 시도: {}", member.getMemberId());
<<<<<<< Updated upstream

        if (br.hasErrors()) {
            logger.warn("유효성 검사 오류: {}", br.getAllErrors());
            return responseTemplate.fail("잘못된 정보", HttpStatus.BAD_REQUEST);
        }

        int result = memberService.enrollMember(member);

        if (result > 0) {
            logger.info("회원가입 성공: {}", member.getMemberId());
            return responseTemplate.success("회원가입 성공", null, HttpStatus.OK);
        }

        logger.warn("회원가입 실패: {}", member.getMemberId());
        return responseTemplate.success("회원가입 실패", null, HttpStatus.OK);
=======
        ResponseData.ResponseDataBuilder<Object> responseDataBuilder =  ResponseData.builder();

        if (br.hasErrors()) {
            logger.warn("유효성 검사 오류: {}", br.getAllErrors());
            responseDataBuilder.responseCode("NN")
                               .resultMessage("잘못된 회원가입 정보")
                               .data(null);
        } else {
            int result = memberService.enrollMember(member);

            if (result > 0) {
                logger.info("회원가입 성공: {}", member.getMemberId());
                responseDataBuilder.responseCode("YY")
                                   .resultMessage("회원가입 성공")
                                   .data(null);
            } else {
                logger.warn("회원가입 실패: {}", member.getMemberId());
                responseDataBuilder.responseCode("NN")
                                   .resultMessage("회원가입 실패")
                                   .data(null);
            }
        }

        return responseDataBuilder.build();
>>>>>>> Stashed changes
    }
}

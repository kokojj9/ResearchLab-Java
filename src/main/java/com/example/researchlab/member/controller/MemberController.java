package com.example.researchlab.member.controller;

import com.example.researchlab.common.model.vo.ResponseData;
import com.example.researchlab.member.model.service.MemberService;
import com.example.researchlab.member.model.vo.Member;
import com.example.researchlab.template.ResponseTemplate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final ResponseTemplate responseTemplate;
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @PostMapping("/login")
    public ResponseData<Object> login(@RequestBody @Valid Member member, BindingResult br) {
        logger.info("회원 로그인 시도: {}", member.getMemberId());

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

    @PostMapping("/enroll")
    public ResponseData<Object> enrollMember(@RequestBody @Valid Member member, BindingResult br) {
        logger.info("회원가입 시도: {}", member.getMemberId());
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
    }
}

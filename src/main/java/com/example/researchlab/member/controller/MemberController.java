package com.example.researchlab.member.controller;

import com.example.researchlab.common.model.vo.ResponseData;
import com.example.researchlab.template.ResponseTemplate;
import com.example.researchlab.member.model.service.MemberService;
import com.example.researchlab.member.model.vo.Member;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final BCryptPasswordEncoder bc;
    private final MemberService memberService;
    private final ResponseTemplate responseTemplate;

    //로그인
    @PostMapping("/login")
    public ResponseEntity<ResponseData<Object>> login(@RequestBody @Valid Member member, HttpSession session, BindingResult br) {
        // 추후에 JWT활용 로그인 방식으로 변경
        if(br.hasErrors()){
            return responseTemplate.fail("invalid info", HttpStatus.BAD_REQUEST);
        } else {
            Member loginMember = memberService.login(member);

            if(loginMember != null) {
                return responseTemplate.success("login success", loginMember, HttpStatus.OK);
            }

            return responseTemplate.success("undefined member", null, HttpStatus.OK);
        }
    }

    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<ResponseData<Object>> lopgout() {
        
        return null;
    }
    
    //회원가입

    @PostMapping("/enroll")
    public ResponseEntity<ResponseData<Object>> enrollMember(@RequestBody @Valid Member member, BindingResult br){
        if(br.hasErrors()){
            return responseTemplate.fail("invalid info", HttpStatus.BAD_REQUEST);
        } else {
            int result = memberService.enrollMember(member);

            if(result > 0) {
                return responseTemplate.success("enroll success", null, HttpStatus.OK);
            }

            return responseTemplate.success("enroll fail", null, HttpStatus.OK);
        }
    }
    //정보수정
    //탈퇴



}

package com.example.researchlab.member.controller;

import com.example.researchlab.common.model.vo.ResponseData;
import com.example.researchlab.member.model.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
//@RequestMapping()
public class MemberController {

    private final BCryptPasswordEncoder bc;
    private final MemberService memberService;

    //로그인
    public ResponseEntity<ResponseData> login(){
        return null;
    }
    //로그아웃
    //회원가입
    //정보수정
    //탈퇴



}

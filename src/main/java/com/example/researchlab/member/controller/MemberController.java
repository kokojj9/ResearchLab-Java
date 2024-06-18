package com.example.researchlab.member.controller;

import com.example.researchlab.common.model.vo.ResponseData;
import com.example.researchlab.member.model.service.MemberService;
import com.example.researchlab.member.model.vo.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final BCryptPasswordEncoder bc;
    private final MemberService memberService;

    //로그인
    @PostMapping("/login")
    public ResponseEntity<ResponseData> login(@RequestBody Member member, HttpSession session) {

        ResponseData rd = new ResponseData();
        rd = ResponseData.builder()
                .data(member)
                .resultMessage("통신성공")
                .build();



        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<ResponseData>(rd, headers, HttpStatus.OK);
    }
    //로그아웃
    //회원가입

    @PostMapping("/enroll")
    public ResponseEntity<ResponseData> enrollMember(@RequestBody Member member){
        return null;
    }
    //정보수정
    //탈퇴



}

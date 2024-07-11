package com.example.researchlab.member.controller;

import com.example.researchlab.common.model.vo.ResponseData;
import com.example.researchlab.member.model.service.MemberService;
import com.example.researchlab.member.model.vo.Member;
import com.example.researchlab.template.ResponseTemplate;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MemberControllerTest {

    @InjectMocks
    private MemberController memberController;

    @Mock
    private MemberService memberService;

    @Mock
    private ResponseTemplate responseTemplate;

    @Mock
    private HttpSession session;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoginSuccess() {
        Member member = new Member();
        member.setMemberId("testuser");
        member.setMemberPwd("password");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(memberService.login(any(Member.class))).thenReturn(member);
        when(responseTemplate.success(anyString(), any(), any(HttpStatus.class))).thenReturn(new ResponseEntity<>(new ResponseData<>(), HttpStatus.OK));

        ResponseEntity<ResponseData<Object>> response = memberController.login(member, session, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(session).setAttribute("loginMember", member);
        verify(responseTemplate).success("로그인 성공", null, HttpStatus.OK);
    }

    @Test
    public void testLoginFail() {
        Member member = new Member();
        member.setMemberId("testUser");
        member.setMemberPwd("password");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(memberService.login(any(Member.class))).thenReturn(null);
        when(responseTemplate.success(anyString(), any(), any(HttpStatus.class))).thenReturn(new ResponseEntity<>(new ResponseData<>(), HttpStatus.OK));

        ResponseEntity<ResponseData<Object>> response = memberController.login(member, session, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(responseTemplate).success("존재하지 않는 회원", null, HttpStatus.OK);
    }

    @Test
    public void testEnrollSuccess() {
        Member member = new Member();
        member.setMemberId("testUser");
        member.setMemberPwd("password");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(memberService.enrollMember(any(Member.class))).thenReturn(1);
        when(responseTemplate.success(anyString(), any(), any(HttpStatus.class))).thenReturn(new ResponseEntity<>(new ResponseData<>(), HttpStatus.OK));

        ResponseEntity<ResponseData<Object>> response = memberController.enrollMember(member, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(responseTemplate).success("회원가입 성공", null, HttpStatus.OK);
    }

    @Test
    public void testEnrollFail() {
        Member member = new Member();
        member.setMemberId("testUser");
        member.setMemberPwd("password");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(memberService.enrollMember(any(Member.class))).thenReturn(0);
        when(responseTemplate.success(anyString(), any(), any(HttpStatus.class))).thenReturn(new ResponseEntity<>(new ResponseData<>(), HttpStatus.OK));

        ResponseEntity<ResponseData<Object>> response = memberController.enrollMember(member, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(responseTemplate).success("회원가입 실패", null, HttpStatus.OK);
    }
}

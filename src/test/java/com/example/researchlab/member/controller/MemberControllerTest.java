package com.example.researchlab.member.controller;

import com.example.researchlab.common.model.vo.ResponseData;
import com.example.researchlab.member.model.service.MemberService;
import com.example.researchlab.member.model.vo.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MemberControllerTest {

    @Mock
    private MemberService memberService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private MemberController memberController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoginSuccess() {
        // Given
        Member member = new Member();
        member.setMemberId("testUser");
        member.setMemberPwd("password123");

        Member loginMember = new Member();
        loginMember.setMemberId("testUser");
        loginMember.setMemberPwd(null);  // 비밀번호를 null로 설정

        when(memberService.login(any(Member.class))).thenReturn(loginMember);
        when(bindingResult.hasErrors()).thenReturn(false); // 유효성 검사 통과

        // When
        ResponseData<Object> rd = memberController.login(member, bindingResult);

        // Then
        verify(memberService).login(any(Member.class));
        assertNotNull(rd);
        assertEquals("YY", rd.getResponseCode());
        assertEquals("로그인 성공", rd.getResultMessage());
        assertEquals("testUser", ((Member) rd.getData()).getMemberId());
        assertNull(((Member) rd.getData()).getMemberPwd()); // 비밀번호는 null이어야 함
    }

}

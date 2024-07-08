package com.example.researchlab.member.model.service;

import com.example.researchlab.member.model.dao.MemberMapper;
import com.example.researchlab.member.model.vo.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MemberServiceImplTest {

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberMapper memberMapper;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoginSuccess() {
        Member member = new Member();
        member.setMemberId("testuser");
        member.setMemberPwd("password");

        Member returnedMember = new Member();
        returnedMember.setMemberId("testuser");
        returnedMember.setMemberPwd("$2a$10$Dow0RkzVgFtjfXKwji8gfOZlbZ.YrVWZfQKoFbY/Z0ZzKxv4k7WzG"); // BCrypt hashed password

        // memberMapper.login() 호출 시 returnedMember 반환
        when(memberMapper.login(any(Member.class))).thenReturn(returnedMember);
        // bCryptPasswordEncoder.matches() 호출 시 true 반환
        when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // 실제 테스트 실행
        Member result = memberService.login(member);

        // 결과 검증
        assertNotNull(result);
        assertEquals("testuser", result.getMemberId());
    }

    @Test
    public void testLoginFail() {
        Member member = new Member();
        member.setMemberId("testuser");
        member.setMemberPwd("wrongpassword");

        // memberMapper.login() 호출 시 null 반환
        when(memberMapper.login(any(Member.class))).thenReturn(null);

        // 실제 테스트 실행
        Member result = memberService.login(member);

        // 결과 검증
        assertNull(result);
    }

    @Test
    public void testEnrollMember() {
        Member member = new Member();
        member.setMemberId("testuser");
        member.setMemberPwd("password");

        // bCryptPasswordEncoder.encode() 호출 시 "hashedpassword" 반환
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("hashedpassword");
        // memberMapper.enrollMember() 호출 시 1 반환
        when(memberMapper.enrollMember(any(Member.class))).thenReturn(1);

        // 실제 테스트 실행
        int result = memberService.enrollMember(member);

        // 결과 검증
        assertEquals(1, result);
        assertEquals("hashedpassword", member.getMemberPwd());
    }
}

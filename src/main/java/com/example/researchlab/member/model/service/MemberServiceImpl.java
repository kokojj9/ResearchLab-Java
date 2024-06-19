package com.example.researchlab.member.model.service;

import com.example.researchlab.member.model.dao.MemberMapper;
import com.example.researchlab.member.model.vo.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberMapper memberMapper;
    private final BCryptPasswordEncoder bc;
    @Override
    public Member login(Member member) {
        Member loginMember = memberMapper.login(member);

        if (loginMember != null && bc.matches(member.getMemberPwd(), loginMember.getMemberPwd())){
           return loginMember;
        }

        return null;
    }




}

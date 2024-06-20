package com.example.researchlab.member.model.service;

import com.example.researchlab.member.model.vo.Member;

public interface MemberService {

    Member login(Member member);

    int enrollMember(Member member);
}

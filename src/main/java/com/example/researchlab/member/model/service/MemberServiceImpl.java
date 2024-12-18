package com.example.researchlab.member.model.service;

import com.example.researchlab.member.model.dao.MemberMapper;
import com.example.researchlab.member.model.vo.Member;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

    @Override
    public Member login(Member member) {
        logger.info("회원 로그인 시도: {}", member.getMemberId());

        // 회원 정보 조회
        Member loginMember = memberMapper.login(member);
        if (loginMember == null) {
            logger.warn("존재하지 않는 회원: {}", member.getMemberId());
            return null;  // 존재하지 않는 회원
        }

        // 비밀번호 검증
        if (!passwordEncoder.matches(member.getMemberPwd(), loginMember.getMemberPwd())) {
            logger.warn("비밀번호 불일치: {}", member.getMemberId());
            return null;  // 비밀번호 불일치
        }

        logger.info("비밀번호 일치: {}", member.getMemberId());
        return loginMember;  // 로그인 성공한 회원 객체 반환
    }

    @Override
    public int enrollMember(Member member) {
        logger.info("회원가입: {}", member.getMemberId());
        String encodedPassword = passwordEncoder.encode(member.getMemberPwd());
        member.setMemberPwd(encodedPassword);

        int result = memberMapper.enrollMember(member);
        if (result > 0) {
            logger.info("회원가입 성공: {}", member.getMemberId());
        } else {
            logger.warn("회원가입 실패: {}", member.getMemberId());
        }

        return result;
    }
}

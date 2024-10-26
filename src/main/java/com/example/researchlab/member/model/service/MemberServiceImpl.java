package com.example.researchlab.member.model.service;

import com.example.researchlab.config.auth.JwtUtil;
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
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder bc;
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
        if (!bc.matches(member.getMemberPwd(), loginMember.getMemberPwd())) {
            logger.warn("비밀번호 불일치: {}", member.getMemberId());
            return null;  // 비밀번호 불일치
        }

        // 비밀번호가 일치하면 JWT 토큰 생성
        logger.info("로그인 성공: {}", member.getMemberId());
        String token = jwtUtil.generateToken(member.getMemberId());
        loginMember.setToken(token);  // Member 객체에 토큰 설정 (필드 추가 필요)

        return loginMember;  // 토큰이 포함된 회원 객체 반환
    }

    @Override
    public int enrollMember(Member member) {
        logger.info("회원가입: {}", member.getMemberId());
        String encodedPassword = bc.encode(member.getMemberPwd());
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

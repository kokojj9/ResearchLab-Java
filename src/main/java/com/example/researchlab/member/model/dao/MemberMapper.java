package com.example.researchlab.member.model.dao;

import com.example.researchlab.member.model.vo.Member;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface MemberMapper {

    Member login(Member member);

}

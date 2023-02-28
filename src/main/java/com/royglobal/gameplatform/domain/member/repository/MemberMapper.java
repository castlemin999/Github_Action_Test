package com.royglobal.gameplatform.domain.member.repository;

import com.royglobal.gameplatform.domain.member.domain.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    void save(Member member);

    Member findById(int mbrNo);
}

package com.royglobal.gameplatform.domain.member.service;

import com.royglobal.gameplatform.domain.member.entity.Member;
import com.royglobal.gameplatform.domain.member.repository.MemberMapper;
import com.royglobal.gameplatform.global.common.aop.LoggingAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 회원 마스터 Service
 */
@Service
public class MemberService {

    @Autowired
    private MemberMapper memberMapper;

    /**
     * 특정 회원 정보 조회
     * @param mbrNo
     * @return
     */
    public Member findById(int mbrNo){
        return memberMapper.findById(mbrNo);
    }

}

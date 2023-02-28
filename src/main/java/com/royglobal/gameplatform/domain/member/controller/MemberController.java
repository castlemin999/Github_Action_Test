package com.royglobal.gameplatform.domain.member.controller;

import com.royglobal.gameplatform.domain.member.domain.Member;
import com.royglobal.gameplatform.domain.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 마스터 Controller
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    /**
     * 특정 회원 정보 조회
     * @param mbrNo
     * @return Member
     */
    @GetMapping("/{mbrNo}")
    public Member get(@PathVariable int mbrNo){
        return memberService.findById(mbrNo);
    }


}

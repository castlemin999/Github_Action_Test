package com.royglobal.gameplatform.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.royglobal.gameplatform.domain.member.entity.Member;
import com.royglobal.gameplatform.domain.member.dto.TestMemberDto;
import com.royglobal.gameplatform.domain.member.service.MemberService;
import com.royglobal.gameplatform.global.common.aop.LoggingAspect;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 회원 마스터 Controller
 */
@RestController
@RequestMapping("/member")
@Log4j2
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

    @GetMapping("/param")
    public String param(@RequestParam int id, @RequestParam String name){
        return "success";
    }

    @PostMapping("/body")
    public TestMemberDto body(@RequestBody TestMemberDto tmd){
        return tmd;
    }
}

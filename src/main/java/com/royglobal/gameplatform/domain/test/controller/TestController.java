package com.royglobal.gameplatform.domain.test.controller;

import com.royglobal.gameplatform.domain.test.dto.TestMemberDto;
import org.springframework.web.bind.annotation.*;

/**
 * 회원 마스터 Controller
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hellostring")
    public String string(){
        return "hello";
    }

    @PostMapping("/hellodto")
    public TestMemberDto dto(@RequestBody TestMemberDto tmd){
        return tmd;
    }
}

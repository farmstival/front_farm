package com.joyfarm.farmstival.member.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class MemberController {

    @GetMapping("/token")
    public String token(){
     // 토큰 발급 - 로그인 절차 중 하나
        return "token";
    }
}

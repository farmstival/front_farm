package com.joyfarm.farmstival.member.controllers;

import com.joyfarm.farmstival.global.Utils;
import com.joyfarm.farmstival.global.exceptions.BadRequestException;
import com.joyfarm.farmstival.member.validators.JoinValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class MemberController {

    private final JoinValidator joinValidator;
    private final Utils utils;

    /* 회원 가입 시 응답 코드 201 */
    @PostMapping // /account 쪽에 Post 방식으로 접근하면 -> 회원가입
    public ResponseEntity join(@RequestBody @Valid RequestJoin form, Errors errors){
        // 회원 가입 정보는 JSON 데이터로 전달 -> @RequestBody

        joinValidator.validate(form, errors);

        if (errors.hasErrors()){
            throw new BadRequestException(utils.getErrorMessages(errors));
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /* 로그인 절차 완료 시 토큰(=교환권) 발급 */
    @GetMapping("/token")
    public String token(){

        return "token";
    }
}

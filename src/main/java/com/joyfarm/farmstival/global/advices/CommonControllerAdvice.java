package com.joyfarm.farmstival.global.advices;

import com.joyfarm.farmstival.global.Utils;
import com.joyfarm.farmstival.global.exceptions.CommonException;
import com.joyfarm.farmstival.global.rests.JSONData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestControllerAdvice("com.joyfarm.farmstival")
public class CommonControllerAdvice {

    private final Utils utils;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JSONData> errorHandler(Exception e) {

        Object message = e.getMessage();

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500
        if (e instanceof CommonException commonException) {
            status = commonException.getStatus();

            // 에러코드인 경우는 메세지를 조회할 수 있도록 함 (commonException의 하위 클래스)
            if (commonException.isErrorCode()) message = utils.getMessage(e.getMessage());
            
            Map<String, List<String>> errorMessages = commonException.getErrorMessages();
            if (errorMessages != null) message = errorMessages;
        }

        String loginErrorCode = null;
        if (e instanceof BadCredentialsException) { // 아이디 또는 비밀번호가 일치하지 않는 경우
            loginErrorCode = "BadCredentials.Login";
        } else if (e instanceof DisabledException) { // 탈퇴한 회원
            loginErrorCode = "Disabled.Login";
        } else if (e instanceof CredentialsExpiredException) { // 비밀번호 유효기간 만료
            loginErrorCode = "CredentialsExpired.Login";
        } else if (e instanceof AccountExpiredException) { // 사용자 계정 유효기간 만료
            loginErrorCode = "AccountExpired.Login";
        } else if (e instanceof LockedException) { // 사용자 계정이 잠겨있는 경우
            loginErrorCode = "Locked.Login";
        } else if (e instanceof AuthenticationException){
            loginErrorCode = "Fail.Login";
        }

        if (StringUtils.hasText(loginErrorCode)) {
            message = utils.getMessage(loginErrorCode);
            status = HttpStatus.UNAUTHORIZED;
        }
        // 코드에 따라 메세지가 다르게 추가된다.

        JSONData data = new JSONData();
        data.setSuccess(false);
        data.setMessage(message);
        data.setStatus(status);

        e.printStackTrace();

        return ResponseEntity.status(status).body(data);
    }
}

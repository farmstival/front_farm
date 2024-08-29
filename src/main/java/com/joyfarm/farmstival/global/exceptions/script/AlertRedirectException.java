package com.joyfarm.farmstival.global.exceptions.script;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

@Getter
public class AlertRedirectException extends AlertException {
    private String url;
    private String target;

    //페이지 이동
    public AlertRedirectException(String message, String url, HttpStatus status, String target) {
        super(message, status);

        target = StringUtils.hasText(target)? target : "self";
        //타겟이 없을 때는 self(현재 창)으로 고정

        this.url = url;
        this.target = target;
    }

    //타겟이 없는 경우
    public AlertRedirectException(String message, String url, HttpStatus status) {
        this(message, url, status, null); //값이 없으면 위 함수를 통해 self로 이동
        //null 대신에 "  ", "self" 넣어도 가능함
    }
}